package com.mercadolibre.android.point_mainapp_demo.app.actions

import android.graphics.drawable.Drawable

data class ActionModel(
    val title: String,
    val icon: Drawable?,
    val action: Map<ActionTypes, Any>,
)
