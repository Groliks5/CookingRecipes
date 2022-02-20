package com.groliks.cookingrecipes.di.data.recipes

import com.groliks.cookingrecipes.data.recipes.remotedata.RemoteRecipesDataSource
import com.groliks.cookingrecipes.data.recipes.remotedata.RemoteRecipesDataSourceImpl
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RecipesApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RemoteRecipesDataBindsModule::class, RemoteRecipesDataProvidesModule::class])
class RemoteRecipesDataModule

@Module
interface RemoteRecipesDataBindsModule {
    @Binds
    fun bindRemoteRecipesDataSourceImpl_to_RemoteRecipesDataSource(
        remoteDataSourceImpl: RemoteRecipesDataSourceImpl
    ): RemoteRecipesDataSource
}

@Module
class RemoteRecipesDataProvidesModule {
    @Provides
    fun provideRecipesApiService(retrofit: Retrofit): RecipesApiService {
        return retrofit.create(RecipesApiService::class.java)
    }
}