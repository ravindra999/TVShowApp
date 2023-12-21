package com.themoviedb.weektvshow.di

import com.google.gson.Gson
import com.themoviedb.weektvshow.BuildConfig
import com.themoviedb.weektvshow.data.network.NetworkResponseAdapterFactory
import com.themoviedb.weektvshow.data.network.TVShowApiService
import com.themoviedb.weektvshow.utils.ExcludeDeserializationStrategy
import com.themoviedb.weektvshow.utils.ExcludeSerializationStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(okHttpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(okHttpLoggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val json = Gson().newBuilder()
            .addSerializationExclusionStrategy(ExcludeSerializationStrategy())
            .addDeserializationExclusionStrategy(ExcludeDeserializationStrategy())
            .setLenient()
            .create()

        return GsonConverterFactory.create(json)
    }

    @Singleton
    @Provides
    fun provideRetrofitTheMovieDBApiInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TVSHOW_BASE_URL
            )
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideTheMovieDBApiService(retrofit: Retrofit): TVShowApiService {
        return retrofit.create(TVShowApiService::class.java)
    }
}