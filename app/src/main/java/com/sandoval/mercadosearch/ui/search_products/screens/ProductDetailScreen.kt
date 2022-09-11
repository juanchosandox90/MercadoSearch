package com.sandoval.mercadosearch.ui.search_products.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.sandoval.mercadosearch.R
import com.sandoval.mercadosearch.ui.compose.BackButton
import com.sandoval.mercadosearch.ui.compose.ErrorSnackbarHost
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productDetailActions
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productDetailsLoading
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productDetailsReady
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productFailureReady
import com.sandoval.mercadosearch.ui.theme.MercadoSearchBrightGray
import com.sandoval.mercadosearch.ui.theme.MercadoSearchGreenHaze
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState

@Composable
fun ProductDetailScreen(
    state: ProductDetailState?,
    actions: ProductDetailsActions
) {
    val localConfiguration = LocalConfiguration.current
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    when (state) {
                        is ProductDetailState.DetailsReady -> {
                            TopBarDetailScreenSection(state.product, actions)
                        }
                        else -> {}
                    }
                },
                navigationIcon = {
                    BackButton(actions.doWhenBackButtonClicked)
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.Black,
                elevation = 10.dp
            )
        },
        snackbarHost = { snackbarHostState -> ErrorSnackbarHost(snackbarHostState = snackbarHostState) }
    ) { paddingValues ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                if (state != null) {
                    ConditionAndSoldQuantitySection(state)
                    NamePictureAndPriceSection(
                        state = state,
                        orientation = localConfiguration.orientation,
                        maxHeight = this@BoxWithConstraints.maxHeight
                    )
                    ProductInfoAndQuantitiesSection(state)
                }
            }
        }
    }
}

@Composable
private fun TopBarDetailScreenSection(product: ProductDataUIModel, actions: ProductDetailsActions) {
    IconButton(
        onClick = {
            actions.doWhenSharedButtonClicked(product.shareableDescription)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Compartir",
            tint = Color.Black
        )
    }
}

@Composable
fun ConditionAndSoldQuantitySection(state: ProductDetailState) {
    val showShimmer = state !is ProductDetailState.DetailsReady
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .height(IntrinsicSize.Min)
            .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer())
    ) {
        Text(
            text = state.product.condition,
            color = Color.Gray,
            style = Typography.caption
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = if (state is ProductDetailState.DetailsReady) {
                state.details.soldQuantity
            } else {
                "Placeholder"
            },
            color = Color.Gray,
            style = Typography.caption
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NamePictureAndPriceSection(
    state: ProductDetailState, orientation: Int, maxHeight: Dp
) {
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
        text = state.product.name,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = Typography.subtitle1
    )

    val pictureSectionSize =
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) maxHeight else maxHeight * 0.4f

    if (state is ProductDetailState.DetailsReady) {
        HorizontalPager(
            count = state.details.pictures.count(),
            modifier = Modifier
                .padding(top = 16.dp)
                .wrapContentSize()
                .fillMaxWidth()
        ) { page ->
            val picture = state.details.pictures[page]
            Box(
                modifier = Modifier
                    .height(pictureSectionSize)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .placeholder(R.drawable.ic_picture_placeholder_512)
                        .error(R.drawable.ic_picture_placeholder_512)
                        .data(picture.url)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "Foto del producto"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .background(
                            MercadoSearchBrightGray,
                            RoundedCornerShape(percent = 50)
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    text = picture.index,
                    style = Typography.caption
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .height(pictureSectionSize)
                .fillMaxWidth()
                .padding(top = 16.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                )
        )
    }

    Text(
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp
        ),
        text = state.product.price,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = Typography.h5
    )
}

@Composable
fun ProductInfoAndQuantitiesSection(state: ProductDetailState) {
    val showShimmer = state !is ProductDetailState.DetailsReady
    Text(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
        text = state.product.address,
        color = Color.Gray,
        maxLines = 2,
        style = Typography.subtitle1,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
            .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
        text = if (state is ProductDetailState.DetailsReady) {
            state.details.availableQuantity
        } else {
            "Placeholder"
        },
        color = if (state is ProductDetailState.DetailsReady) {
            state.details.availableQuantityColor
        } else {
            Color.Gray
        },
        style = Typography.body1
    )

    Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
            painter = painterResource(id = R.drawable.ic_shipping_icon),
            contentDescription = "Envio Gratis",
            tint = MercadoSearchGreenHaze
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
            text = "Envio Gratis",
            color = MercadoSearchGreenHaze,
            style = Typography.body2
        )
    }
    Row(modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
            painter = painterResource(id = R.drawable.ic_security_icon),
            contentDescription = "Asegurado",
            tint = Color.Gray
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .placeholder(visible = showShimmer, highlight = PlaceholderHighlight.shimmer()),
            text = if (state is ProductDetailState.DetailsReady) {
                state.details.warranty
            } else {
                "Placeholder"
            },
            color = Color.Gray,
            style = Typography.body2
        )
    }
}

data class ProductDetailsActions(
    val doWhenSharedButtonClicked: (String) -> Unit,
    val doWhenBackButtonClicked: () -> Unit
)

@Preview(name = "LoadingState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun ProductsDetailsScreenPreview() {
    BuildProductDetailsPreview(productDetailsLoading)
}

@Preview(name = "ProductDetailsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun ProductsDetailsScreenProductDetailsReadyStatePreview() {
    BuildProductDetailsPreview(productDetailsReady)
}

@Preview(name = "FailureState")
@Composable
fun ProductsDetailsScreenFailureStatePreview() {
    BuildProductDetailsPreview(productFailureReady)
}

@Composable
fun BuildProductDetailsPreview(state: ProductDetailState) {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ProductDetailScreen(
                state = state,
                productDetailActions
            )
        }
    }
}