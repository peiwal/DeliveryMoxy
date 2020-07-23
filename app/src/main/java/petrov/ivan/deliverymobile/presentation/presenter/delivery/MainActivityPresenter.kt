package petrov.ivan.deliverymobile.presentation.presenter.delivery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import petrov.ivan.deliverymobile.Basket
import petrov.ivan.deliverymobile.presentation.view.delivery.MainActivityView


@InjectViewState
class MainActivityPresenter(val basket: Basket) : MvpPresenter<MainActivityView>() {
    private val compositeDisposable = CompositeDisposable()

    fun observeBasketCount() {
        val disposable = basket.getProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                var count = 0
                list?.forEach {
                    count += it.count
                }
                viewState.updateBasketCount(count)
            }
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
