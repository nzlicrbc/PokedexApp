package com.example.pokedexapp.di

import com.example.pokedexapp.data.remote.ApiService
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonRepository(
        apiService: ApiService
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService)
    }
}