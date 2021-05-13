package com.example.habittracker

import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitJsonDeserializer
import com.example.habittracker.model.HabitJsonSerializer
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.connection.ConnectInterceptor
import okhttp3.internal.http.RetryAndFollowUpInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val TOKEN = "588f08fd-d56b-4149-9c54-926677ec98d9"
    private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"

    val SERVICE: HabitApi by lazy {
        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()
                .header("Authorization", TOKEN)
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
            .create()

        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HabitApi::class.java)
    }
}