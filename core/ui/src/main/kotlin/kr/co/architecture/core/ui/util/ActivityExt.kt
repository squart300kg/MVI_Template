package kr.co.architecture.core.ui.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle

inline fun <reified T : Activity> Activity.startActivity(
    bundle: Bundle? = null
) {
    Intent(this, T::class.java).apply {
        bundle?.apply { putExtras(this) }
        startActivity(this)
    }
}