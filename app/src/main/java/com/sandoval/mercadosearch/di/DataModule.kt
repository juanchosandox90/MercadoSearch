package com.sandoval.mercadosearch.di

import android.content.Context
import com.sandoval.mercadosearch.BuildConfig
import com.sandoval.mercadosearch.data.DefaultProductsRepository
import com.sandoval.mercadosearch.data.ProductsRepository
import com.sandoval.mercadosearch.data.datasource.remote.DefaultProductsRemoteDataSource
import com.sandoval.mercadosearch.data.datasource.remote.RemoteProductsDataSource
import com.sandoval.mercadosearch.data.datasource.remote.api.ProductsApiService
import com.sandoval.mercadosearch.data.networking.DEFAULT_TIMEOUT_IN_SECONDS
import com.sandoval.mercadosearch.data.networking.NetworkConnectionChecker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    companion object {

        @Provides
        fun provideProductsApiService(): ProductsApiService =
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        })
                        .build()
                )
                .baseUrl(BuildConfig.API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductsApiService::class.java)

        @Provides
        fun providesNetworkConnectionChecker(@ApplicationContext context: Context): NetworkConnectionChecker =
            NetworkConnectionChecker(context)

    }

    @Binds
    abstract fun bindRemoteProductsDataSource(dataSource: DefaultProductsRemoteDataSource): RemoteProductsDataSource

    @Binds
    abstract fun bindRemoteProductsRepository(repository: DefaultProductsRepository): ProductsRepository
}