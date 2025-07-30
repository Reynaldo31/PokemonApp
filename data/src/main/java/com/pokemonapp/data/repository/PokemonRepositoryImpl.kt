package com.pokemonapp.data.repository

import com.pokemonapp.core.constants.Constants.BASE_POKEMON_IMAGE_URL
import com.pokemonapp.data.api.PokeApiService
import com.pokemonapp.domain.model.Pokemon
import com.pokemonapp.domain.model.PokemonListItem
import com.pokemonapp.domain.model.Stat
import com.pokemonapp.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PokemonRepositoryImpl @Inject constructor(private val api: PokeApiService) :
    PokemonRepository {
    private var cachedPokemons: List<PokemonListItem>? = null

    override suspend fun clearCache() {
        cachedPokemons = null
    }

    override suspend fun getFirstGenerationPokemons(): List<PokemonListItem> {
        return cachedPokemons ?: run {
            val response = api.getPokemonList(limit = 151)
            val semaphore = kotlinx.coroutines.sync.Semaphore(permits = 10)

            val pokemons = coroutineScope {
                response.results.mapIndexed { index, result ->
                    async {
                        val id = index + 1

                        semaphore.acquire()
                        val details = try {
                            api.getPokemonDetails(id)
                        } catch (e: Exception) {
                            null
                        } finally {
                            semaphore.release()
                        }

                        PokemonListItem(
                            id = id,
                            name = result.name.replaceFirstChar { it.uppercase() },
                            imageUrl = details?.sprites?.other?.home?.frontDefault
                                ?: "$BASE_POKEMON_IMAGE_URL/$id.png",
                            types = details?.types?.map { it.type.name } ?: emptyList()
                        )
                    }
                }.awaitAll()
            }

            cachedPokemons = pokemons
            pokemons
        }
    }

    override suspend fun getPokemonDetails(id: Int): Pokemon {
        val response = api.getPokemonDetails(id)

        val speciesResponse = api.getPokemonSpecies(id)

        val englishDescription = speciesResponse.flavor_text_entries
            .firstOrNull { it.language.name == "en" }
            ?.flavor_text
            ?.replace("\n", " ")
            ?: "No description available"

        return Pokemon(
            id = response.id,
            name = response.name.replaceFirstChar { it.uppercase() },
            imageUrl = response.sprites.other?.home?.frontDefault ?: "",
            types = response.types.map { it.type.name },
            stats = response.stats.map { Stat(it.stat.name, it.baseStat) },
            height = response.height,
            weight = response.weight,
            abilities = response.abilities.map { it.ability.name },
            description = englishDescription
        )
    }

    override suspend fun searchPokemon(query: String): List<PokemonListItem> {
        return try {
            val allPokemons = getFirstGenerationPokemons()
            if (query.isBlank()) {
                allPokemons
            } else {
                allPokemons.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            try {
                                query.replace("#", "").trim().toInt() == it.id
                            } catch (e: NumberFormatException) {
                                false
                            }
                }
            }
        } catch (e: Exception) {
            if (e.isCancellation()) throw e // Relanzar las cancelaciones
            emptyList()
        }
    }

    private fun Exception.isCancellation() = this is CancellationException

}