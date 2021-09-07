package petrov.ivan.deliverymobile.ui.fragment.delivery.fcmmessage

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fcm_message_activity.*
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.ui.base.BaseActivity
import java.io.Serializable

class FcmMessageActivity: BaseActivity() {
    var argsFcmMessage: ArgsFcmMessage? = null
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fcm_message_activity)

        intent.extras?.getBundle("args")?.let {
            argsFcmMessage = FcmMessageActivityArgs.fromBundle(it).args
        }

        disposable = Observable.merge(fabBack.clicks(), btnOk.clicks())
            .firstElement()
            .subscribe {
                finish()
            }
        showData()
    }

    private fun showData() {
        argsFcmMessage?.let {
            tvTitle.text = it.title ?: getString(R.string.activity_fcm_message_default_title)
            tvMessage.text = it.message ?: ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}

data class ArgsFcmMessage(val title: String?, val message: String?): Serializable