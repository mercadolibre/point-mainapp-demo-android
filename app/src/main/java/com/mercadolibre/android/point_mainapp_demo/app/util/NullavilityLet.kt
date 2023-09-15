package com.mercadolibre.android.point_mainapp_demo.app.util

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit): List<T>? {
    return if (elements.all { it != null }) {
        closure(elements.filterNotNull())
        elements.filterNotNull()
    } else {
        null
    }
}
