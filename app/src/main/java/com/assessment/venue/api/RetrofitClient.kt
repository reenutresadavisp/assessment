package com.assessment.venue.api

import com.assessment.venue.BuildConfig.*
import com.assessment.venue.util.Constants.API_PARAM_CLIENT_ID
import com.assessment.venue.util.Constants.API_PARAM_CLIENT_SECRET
import com.assessment.venue.util.Constants.API_PARAM_VERSION_DATE
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handles instantiation of Retrofit Api Service
 */
object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(gethttpClient())
        .build()

    //Method to create instance of VenueApiService using Retrofit
    fun getVenueApiService(): VenueApiService {
        return retrofit.create(VenueApiService::class.java)
    }

    //Method to create Http Client with logging interceptor and queryInterceptor
    private fun gethttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val queryInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter(API_PARAM_CLIENT_ID, API_PARAM_CLIENT_ID_VALUE)
                .addQueryParameter(API_PARAM_CLIENT_SECRET, API_PARAM_CLIENT_SECRET_VALUE)
                .addQueryParameter(API_PARAM_VERSION_DATE, API_PARAM_VERSION_DATE_VALUE)
                .build()

            val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)

            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }


        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(queryInterceptor)
        return httpClient.build()
    }
}