package com.example.frontend.data.api

import android.util.Base64
import com.example.frontend.data.auth.SessionManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val bURL = "http://192.168.0.111:8080/"
    private var sm: SessionManager? = null
    
    fun init(s: SessionManager) {
        sm = s
    }
    
    private val auth = Interceptor { chain ->
        val req = chain.request()
        val builder = req.newBuilder()
        
        sm?.let { s ->
            val token = s.getAuthToken()
            if (token.isNotEmpty()) {
                builder.header("Authorization", "Bearer $token")
            } else {
                val login = s.getLogin()
                val pass = s.getPassword()
                if (login.isNotEmpty() && pass.isNotEmpty()) {
                    val cred = "$login:$pass"
                    val basic = "Basic " + Base64.encodeToString(
                        cred.toByteArray(),
                        Base64.NO_WRAP
                    )
                    builder.header("Authorization", basic)
                } else { }
            }
        }
        
        chain.proceed(builder.build())
    }
    
    private val log = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(auth)
        .addInterceptor(log)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(bURL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    
    val meetupApi: MeetupApiService by lazy {
        retrofit.create(MeetupApiService::class.java)
    }
}
