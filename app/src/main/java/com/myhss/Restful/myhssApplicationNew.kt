package com.uk.myhss.Restful

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.uk.myhss.Utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyHssApplicationNew private constructor() : MultiDexApplication() {
    private lateinit var sessionManager: SessionManager
    private val apiService: ApiInterface? = null
    private val context: Context? = null
    private var retrofit: Retrofit?
    val api: ApiInterface
        get() {
            if (retrofit == null) {
                retrofit = provideRetrofit(BaseURL)
            }
            return retrofit!!.create(ApiInterface::class.java)
        }

    private fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)

        okhttpClientBuilder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
//                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
//                    .header("Pragma", "no-cache")
//                    .header("Expires", "0")
//                        .header("Accept", "application/pyur.v1")
//                    .header("Authorization", "Bearer ${sessionManager.fetchAuthToken()}")
//                        .header("Content-Type", "application/json; charset=UTF-8")
//                        .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                    .build()
                return chain.proceed(request)
            }
        }).build()

        /*sessionManager = SessionManager(this)
        Log.d("SessionToken", sessionManager.fetchAuthToken().toString())

        okhttpClientBuilder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
//                        .header("Accept", "application/pyur.v1")
                    .header("Authorization", "Bearer ${sessionManager.fetchAuthToken()}")
//                        .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                    .build()
                return chain.proceed(request)
            }
        }).build()*/

//    val httpClient = OkHttpClient.Builder()
        /*okhttpClientBuilder.addInterceptor { chain ->
    val original = chain.request()
    sessionManager = SessionManager(this)
    // Request customization: add request headers
    val requestBuilder = original.newBuilder()
        .header("Authorization", "Bearer ${sessionManager.fetchAuthToken()}") // <-- this is the important line
        .addHeader("Content-Type", "application/json")
    val request = requestBuilder.build()
    chain.proceed(request)
}*/
//            logging.redactHeader("Authorization"+ "Bearer ${sessionManager.fetchAuthToken()}")

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okhttpClientBuilder.addInterceptor(logging)

        return okhttpClientBuilder.build()
    }

    private fun provideRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    companion object {
        var BaseURL = "https://myhss.org.uk/"    // Production URL
//        var BaseURL = "https://stg.myhss.org.uk/"    // Staging URL
//        var BaseURL = "https://dev.myhss.org.uk/api/"   // Developer

        var IMAGE_PDF_URL = BaseURL + "assets/qualification_file/"  //Production URL
//        var IMAGE_PDF_URL = BaseURL + "assets/qualification_file/" //Staging URL
//        var IMAGE_PDF_URL = BaseURL + "assets/qualification_file/"  //Developement Url

        @SuppressLint("StaticFieldLeak")
        private var minstance: MyHssApplicationNew? = null

        @get:Synchronized
        val instance: MyHssApplicationNew?
            get() {
                if (minstance == null) {
                    minstance = MyHssApplicationNew()
                }
                return minstance
            }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}