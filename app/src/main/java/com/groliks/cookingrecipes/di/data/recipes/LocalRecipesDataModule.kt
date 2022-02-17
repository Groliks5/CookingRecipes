package com.groliks.cookingrecipes.di.data.recipes

import android.content.Context
import android.os.Environment
import com.groliks.cookingrecipes.data.database.RecipesDatabase
import com.groliks.cookingrecipes.data.recipes.localdata.LocalRecipesDataSource
import com.groliks.cookingrecipes.data.recipes.localdata.LocalRecipesDataSourceImpl
import com.groliks.cookingrecipes.data.recipes.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.recipes.localdata.photosaver.PhotoSaver
import com.groliks.cookingrecipes.data.recipes.localdata.photosaver.PhotoSaverImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.io.File

@Module(includes = [LocalRecipesDataProvideModule::class, LocalRecipesDataBindsModule::class])
class LocalRecipesDataModule

@Module
class LocalRecipesDataProvideModule {
    @Provides
    fun provideRecipesDao(recipesDatabase: RecipesDatabase): RecipesDao {
        return recipesDatabase.recipesDao()
    }

    @Provides
    fun providePhotoDirectory(context: Context): File? {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }
}

@Module
interface LocalRecipesDataBindsModule {
    @Binds
    fun bindLocalDataSourceImpl_to_LocalDataSource(
        localDataSourceImpl: LocalRecipesDataSourceImpl
    ): LocalRecipesDataSource

    @Binds
    fun bindPhotoSaverImpl_to_PhotoSaver(photoSaverImpl: PhotoSaverImpl): PhotoSaver
}