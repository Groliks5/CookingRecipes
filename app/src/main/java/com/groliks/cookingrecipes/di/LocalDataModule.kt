package com.groliks.cookingrecipes.di

import android.content.Context
import android.os.Environment
import androidx.room.Room
import com.groliks.cookingrecipes.data.localdata.LocalDataSource
import com.groliks.cookingrecipes.data.localdata.LocalDataSourceImpl
import com.groliks.cookingrecipes.data.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.localdata.database.RecipesDatabase
import com.groliks.cookingrecipes.data.localdata.photosaver.PhotoSaver
import com.groliks.cookingrecipes.data.localdata.photosaver.PhotoSaverImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.io.File

@Module(includes = [LocalDataProvideModule::class, LocalDataBindsModule::class])
class LocalDataModule

@Module
class LocalDataProvideModule {
    @Provides
    fun provideRecipesDatabase(context: Context): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_db"
        ).build()
    }

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
interface LocalDataBindsModule {
    @Binds
    fun bindLocalDataSourceImpl_to_LocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    fun bindPhotoSaverImpl_to_PhotoSaver(photoSaverImpl: PhotoSaverImpl): PhotoSaver
}