package com.miempresa.jsonplaceapp.di

import com.miempresa.jsonplaceapp.data.repository.PostRepositoryImpl
import com.miempresa.jsonplaceapp.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Hilt sabrá que cuando alguien pida PostRepository, debe proveer PostRepositoryImpl
    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository
}