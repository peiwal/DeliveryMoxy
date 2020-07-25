package petrov.ivan.deliverymobile.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import petrov.ivan.deliverymobile.AppConstants
import petrov.ivan.deliverymobile.components.ApplicationScope
import petrov.ivan.deliverymobile.service.IMobileClientApiRX
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Module(includes = arrayOf(OkHttpClientModule::class))
class DeliveryModule {

    @ApplicationScope
    @Provides
    fun deliveryApi(okHttpClient: OkHttpClient): IMobileClientApiRX {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(jacksonConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(IMobileClientApiRX::class.java)
    }

    private fun jacksonConverterFactory(): JacksonConverterFactory {
        return JacksonConverterFactory.create(objectMapper())
    }

    @ApplicationScope
    @Provides
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }

    @ApplicationScope
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}