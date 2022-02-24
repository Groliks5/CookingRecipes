package com.groliks.cookingrecipes.di.data.filters

import com.groliks.cookingrecipes.data.filters.remotedata.RemoteFiltersDataSource
import com.groliks.cookingrecipes.data.filters.remotedata.RemoteFiltersDataSourceImpl
import com.groliks.cookingrecipes.data.filters.remotedata.retrofit.FiltersApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RemoteFiltersDataBindsModule::class, RemoteFiltersDataProvidesModule::class])
class RemoteFiltersDataModule

@Module
interface RemoteFiltersDataBindsModule {
    @Binds
    fun bindRemoteFiltersDataSourceImpl_to_RemoteFiltersDataSource(
        remoteDataSourceImpl: RemoteFiltersDataSourceImpl
    ): RemoteFiltersDataSource
}

@Module
class RemoteFiltersDataProvidesModule {
    @Provides
    fun provideFiltersApiService(retrofit: Retrofit): FiltersApiService {
        return retrofit.create(FiltersApiService::class.java)
    }
}