package petrov.ivan.deliverymobile.modules

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import petrov.ivan.deliverymobile.AppConstants
import petrov.ivan.deliverymobile.BuildConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module(includes = arrayOf(ContextModule::class))
class OkHttpClientModule {

    @Provides
    fun client(): OkHttpClient {
        if (BuildConfig.DEBUG) {
            return OkHttpClient().newBuilder()
                .cache(null)
                .addInterceptor(authInterceptor())
                .addInterceptor(httpLoggingInterceptor())
                .connectTimeout(AppConstants.CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build()
        } else {
            return OkHttpClient().newBuilder()
                .cache(null)
                .addInterceptor(httpLoggingInterceptor())
                .addInterceptor(authInterceptor())
                .connectTimeout(AppConstants.CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
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
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }
}
