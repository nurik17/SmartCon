package kz.cifron.smartcon.feature_result.domain

import kz.cifron.smartcon.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ResultRetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val tokenInterceptor = Interceptor{chain ->
        val originalRequest = chain.request()
        val token = Constant.TOKEN
        val requestWidthToken = originalRequest.newBuilder()
            .header("Authorization","Bearer $token")
            .build()
        chain.proceed(requestWidthToken)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

    val resultInstanceApi: ResultApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ResultApi::class.java)
    }
}