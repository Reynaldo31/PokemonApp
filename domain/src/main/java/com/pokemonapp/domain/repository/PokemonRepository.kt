package com.pokemonapp.domain.repository

import com.pokemonapp.domain.model.Pokemon
import com.pokemonapp.domain.model.PokemonListItem

interface PokemonRepository {
    suspend fun getFirstGenerationPokemons(): List<PokemonListItem>
    suspend fun getPokemonDetails(id: Int): Pokemon
    suspend fun searchPokemon(query: String): List<PokemonListItem>
    suspend fun clearCache()
}