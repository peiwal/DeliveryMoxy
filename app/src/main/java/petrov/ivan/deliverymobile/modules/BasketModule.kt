package petrov.ivan.deliverymobile.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.components.ApplicationScope

@Module(includes = arrayOf(ContextModule::class))
class BasketModule {

    @ApplicationScope
    @Provides
    fun basket(context: Context): Basket {
        return Basket(context)
    }
}