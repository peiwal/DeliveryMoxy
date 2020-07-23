package petrov.ivan.deliverymobile.modules

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import petrov.ivan.deliverymobile.AppConstants
import petrov.ivan.deliverymobile.components.ApplicationScope
import petrov.ivan.deliverymobile.service.RestApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(includes = arrayOf(OkHttpClientModule::class))
class DeliveryModule {

    @ApplicationScope
    @Provides
    fun deliveryApi(okHttpClient: OkHttpClient): RestApi {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(moshiConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(RestApi::class.java)
    }

    private fun moshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @ApplicationScope
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}