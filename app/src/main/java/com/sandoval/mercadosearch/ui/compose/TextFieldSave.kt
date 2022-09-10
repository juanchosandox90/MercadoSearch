package com.sandoval.mercadosearch.ui.compose

import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.text.input.TextFieldValue

val textFieldSaver = run {
    val valueKey = "value"
    mapSaver(
        save = { mapOf(valueKey to it.text) },
        restore = {
            TextFieldValue(it[valueKey].run {
                if (this is String) this else ""
            })
        }
    )
}