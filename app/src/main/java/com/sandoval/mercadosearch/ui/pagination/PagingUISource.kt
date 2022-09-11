package com.sandoval.mercadosearch.ui.pagination

/**
 * Clase que envuelve la lógica para manipular y llenar la fuente de datos paginada.
 * Cada vez que se agregan más elementos a la fuente de datos, este contenedor vuelve a calcular
 * el desplazamiento y las páginas restantes de todo el conjunto de datos.
 *
 * @param limit Limite por pagina
 * */

class PagingUISource<T>(private val limit: Int) {

    private var allData = mutableListOf<T>()
    private var currentSearchPages = 0
    private var currentPage = 0
    private var currentSearchOffset = 0
    private var totalCount = 0
    private var currentCount = 0

    fun limit() = limit
    fun offset() = currentSearchOffset
    fun data() = allData


    fun init(pages: Int, total: Int) {
        reset()
        currentSearchPages = pages
        totalCount = total
    }

    fun canFetch() = (currentCount < totalCount && currentPage < currentSearchPages)
        .also { value ->
            if (value) calculateOffset()
        }

    fun reset() {
        currentPage = 0
        currentSearchPages = 0
        currentSearchOffset = 0
        currentCount = 0
        allData.clear()
    }

    fun append(data: List<T>): Boolean {
        val dataCount = data.count()
        return if ((currentCount + dataCount) <= totalCount) {
            currentCount += dataCount
            allData.addAll(data)
            currentPage++
            true
        } else {
            false
        }
    }

    private fun calculateOffset() {
        currentSearchOffset = currentPage * limit
    }

}