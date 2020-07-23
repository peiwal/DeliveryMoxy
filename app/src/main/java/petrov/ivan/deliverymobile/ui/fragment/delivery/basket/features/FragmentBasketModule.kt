package petrov.ivan.deliverymobile.ui.fragment.delivery.basket.features

import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import petrov.ivan.deliverymobile.presentation.presenter.delivery.BasketPresenter
import petrov.ivan.deliverymobile.ui.adapters.BasketAdapter

@Module
class FragmentBasketModule(private val presenter: BasketPresenter) {

    @FragmentBasketScope
    @Provides
    fun basketAdapter(requestManager: RequestManager): BasketAdapter {
        return BasketAdapter(presenter, requestManager)
    }
}
