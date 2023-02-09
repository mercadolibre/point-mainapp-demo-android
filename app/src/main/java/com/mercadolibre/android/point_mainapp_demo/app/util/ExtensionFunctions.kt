package com.mercadolibre.android.point_mainapp_demo.app.util

import android.view.View

internal fun View.gone() {
    this.visibility = View.GONE
}

internal fun View.visible() {
    this.visibility = View.VISIBLE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}
