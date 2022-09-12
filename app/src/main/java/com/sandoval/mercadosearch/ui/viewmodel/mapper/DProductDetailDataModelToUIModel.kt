package com.sandoval.mercadosearch.ui.viewmodel.mapper

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.sandoval.mercadosearch.R
import com.sandoval.mercadosearch.domain.base.Mapper
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel
import com.sandoval.mercadosearch.ui.theme.MercadoSearchDarkBlue
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductAttributeUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductDetailDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductPictureUIModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DProductDetailDataModelToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<DProductDetailsDataModel, ProductDetailDataUIModel> {
    override fun map(input: DProductDetailsDataModel): ProductDetailDataUIModel {
        val picturesCount = input.pictures.count()
        return ProductDetailDataUIModel(warranty = validateValidUserFaceValue(
            input.warranty,
            R.string.title_warranty_not_specified
        ),
            pictures = input.pictures.mapIndexed { index, picture ->
                ProductPictureUIModel(
                    index = "${index.plus(1)}/$picturesCount",
                    url = picture
                )
            }, attributes = input.attributes.map { attribute ->
                ProductAttributeUIModel(
                    name = validateValidUserFaceValue(attribute.name),
                    value = validateValidUserFaceValue(attribute.value)
                )
            }, soldQuantity = context.resources.getQuantityString(
                R.plurals.soldQuantity,
                input.soldQuantity,
                input.soldQuantity
            ), availableQuantity = context.resources.getQuantityString(
                R.plurals.availableQuantity,
                input.availableQuantity,
                input.availableQuantity
            ),
            availableQuantityColor = if (input.availableQuantity > 0) {
                MercadoSearchDarkBlue
            } else {
                Color.Red
            }
        )
    }

    private fun validateValidUserFaceValue(
        value: String,
        @StringRes defaultValue: Int = R.string.title_not_specified
    ): String =
        value.run { this.ifBlank { context.getString(defaultValue) } }

}