import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.xvega.weatherapp.api.Connection
import com.xvega.weatherapp.api.WeatherAppInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TIME_OUT: Long = 120

    private val gson = GsonBuilder().setStrictness(Strictness.LENIENT).create()

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val resp = chain.proceed(chain.request())
            // Deal with the response code
            if (resp.code == 200 || resp.code == 400) {
                try {
                    val myJson =
                        resp.peekBody(2048).string() // peekBody() will not close the response
                    println(myJson)
                } catch (e: retrofit2.HttpException) {
                    println("HttpException:  ${e.response()?.errorBody()?.string()}")
                }
            } else {
                println(resp)
            }
            resp
        }.build()

    val retrofit: WeatherAppInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Connection.BASE_URL)
            .client(okHttpClient)
            .build().create(WeatherAppInterface::class.java)
    }

}