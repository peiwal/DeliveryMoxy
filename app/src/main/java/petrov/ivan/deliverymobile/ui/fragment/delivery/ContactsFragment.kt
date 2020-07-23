package petrov.ivan.deliverymobile.ui.fragment.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.presenter.delivery.ContactsPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.ContactsView
import petrov.ivan.deliverymobile.ui.base.BaseFragment

class ContactsFragment : BaseFragment(), ContactsView {
    @InjectPresenter
    lateinit var presenter: ContactsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo create rest api to get info
    }
}
