package petrov.ivan.deliverymobile.service

import android.content.Intent
import android.text.TextUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import petrov.ivan.deliverymobile.components.DaggerDeliveryComponents
import petrov.ivan.deliverymobile.components.DeliveryComponents
import petrov.ivan.deliverymobile.modules.ContextModule
import petrov.ivan.deliverymobile.ui.fragment.delivery.fcmmessage.ArgsFcmMessage
import petrov.ivan.deliverymobile.ui.fragment.delivery.fcmmessage.FcmMessageActivity
import petrov.ivan.deliverymobile.ui.fragment.delivery.fcmmessage.FcmMessageActivityArgs

class AppFirebaseMessagingService: FirebaseMessagingService() {
    private val deliveryComponents: DeliveryComponents by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerDeliveryComponents.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(body)) {
            val args = ArgsFcmMessage(title, body)
            val bundle = FcmMessageActivityArgs.Builder(args).build().toBundle()

            val intent = Intent(applicationContext, FcmMessageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("args", bundle)
            }
            applicationContext.startActivity(intent)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        System.out.println("fcmToken = $token")
//        deliveryComponents.getDeliveryService().setAppData()
    }
}