package com.phoebus.demo.phastpay.ui.components.popup

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.phoebus.demo.phastpay.R
object AppToast {

    fun show(
        context: Context,
        message: String,
        duration: Duration = Duration.SHORT
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_dark, null)

        view.findViewById<TextView>(R.id.toast_text).text = message

        val toastDuration = when (duration) {
            Duration.LONG -> Toast.LENGTH_LONG
            else -> Toast.LENGTH_SHORT
        }

        Toast(context.applicationContext).apply {
            this.view = view
            this.duration = toastDuration
        }.show()
    }

    enum class Duration {
        SHORT,
        LONG
    }
}