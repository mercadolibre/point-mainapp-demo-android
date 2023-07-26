package com.mercadolibre.android.point_mainapp_demo.app.actions.contract

import android.content.Context
import com.mercadolibre.android.point_mainapp_demo.app.actions.model.ActionModel

interface ActionsProvicer {
    fun getActions(context: Context): List<ActionModel>
}