package me.safarov399.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.safarov399.data.preferences.EncryptedSharedPreferencesManager
import me.safarov399.data.preferences.SortingPreferenceRepositoryImpl
import me.safarov399.data.repo.FileFolderOperationRepository
import me.safarov399.domain.repo.AbstractSortingPreferenceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(
        preferencesManager: EncryptedSharedPreferencesManager
    ): AbstractSortingPreferenceRepository {
        return SortingPreferenceRepositoryImpl(preferencesManager)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext ctx: Context
    ): EncryptedSharedPreferencesManager {
        return EncryptedSharedPreferencesManager(ctx)
    }

    @Provides
    @Singleton
    fun provideFileFolderOperationsRepository(): FileFolderOperationRepository = FileFolderOperationRepository()
}