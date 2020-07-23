package petrov.ivan.deliverymobile.presentation.presenter.delivery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.presentation.view.delivery.ProductInfoView

@InjectViewState
class ProductInfoPresenter(val basket: Basket) : MvpPresenter<ProductInfoView>() {
    private val compositeDisposable = CompositeDisposable()

    fun showProduct(product: Product) {
        viewState.showProduct(product)
    }

    fun addToBasket(product: Product) {
        Completable.fromAction {
            basket.addProduct(product)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: CompletableObserver {
                override fun onComplete() {
                    viewState.succesAddToBusket()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    viewState.showError(null)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
