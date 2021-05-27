package com.example.presentation

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.data.*
import com.example.domain.Habit
import com.google.gson.GsonBuilder
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Singleton
//@Component(modules = [HabitModule::class, SubcomponentModule::class])
//interface ApplicationComponent{
//    fun habitComponent(): HabitComponent.Factory
//}
//
//@Module(subcomponents = [HabitComponent::class])
//class SubcomponentModule{}
//
//@Subcomponent
//interface HabitComponent{
//
//    @Subcomponent.Factory
//    interface Factory{
//        fun create(): HabitComponent
//    }
//
//    fun inject(mainPageFragment: MainPageFragment)
//}

//@Module
//class HabitModule {
//    @Provides
//    fun provideHabitRepository(habitDao: HabitDao, habitApi: HabitApi): HabitRepository {
//        return HabitRepository(habitDao, habitApi)
//    }
//
//    @Provides
//    fun provideHabitDao(db: HabitDatabase): HabitDao{
//        return db.habitDao()
//    }
//
//    @Provides
//    fun provideHabitRetrofitService(retrofit: Retrofit): HabitApi{
//        return retrofit.create(HabitApi::class.java)
//    }
//
//    @Singleton
//    @Provides
//    fun providesHabitDatabase(context: Context): HabitDatabase {
//        return Room.databaseBuilder(
//            context.applicationContext,
//            HabitDatabase::class.java,
//            "habit_database"
//        ).build()
//    }
//
//    @Singleton
//    @Provides
//    fun providesRetrofit(): Retrofit {
//        val token = "588f08fd-d56b-4149-9c54-926677ec98d9"
//        val baseUrl = "https://droid-test-server.doubletapp.ru/api/"
//        val interceptor = Interceptor { chain ->
//            val originalRequest = chain.request()
//            val builder = originalRequest.newBuilder()
//                .header("Authorization", token)
//            val newRequest = builder.build()
//            chain.proceed(newRequest)
//        }
//
//        val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
//
//        val gson = GsonBuilder()
//            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
//            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
//            .create()
//
//        return Retrofit.Builder()
//            .client(client)
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
////            .create(HabitApi::class.java)
//    }
//}