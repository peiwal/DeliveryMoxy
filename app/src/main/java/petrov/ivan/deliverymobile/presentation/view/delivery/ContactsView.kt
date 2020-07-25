package petrov.ivan.deliverymobile.presentation.view.delivery

import com.arellomobile.mvp.MvpView
import petrov.delivery.webapi.ParamRespCompanyInfo

interface ContactsView : MvpView {
    fun showCompanyInfo(companyInfo: ParamRespCompanyInfo)
    fun showError(errorCode: Int)
}
