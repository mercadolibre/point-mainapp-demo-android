package com.mercadolibre.android.point_mainapp_demo.app.actions

import android.content.Context

interface ActionsProvicer {

    fun getActions(context: Context): List<ActionModel>
}