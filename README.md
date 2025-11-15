A test app for retreiving weather information in Moscow and showing details using only Compose:
1. Day temperature, 
2. Hourly temperature for 3 days 
3. If there's a connection problem, a Error screen should be shown that should let refresh the data

Image data is cached using local storage so it's not retreived every time from URL using Coil library for compose

https://api.weatherapi.com/v1/forecast.json?key=fa8b3df74d4042b9aa7135114252304&q=55.7569,37.6151&days=3

A MVVM pattern was used for simplicity and reliability
Stack:
Coroutines, SharedFlow, StateFlow, Compose, ViewModel, Coil, Retrofit

![weather1](https://github.com/user-attachments/assets/af5ea02c-ed48-4f22-909b-2af9a4db6364)


![weather2](https://github.com/user-attachments/assets/9a109dfe-c035-43f3-a160-cc1b35007f1e)
