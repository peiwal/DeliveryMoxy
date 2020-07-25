package petrov.ivan.deliverymobile.presentation.presenter.delivery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.view.delivery.ContactsView
import petrov.ivan.deliverymobile.service.IMobileClientApiRX

@InjectViewState
class ContactsPresenter(val restApi: IMobileClientApiRX) : MvpPresenter<ContactsView>() {
    val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun loadCompanyInfo() {
        val disposable = restApi.getCompanyInfo()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showCompanyInfo(it)
            },{
                viewState.showError(R.string.error_load_data)
            })
        compositeDisposable.add(disposable)
    }
}
