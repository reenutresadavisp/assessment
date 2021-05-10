package com.assessment.venue.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.foursquare.com/v2/venues/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(gethttpClient())
        .build()

    fun getVenueApiService(): VenueApiService {
        return retrofit.create(VenueApiService::class.java)
    }

    private fun gethttpClient(): OkHttpClient{

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val queryInterceptor = Interceptor{ chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("client_id", "VHKGU0VROG3LLBTPUQIIYWMTDKD01QJ3QDZDXHPH0Z100SFK")
                .addQueryParameter("client_secret", "AKMI2N0Y2KTN541ZOFGF2PRQVKCXM2SGLD2O2UYOIHZOSAQV")
                .addQueryParameter("v", "20210509")
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