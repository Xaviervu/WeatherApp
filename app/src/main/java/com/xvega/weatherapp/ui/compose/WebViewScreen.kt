package com.xvega.weatherapp.ui.compose

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(modifier: Modifier = Modifier) {
//    val defaultUrl = "https://weather.com/weather/today/l/34f2aafc84cff75ae0b014754856ea5e7f8ddf618cf9735549dfb5e016c28e10"
    val defaultUrl = "https://www.roboform.com/filling-test-all-fields"
    var textValue by rememberSaveable { mutableStateOf(defaultUrl) }
    var urlToLoad by rememberSaveable { mutableStateOf(defaultUrl) }
    val isDark = isSystemInDarkTheme()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    val context = LocalContext.current
    val webView = remember {
        NestedScrollingWebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isRefreshing = false
                    url?.let { 
                        if (it != textValue) {
                            textValue = it
                        }
                    }
                }
            }
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(true)
            }
            contentDescription = "Web Content"
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    // Handle lifecycle and prevent "hanging"
    DisposableEffect(webView) {
        webView.onResume()
        webView.resumeTimers()
        onDispose {
            webView.onPause()
            webView.pauseTimers()
        }
    }

    fun updateUrl() {
        val trimmed = textValue.trim()
        val formatted = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.startsWith("www.") -> "https://$trimmed"
            else -> "https://www.$trimmed"
        }
        urlToLoad = formatted
        webView.loadUrl(urlToLoad)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Address") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                visualTransformation = UrlVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = { updateUrl() }
                ),
                trailingIcon = {
                    IconButton(onClick = { updateUrl() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go")
                    }
                }
            )
        }
        
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                isRefreshing = true
                webView.reload()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            AndroidView(
                factory = { webView },
                update = { view ->
                    // Handle Dark Mode
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        val forceDark = if (isDark) WebSettingsCompat.FORCE_DARK_ON else WebSettingsCompat.FORCE_DARK_OFF
                        WebSettingsCompat.setForceDark(view.settings, forceDark)
                    }

                    // Algorithmic darkening is better for some sites if supported
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                        WebSettingsCompat.setAlgorithmicDarkeningAllowed(view.settings, isDark)
                    }
                    
                    if (view.url == null) {
                        view.loadUrl(urlToLoad)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .semantics {
                        contentDescription = "Weather Web View"
                    }
            )
        }
    }
}

class NestedScrollingWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle
) : WebView(context, attrs, defStyleAttr), NestedScrollingChild3 {

    private val childHelper = NestedScrollingChildHelper(this).apply {
        isNestedScrollingEnabled = true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val returnValue: Boolean

        val eventCopy = MotionEvent.obtain(event)
        val action = eventCopy.actionMasked

        if (action == MotionEvent.ACTION_DOWN) {
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
        }

        returnValue = super.onTouchEvent(event)

        if (action == MotionEvent.ACTION_UP) {
            performClick()
        }

        if (action == MotionEvent.ACTION_MOVE) {
            // Very simple nested scroll implementation: 
            // if we are at the top and pulling down, we let nested scroll happen.
            if (scrollY <= 0 && event.y > 0) {
                 // Dispatching nested scroll helps PullToRefresh know we are pulling
                 dispatchNestedScroll(0, 0, 0, 0, null, ViewCompat.TYPE_TOUCH)
            }
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            stopNestedScroll(ViewCompat.TYPE_TOUCH)
        }
        eventCopy.recycle()
        return returnValue
    }

    // NestedScrollingChild3 implementation
    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean = childHelper.startNestedScroll(axes, type)
    override fun stopNestedScroll(type: Int) = childHelper.stopNestedScroll(type)
    override fun hasNestedScrollingParent(type: Int): Boolean = childHelper.hasNestedScrollingParent(type)
    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean =
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean =
        childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)

    // Legacy NestedScrollingChild
    override fun setNestedScrollingEnabled(enabled: Boolean) { childHelper.isNestedScrollingEnabled = enabled }
    override fun isNestedScrollingEnabled(): Boolean = childHelper.isNestedScrollingEnabled
    override fun startNestedScroll(axes: Int): Boolean = childHelper.startNestedScroll(axes)
    override fun stopNestedScroll() = childHelper.stopNestedScroll()
    override fun hasNestedScrollingParent(): Boolean = childHelper.hasNestedScrollingParent()
    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean =
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean =
        childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean =
        childHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean =
        childHelper.dispatchNestedPreFling(velocityX, velocityY)
}

class UrlVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val url = text.text
        val annotatedString = buildAnnotatedString {
            val protocolEnd = url.indexOf("://").let { if (it == -1) 0 else it + 3 }
            val domainWithWwwStart = protocolEnd
            val domainStart = if (url.startsWith("www.", domainWithWwwStart)) {
                domainWithWwwStart + 4
            } else {
                domainWithWwwStart
            }
            
            val nextSlash = url.indexOf("/", domainStart)
            val domainEnd = if (nextSlash == -1) url.length else nextSlash

            // Everything before main domain (protocol + www.)
            if (domainStart > 0) {
                withStyle(SpanStyle(color = Color.Gray)) {
                    append(url.substring(0, domainStart))
                }
            }

            // Main Domain
            append(url.substring(domainStart, domainEnd))

            // Everything after main domain
            if (domainEnd < url.length) {
                withStyle(SpanStyle(color = Color.Gray)) {
                    append(url.substring(domainEnd))
                }
            }
        }

        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}
