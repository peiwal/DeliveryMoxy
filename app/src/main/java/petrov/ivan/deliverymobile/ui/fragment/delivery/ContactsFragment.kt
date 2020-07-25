package petrov.ivan.deliverymobile.ui.fragment.delivery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_contacts.*
import petrov.delivery.webapi.ParamRespCompanyInfo
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.presenter.delivery.ContactsPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.ContactsView
import petrov.ivan.deliverymobile.service.IMobileClientApiRX
import petrov.ivan.deliverymobile.ui.activity.delivery.CompanyOnMapActivity
import petrov.ivan.deliverymobile.ui.base.BaseFragment


class ContactsFragment : BaseFragment(), ContactsView {
    private val deliveryService: IMobileClientApiRX by lazy(mode = LazyThreadSafetyMode.NONE) {
        deliveryComponents.getDeliveryService()
    }

    @InjectPresenter
    lateinit var presenter: ContactsPresenter

    @ProvidePresenter
    fun providePresenter(): ContactsPresenter = ContactsPresenter(deliveryService)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    val compositeDisposable = CompositeDisposable()
    var companyInfoObservable: Subject<ParamRespCompanyInfo> = PublishSubject.create()
    private var companyInfo: ParamRespCompanyInfo? = null
        set(value) {
            field = value
            field?.let {
                companyInfoObservable.onNext(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnShowOnMap.setOnClickListener {
            activity?.let { activity ->
                companyInfo?.let { companyInfo ->
                    val intent = Intent(activity, CompanyOnMapActivity::class.java).apply {
                        putExtra(
                            CompanyOnMapActivity.PARAM_COMPANY_PLACE,
                            deliveryComponents.getObectMapper()
                                .writeValueAsString(companyInfo.places)
                        )
                    }
                    activity.startActivity(intent)
                }
            }
        }

        llPhone.setOnClickListener {
            startPhoneDialActivity()
        }

        llEmail.setOnClickListener {
            startEmailClientActivity()
        }

        observeCompanyInfo()
        presenter.loadCompanyInfo()
    }

    override fun showCompanyInfo(companyInfo: ParamRespCompanyInfo) {
        llContent.visibility = View.VISIBLE
        this.companyInfo = companyInfo
    }

    override fun showError(errorCode: Int) {
        Snackbar.make(llEmail, getString(errorCode), Snackbar.LENGTH_LONG).show()
    }

    private fun observeCompanyInfo() {
        compositeDisposable.add(
            companyInfoObservable.subscribe { companyInfo ->
                if (companyInfo.places.isEmpty()) {
                    rlPlacesOnMap.visibility = View.GONE
                } else {
                    rlPlacesOnMap.visibility = View.VISIBLE
                }

                with(companyInfo.phone) {
                    if (this.isEmpty()) {
                        llPhone.visibility = View.GONE
                    } else {
                        llPhone.visibility = View.VISIBLE
                        tvPhone.text = companyInfo.phone
                    }
                }

                with(companyInfo.email) {
                    if (this.isEmpty()) {
                        llEmail.visibility = View.GONE
                    } else {
                        llEmail.visibility = View.VISIBLE
                        tvEmail.text = companyInfo.email
                    }
                }
            }
        )
    }

    private fun startPhoneDialActivity() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:" + tvPhone.text.toString())
        }
        startResolvedActivity(intent)
    }

    private fun startEmailClientActivity() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${tvEmail.text}"))
        startResolvedActivity(intent)
    }

    private fun startResolvedActivity(intent: Intent) {
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
