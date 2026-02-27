package hr.algebra.footballzone.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun View.applyAnimation(id: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, id))

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit {
            putBoolean(key, value)
        }
}

fun Context.getBooleanPreference(key: String): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        getSystemService<ConnectivityManager>() // compare with ours -> reified T: Any - returns null!
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}


fun Fragment.finish() = requireActivity().finish()
fun Fragment.context() = requireContext()

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Fragment.setBooleanPreference(key: String, value: Boolean = true) {
    requireContext().setBooleanPreference(key, value)
}

fun Fragment.getBooleanPreference(key: String): Boolean =
    requireContext().getBooleanPreference(key)

fun Fragment.isOnline(): Boolean =
    requireContext().isOnline()

fun Fragment.callDelayed(delay: Long, work: () -> Unit) {
    view?.postDelayed({
        if (isAdded && view != null) { // safety check
            work()
        }
    }, delay)
}

fun Fragment.navigateTo(actionId: Int, args: Bundle? = null) {
    findNavController().navigate(actionId, args)
}

fun Fragment.navigate(destinationId: Int, args: Bundle? = null) {
    findNavController().navigate(destinationId, args)
}

inline fun <reified T : Activity> Context.startActivity() = startActivity(
    Intent(
        this,
        T::class.java
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))
