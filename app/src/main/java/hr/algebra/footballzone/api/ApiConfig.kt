package hr.algebra.footballzone.api

import hr.algebra.footballzone.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    const val API_URL = "https://v3.football.api-sports.io/"
    const val HEADER_API_KEY = "x-apisports-key"

    private val apiKeyInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(HEADER_API_KEY, BuildConfig.FOOTBALL_API_KEY)
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val footballApi: FootballZoneApi = retrofit.create(FootballZoneApi::class.java)
}