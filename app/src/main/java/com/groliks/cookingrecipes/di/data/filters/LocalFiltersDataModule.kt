package com.groliks.cookingrecipes.di.data.filters

import android.content.Context
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.database.RecipesDatabase
import com.groliks.cookingrecipes.data.filters.localdata.LocalFiltersDataSource
import com.groliks.cookingrecipes.data.filters.localdata.LocalFiltersDataSourceImpl
import com.groliks.cookingrecipes.data.filters.localdata.database.FiltersDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [LocalFilterDataProvidesModule::class, LocalFiltersDataBindsModule::class])
interface LocalFiltersDataModule

@Module
interface LocalFiltersDataBindsModule {
    @Binds
    fun bindLocalFiltersDataSourceImpl_to_LocalFiltersDataSource(
        localFiltersDataSourceImpl: LocalFiltersDataSourceImpl
    ): LocalFiltersDataSource
}

@Module
class LocalFilterDataProvidesModule {
    @Provides
    fun provideFiltersDao(recipesDatabase: RecipesDatabase): FiltersDao {
        return recipesDatabase.filtersDao()
    }

    @Provides
    @Named("favourite_filter_name")
    fun provideFavouriteFilterName(context: Context): String {
        return context.resources.getString(R.string.favourites)
    }
}