package petrov.ivan.deliverymobile.modules

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import petrov.ivan.deliverymobile.BuildConfig
import timber.log.Timber

@Module(includes = arrayOf(ContextModule::class))
class OkHttpClientModule {

    @Provides
    fun client(): OkHttpClient {
        if (BuildConfig.DEBUG) {
            return OkHttpClient().newBuilder()
                .cache(null)
                .addInterceptor(authInterceptor())
                .addInterceptor(httpLoggingInterceptor())
                .build()
        } else {
            return OkHttpClient().newBuilder()
                .cache(null)
                .addInterceptor(httpLoggingInterceptor())
                .addInterceptor(authInterceptor())
                .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Timber.d(message)
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    private fun authInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newUrl = chain.request().url()
                .newBuilder()
                //.addQueryParameter("api_key", AppConstants.API_KEY)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }
}