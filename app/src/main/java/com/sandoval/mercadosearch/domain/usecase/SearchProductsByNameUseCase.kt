package com.sandoval.mercadosearch.domain.usecase

import com.sandoval.mercadosearch.domain.base.PaginatedDataEntity
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.base.UseCase
import com.sandoval.mercadosearch.domain.models.DProductDataModel
import com.sandoval.mercadosearch.domain.models.SearchProductsParams
import com.sandoval.mercadosearch.domain.repository.ProductsRepository
import javax.inject.Inject

class SearchProductsByNameUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    UseCase<SearchProductsParams, PaginatedDataEntity<DProductDataModel>> {

    override suspend fun perform(params: SearchProductsParams): Result<PaginatedDataEntity<DProductDataModel>> =
        productsRepository.searchByName(params)
}