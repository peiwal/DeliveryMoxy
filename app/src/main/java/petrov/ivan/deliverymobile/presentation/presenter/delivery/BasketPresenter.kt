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
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.data.Product
import petrov.ivan.deliverymobile.data.ProductInBasket
import petrov.ivan.deliverymobile.presentation.view.delivery.BasketView
import java.math.BigDecimal


@InjectViewState
class BasketPresenter(val basket: Basket) : MvpPresenter<BasketView>(), IAdapterBasket {
    private val compositeDisposable = CompositeDisposable()

    fun showProducts() {
        val disposable = basket.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    val products = ArrayList<ProductInBasket>()
                    list?.forEach {
                        products.add(ProductInBasket(Product(it.idx, it.name, it.description, it.ingredients, it.imgUrl, it.cost), it.count))
                    }
                    val isEmpty = products.isEmpty()
                    viewState.isEmptyBasket(isEmpty)
                    if (!isEmpty) {
                        viewState.showProducts(products)
                    }
                },
                { error ->
                    viewState.showError(R.string.error_load_data)
                })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun addCount(product: Product, count: Int) {
        Completable.fromAction {
            basket.updateProduct(product, count + 1)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }
            override fun onComplete() {}
            override fun onError(e: Throwable) {
                viewState.showError(R.string.error_unknown)
            }
        })
    }

    override fun reduceCount(product: Product, count: Int) {
        Completable.complete().doOnComplete {
            if (count > 1) {
                basket.updateProduct(product, count - 1)
            } else {
                basket.deleteProduct(product.idx)
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }
            override fun onComplete() {}
            override fun onError(e: Throwable) {
                viewState.showError(R.string.error_unknown)
            }
        })
    }

    fun createOrder() {
        Completable.fromAction {
            basket.deleteAllProducts()
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object: CompletableObserver {
            override fun onComplete() {
                viewState.orderCreated()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
                viewState.showError(R.string.basket_error_create_order)
            }
        })
    }

    fun subscribeChangeFullCost() {
        val disposable = basket.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                var cost = BigDecimal.ZERO
                list?.forEach {
                    cost = cost.add(it.cost * BigDecimal(it.count))
                }
                viewState.updateFullCost(cost)
            }
        compositeDisposable.add(disposable)
    }
}

interface IAdapterBasket {
    fun addCount(product: Product, count: Int)
    fun reduceCount(product: Product, count: Int)
}
