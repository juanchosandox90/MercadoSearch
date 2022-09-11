package com.sandoval.mercadosearch.ui.viewmodel.mapper

import android.content.Context
import com.sandoval.mercadosearch.domain.base.Mapper
import com.sandoval.mercadosearch.domain.models.products.DProductDataModel
import com.sandoval.mercadosearch.ui.utils.formatAsCurrency
import com.sandoval.mercadosearch.ui.viewmodel.models.ProductDataUIModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DProductDataModelToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<List<DProductDataModel>, List<ProductDataUIModel>> {
    override fun map(input: List<DProductDataModel>): List<ProductDataUIModel> =
        input.map { product ->
            ProductDataUIModel(
                id = product.id,
                name = product.title,
                price = product.price.formatAsCurrency(),
                picture = product.thumbnail,
                freeShipping = product.freeShipping,
                condition = when (product.condition) {
                    DProductDataModel.ProductConditionEntity.NEW -> "Nuevo"
                    DProductDataModel.ProductConditionEntity.USED -> "Usado"
                    DProductDataModel.ProductConditionEntity.UNKNOWN -> ""
                },
                address = product.address.run {
                    when {
                        this.city.isNotBlank() && this.state.isNotBlank() -> "$state , $city"
                        this.state.isNotBlank() -> state
                        this.city.isNotBlank() -> city
                        else -> "No hay informacion asociada a la direccion"

                    }
                },
                shareableDescription = "Mira este maravilloso producto!"
                    .plus("\n").plus(product.title).plus("\n").plus(product.permalink),
                storeLink = product.permalink
            )
        }
}