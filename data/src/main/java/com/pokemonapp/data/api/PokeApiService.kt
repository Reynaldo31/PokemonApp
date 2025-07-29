package com.pokemonapp.data.api

import com.pokemonapp.data.responses.PokemonResponses
import com.pokemonapp.domain.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 151
    ): PokemonResponses.PokemonListResponse

    @GET("pokemon/{identifier}")
    suspend fun getPokemonDetails(
        @Path("identifier") identifier: Int
    ): PokemonResponses.PokemonDetailsResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesResponse
}