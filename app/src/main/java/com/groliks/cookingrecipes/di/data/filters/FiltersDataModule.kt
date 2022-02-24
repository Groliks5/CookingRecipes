package com.groliks.cookingrecipes.di.data.filters

import com.groliks.cookingrecipes.data.filters.repository.FiltersRepository
import com.groliks.cookingrecipes.data.filters.repository.FiltersRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [LocalFiltersDataModule::class, RemoteFiltersDataModule::class])
interface FiltersDataModule {
    @Binds
    fun bindFiltersRepositoryImpl_to_FiltersRepository(
        filtersRepositoryImpl: FiltersRepositoryImpl
    ): FiltersRepository
}