package xyz.katiedotson.gqlk

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServiceFactory {
    companion object {
        fun buildApiService(baseUrl: String): ApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ErrorHandlingInterceptor())
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiService::class.java)
        }
    }

    class ErrorHandlingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val response = chain.proceed(request)
            return response.newBuilder().code(200).build()
        }
    }
}