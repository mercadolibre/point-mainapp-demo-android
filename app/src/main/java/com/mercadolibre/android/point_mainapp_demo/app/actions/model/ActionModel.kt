package com.mercadolibre.android.point_mainapp_demo.app.actions.model

import android.graphics.drawable.Drawable
import com.mercadolibre.android.point_mainapp_demo.app.actions.contract.HomeActions

data class ActionModel(
    val title: String,
    val icon: Drawable?,
    val action: HomeActions,
)
