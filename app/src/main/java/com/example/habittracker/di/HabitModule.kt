package com.example.habittracker.di

import android.content.Context
import androidx.room.Room
import com.example.data.*
import com.example.domain.*
import com.example.domain.usecases.EditHabitUseCase
import com.example.domain.usecases.GetHabitToEditUseCase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class HabitModule(private val repository: HabitsRepository? = null) {
    @Provides
    fun provideGetHabitToEditUseCase(repository: HabitsRepository): GetHabitToEditUseCase {
        return GetHabitToEditUseCase(repository)
    }

    @Provides
    fun provideEditHabitUseCase(repository: HabitsRepository): EditHabitUseCase {
        return EditHabitUseCase(repository)
    }

    @Provides
    fun provideMarkHabitAsCompletedUseCase(repository: HabitsRepository): MarkHabitAsCompletedUseCase {
        return MarkHabitAsCompletedUseCase(repository)
    }

    @Provides
    fun provideFilterAndSortHabitUseCase(repository: HabitsRepository): FilterAndSortHabitsUseCase {
        return FilterAndSortHabitsUseCase(repository)
    }

    @Provides
    fun provideDeleteHabitUseCase(repository: HabitsRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(repository)
    }

    @Provides
    fun provideInsertHabitUseCase(repository: HabitsRepository): InsertHabitUseCase {
        return InsertHabitUseCase(repository)
    }

    @Provides
    fun provideGetAllHabitsUseCase(repository: HabitsRepository): GetAllHabitsUseCase {
        return GetAllHabitsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideHabitRepository(habitDao: HabitDao, habitApi: HabitApi): HabitsRepository {
        return repository ?: HabitRepository(habitDao, habitApi)
    }

    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitDao {
        return db.habitDao()
    }

    @Singleton
    @Provides
    fun provideHabitDatabase(context: Context): HabitDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habit_database"
            ).build()
        }
    }

    @Singleton
    @Provides
    fun provideHabitRetrofitService(): HabitApi {
        val token = "588f08fd-d56b-4149-9c54-926677ec98d9"
        val baseUrl = "https://droid-test-server.doubletapp.ru/api/"
        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()
                .header("Authorization", token)
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
            .create()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HabitApi::class.java)
    }
}