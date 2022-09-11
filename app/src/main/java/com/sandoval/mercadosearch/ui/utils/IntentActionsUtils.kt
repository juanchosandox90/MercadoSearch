package com.sandoval.mercadosearch.ui.utils

import android.app.Activity
import android.content.Intent

/**
 * Esta extension ayuda a crear un intent implicito para compartir texto con otras apps.
 * Pide al dispotivo un dialogo de opciones.
 *
 * @param title Titulo para el modal de opciones
 * @param contentToShare Texto para compartir
 *
 * */

fun Activity.shareText(title: String, contentToShare: String) {
    startActivity(Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, contentToShare)
        type = "text/plain"
    }, title))
}