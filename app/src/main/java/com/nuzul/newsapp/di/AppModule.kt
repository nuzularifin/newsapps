package com.nuzul.newsapp.di

import android.app.Application
import android.content.Context
import com.nuzul.newsapp.core.util.Constants
import com.nuzul.newsapp.data.remote.NewsAPI
import com.nuzul.newsapp.data.repository.NewsRepositoryImpl
import com.nuzul.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideNewsAPI() : NewsAPI {
        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)

        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsAPI) : NewsRepository {
        return NewsRepositoryImpl(api)
    }

//    @Provides
//    @Singleton
//    fun providesGlide() :

}