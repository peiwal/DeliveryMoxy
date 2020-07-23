package petrov.ivan.deliverymobile.utils

import android.content.res.Resources

class Extensions
fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()