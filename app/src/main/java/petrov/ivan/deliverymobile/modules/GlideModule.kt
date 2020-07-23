package petrov.ivan.deliverymobile.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import petrov.ivan.deliverymobile.components.ApplicationScope

@Module(includes = arrayOf(ContextModule::class))
class GlideModule {
    @ApplicationScope
    @Provides
    fun glide(context: Context): RequestManager {
        return  Glide.with(context)
    }
}