package com.pokemonapp.feature_pokemon.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemonapp.domain.model.PokemonListItem
import com.pokemonapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    // Estados para la UI
    private val _state = MutableStateFlow<PokemonListState>(PokemonListState.Loading)
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    // Estado para la búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showSearchResults = MutableStateFlow(false)
    val showSearchResults: StateFlow<Boolean> = _showSearchResults.asStateFlow()

    // Job para controlar la búsqueda con debounce
    private var searchJob: Job? = null

    // Cache para los Pokémon de primera generación
    private var firstGenPokemons: List<PokemonListItem> = emptyList()

    private var lastQuery: String = ""

    init {
        loadFirstGenerationPokemons()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        _showSearchResults.value = newQuery.isNotEmpty()

        if (newQuery == lastQuery) return

        lastQuery = newQuery
        performSearch()
    }

    private fun loadFirstGenerationPokemons() {
        viewModelScope.launch {
            _state.value = PokemonListState.Loading
            try {
                firstGenPokemons = repository.getFirstGenerationPokemons()
                _state.value = PokemonListState.Success(firstGenPokemons)
            } catch (e: Exception) {
                if (!e.isCancellation()) {
                    _state.value = PokemonListState.Error(
                        message = "Failed to load Pokémon: ${e.message ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    private fun performSearch() {
        searchJob?.cancel()

        val currentQuery = _searchQuery.value

        if (currentQuery.isEmpty()) {
            _state.value = PokemonListState.Success(firstGenPokemons)
            return
        }


        searchJob = viewModelScope.launch {

            try {
                delay(500)

                val results = if (firstGenPokemons.isNotEmpty()) {
                    searchInCache(currentQuery)
                } else {
                    repository.searchPokemon(currentQuery)
                }

                if (results.isEmpty()) {
                    _state.value = PokemonListState.Error("No Pokémon found for '$currentQuery'")
                } else {
                    _state.value = PokemonListState.Success(results)
                }
            } catch (e: Exception) {
                if (!e.isCancellation()) {
                    _state.value = PokemonListState.Error(
                        message = "Search failed: ${e.message ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    private suspend fun searchInCache(query: String): List<PokemonListItem> {
        return firstGenPokemons.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.id.toString().startsWith(query)
        }
    }
}

private fun Exception.isCancellation() = this is CancellationException

sealed class PokemonListState {
    object Loading : PokemonListState()
    data class Success(val pokemons: List<PokemonListItem>) : PokemonListState()
    data class Error(val message: String) : PokemonListState()
}