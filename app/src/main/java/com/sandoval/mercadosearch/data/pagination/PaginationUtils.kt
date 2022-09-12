package com.sandoval.mercadosearch.data.pagination

import kotlin.math.ceil

object PaginationUtils {

    /**
     * Calcula el numero de paginas de un set de datos basado en el [total] y en el [limit] actual
     *
     * @param total total de resultados
     * @param limit limite por pagina
     *
     * @return numero de paginas
     *
     * */

    fun calculatePages(total: Int, limit: Int): Int =
        if (limit > 0) {
            ceil(total.toDouble() / limit.toDouble()).toInt()
        } else {
            0
        }
}