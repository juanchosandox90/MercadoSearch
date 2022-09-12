package com.sandoval.mercadosearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sandoval.mercadosearch.common.DEFAULT_UPDATE_DELAY
import com.sandoval.mercadosearch.common.PAGINATION_SEARCH_DELAY
import com.sandoval.mercadosearch.domain.models.SearchProductsParams
import com.sandoval.mercadosearch.domain.usecase.SearchProductsByNameUseCase
import com.sandoval.mercadosearch.ui.viewmodel.base.BaseViewModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.usecase.GetProductDetailsUseCase
import com.sandoval.mercadosearch.ui.base.ErrorDProductModelToUIModel
import com.sandoval.mercadosearch.ui.pagination.PagingUISource
import com.sandoval.mercadosearch.ui.viewmodel.mapper.DProductDataModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.mapper.DProductDetailDataModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Aquí se encuentran todas las lógicas para buscar y obtener información sobre los productos. Resultados de una búsqueda
 * por una frase están paginados, consulte [PagingUISource]. Todos los flujos de datos se presentan como [LiveData] en orden
 * para proporcionar soporte para pruebas unitarias ya que [State] no proporciona una forma adecuada de observar las actualizaciones fuera de un
 * alcance componible.
 *
 * @param delayStatus Retrasos habilitados/deshabilitados para enviar actualizaciones de Livedata. Se aplican retrasos
 * aquí para proporcionar una mejor experiencia UX al usuario, mientras algunos cálculos se completan.
 * Realmente esto puede evitar que la interfaz de usuario proporcione una animación o un estado de carga adecuados.
 *
 * Dado que TestScope.kt salta el retraso funciones, este indicador se establece en verdadero al instanciar [ProductsViewModel] desde una prueba unitaria, esto parece
 * como una solución decente por ahora. (Es difícil inyectar un booleano usando Hilt)
 * Ver: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-scope/index.html#-33808464%2FExtensions%2F-1989735873
 *
 * @autor Juan Camilo Sandoval
 *
 * */

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val searchProductsByNameUseCase: SearchProductsByNameUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val productDetailsMapper: DProductDetailDataModelToUIModel,
    private val productModelMapper: DProductDataModelToUIModel,
    private val errorMapper: ErrorDProductModelToUIModel,
    private val delayStatus: DelayStatus = DelayStatus()
) : BaseViewModel() {

    private var _searchState = MutableLiveData<ProductSearchState>()
    val searchState: LiveData<ProductSearchState> = _searchState

    private var _detailsState = MutableLiveData<ProductDetailState>()
    val detailsState: LiveData<ProductDetailState> = _detailsState

    private val pagingSource by lazy { PagingUISource<ProductDataUIModel>(limit = 20) }

    fun initialSearch(text: String) {
        if (text.isNotBlank()) {
            runJobAndCancelPrevious(jobName = "initialSearch", job =
            viewModelScope.launch(Dispatchers.IO) {
                _searchState.postValue(ProductSearchState.Loading)
                when (val result = searchProductsByNameUseCase.perform(
                    SearchProductsParams(
                        name = text,
                        offset = pagingSource.offset(),
                        limit = pagingSource.limit()
                    )
                )) {
                    is Result.Success -> {
                        if (result.data.isNotEmpty()) {
                            pagingSource.init(
                                pages = result.data.pages,
                                total = result.data.total
                            )
                            pagingSource.append(productModelMapper.map(result.data.items))
                            runDelayed {
                                _searchState.postValue(
                                    ProductSearchState.Results(pagingSource.data())
                                )
                            }
                        } else {
                            runDelayed {
                                _searchState.postValue(ProductSearchState.NoResults)
                            }
                        }
                    }
                    is Result.Failure -> runDelayed {
                        _searchState.postValue(
                            ProductSearchState.Failure(error = errorMapper.map(result.error))
                        )
                    }
                }
            })
        } else {
            _searchState.value = ProductSearchState.EmptySearchedText
        }
    }

    fun fetchMoreResults(text: String) {
        runJobAndCancelPrevious(jobName = "fetchMoreResults",
            job = viewModelScope.launch(Dispatchers.IO) {
                if (delayStatus.enabled) {
                    delay(PAGINATION_SEARCH_DELAY)
                }
                if (pagingSource.canFetch()) {
                    _searchState.postValue(
                        ProductSearchState.Results(pagingSource.data(), isRefreshing = true)
                    )
                    when (val result = searchProductsByNameUseCase.perform(
                        SearchProductsParams(
                            name = text,
                            offset = pagingSource.offset(),
                            limit = pagingSource.limit()
                        )
                    )) {
                        is Result.Success -> {
                            if (result.data.isNotEmpty()) {
                                pagingSource.append(productModelMapper.map(result.data.items))
                            }
                            runDelayed {
                                _searchState.postValue(ProductSearchState.Results(pagingSource.data()))
                            }
                        }
                        is Result.Failure -> runDelayed {
                            _searchState.postValue(
                                ProductSearchState.Results(
                                    pagingSource.data(),
                                    refreshingError = errorMapper.map(result.error)
                                )
                            )
                        }
                    }
                }
            }
        )
    }

    fun getProductDetails(product: ProductDataUIModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailsState.postValue(ProductDetailState.Loading(product))
            when (val result = getProductDetailsUseCase.perform(product.id)) {
                is Result.Success -> runDelayed {
                    _detailsState.postValue(
                        ProductDetailState.DetailsReady(
                            product = product,
                            details = productDetailsMapper.map(result.data)
                        )
                    )
                }
                is Result.Failure -> runDelayed {
                    _detailsState.postValue(
                        ProductDetailState.Failure(
                            product = product,
                            error = errorMapper.map(result.error)
                        )
                    )
                }
            }
        }
    }

    private suspend fun runDelayed(
        delay: Long = DEFAULT_UPDATE_DELAY,
        function: suspend () -> Unit
    ) {
        if (delayStatus.enabled) {
            delay(delay)
        }
        function()
    }
}