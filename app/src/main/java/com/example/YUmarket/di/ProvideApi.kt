package com.example.YUmarket.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.YUmarket.data.network.MapApiService
import com.example.YUmarket.data.network.home.TownMarketApiService
import com.example.YUmarket.data.url.Url
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun provideMapApiService(retrofit: Retrofit): MapApiService {
    return retrofit.create(MapApiService::class.java)
}

/**
 * TownMarket에 대한 network service를 제공하는 메소드
 * @author Doyeop Kim (main)
 * @since 2022/01/28
 */
fun provideTownMarketApiService(retrofit: Retrofit): TownMarketApiService = retrofit.create(TownMarketApiService::class.java)

fun provideMapRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.TMAP_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

/**
 * YU_Market 자체 서버에 대한 retrofit을 제공하는 메소드
 * @author Doyeop Kim (main)
 * @since 2022/01/28
 */
fun provideYuMarketRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.YU_MARKET_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

// 2022/01/28 type adapter 장착 by 김도엽
@RequiresApi(Build.VERSION_CODES.O)
fun provideGsonConverterFactory(): GsonConverterFactory {
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter().nullSafe())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter().nullSafe())
        .create()

    return GsonConverterFactory.create(gson)
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()

//        if (BuildConfig.DEBUG) {
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            interceptor.level = HttpLoggingInterceptor.Level.NONE
//        }

    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}