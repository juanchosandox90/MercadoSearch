package com.sandoval.mercadosearch.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sandoval.mercadosearch.domain.base.PaginatedDProductDataModel
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.errorEntity
import com.sandoval.mercadosearch.domain.models.SearchProductsParams
import com.sandoval.mercadosearch.domain.productDetailsEntity
import com.sandoval.mercadosearch.domain.repository.ProductsRepository
import com.sandoval.mercadosearch.domain.usecase.GetProductDetailsUseCase
import com.sandoval.mercadosearch.domain.usecase.SearchProductsByNameUseCase
import com.sandoval.mercadosearch.ui.base.ErrorDProductModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.base.BaseViewModel
import com.sandoval.mercadosearch.ui.viewmodel.mapper.DProductDataModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.mapper.DProductDetailDataModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var mockedProductsRepository: ProductsRepository

    @MockK
    lateinit var productDetailsMapper: DProductDetailDataModelToUIModel

    @MockK
    lateinit var mockedProductMapper: DProductDataModelToUIModel

    @MockK
    lateinit var mockedErrorMapper: ErrorDProductModelToUIModel

    @MockK
    lateinit var mockSearchStateObserver: Observer<ProductSearchState>

    @MockK
    lateinit var mockProductDetailsObserver: Observer<ProductDetailState>

    private lateinit var viewModel: ProductsViewModel

    @Before
    fun setupDependencies() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = ProductsViewModel(
            SearchProductsByNameUseCase(mockedProductsRepository),
            GetProductDetailsUseCase(mockedProductsRepository),
            productDetailsMapper,
            mockedProductMapper,
            mockedErrorMapper,
            delayStatus = BaseViewModel.DelayStatus().apply { enabled = false }
        )
        every { mockedErrorMapper.map(errorEntity) } returns errorUIModel
    }

    @Test
    fun emptyInitialSearchText() {

        viewModel.initialSearch(text = "")

        Assert.assertTrue(viewModel.searchState.value == ProductSearchState.EmptySearchedText)

    }

    @Test
    fun emptyInitialSearch() = runTest(context = UnconfinedTestDispatcher()) {

        val searchedText = "PS5 console"

        coEvery {
            mockedProductsRepository.searchByName(
                SearchProductsParams(
                    name = searchedText,
                    offset = 0,
                    limit = 20
                )
            )
        } returns Result.Success(
            PaginatedDProductDataModel(
                total = 0,
                pages = 0,
                items = emptyList()
            )
        )

        val state = viewModel.searchState
        state.observeForever(mockSearchStateObserver)

        viewModel.initialSearch(text = searchedText)

        coVerifySequence {
            mockSearchStateObserver.onChanged(ProductSearchState.Loading)
            mockSearchStateObserver.onChanged(ProductSearchState.NoResults)
        }

    }

    @Test
    fun failedInitialSearch() = runTest {

        val searchedText = "PS5 console"

        coEvery {
            mockedProductsRepository.searchByName(
                SearchProductsParams(
                    name = searchedText,
                    offset = 0,
                    limit = 20
                )
            )
        } returns Result.Failure(errorEntity)

        viewModel.searchState.observeForever(mockSearchStateObserver)

        viewModel.initialSearch(text = searchedText)

        coVerifySequence {
            mockSearchStateObserver.onChanged(ProductSearchState.Loading)
            mockSearchStateObserver.onChanged(ProductSearchState.Failure(errorUIModel))
        }

    }

    @Test
    fun successfulDetailsFetch() {
        val productId = "ID20"
        coEvery {
            mockedProductsRepository.getDetails(productId)
        } returns Result.Success(productDetailsEntity)

        every { productDetailsMapper.map(productDetailsEntity) } returns productDetailsModel

        viewModel.detailsState.observeForever(mockProductDetailsObserver)

        viewModel.getProductDetails(productModel)

        coVerifySequence {
            mockProductDetailsObserver.onChanged(ProductDetailState.Loading(productModel))
            mockProductDetailsObserver.onChanged(ProductDetailState.DetailsReady(productModel, productDetailsModel)
            )
        }
    }

    @Test
    fun unsuccessfulDetailsFetch() {
        val productId = "ID20"
        coEvery {
            mockedProductsRepository.getDetails(productId)
        } returns Result.Failure(errorEntity)

        viewModel.detailsState.observeForever(mockProductDetailsObserver)

        viewModel.getProductDetails(productModel)

        coVerifySequence {
            mockProductDetailsObserver.onChanged(ProductDetailState.Loading(productModel))
            mockProductDetailsObserver.onChanged(ProductDetailState.Failure(productModel, errorUIModel)
            )
        }
    }

}