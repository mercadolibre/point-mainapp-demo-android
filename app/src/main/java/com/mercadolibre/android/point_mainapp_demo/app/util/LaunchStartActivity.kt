package com.mercadolibre.android.point_mainapp_demo.app.util

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun Context.launchActivity(destination: Class<*>, bundle: Bundle? = null) {
    Intent(this, destination).run {
        bundle?.let {
            putExtras(bundle)
        }
        startActivity(this)
    }
}
