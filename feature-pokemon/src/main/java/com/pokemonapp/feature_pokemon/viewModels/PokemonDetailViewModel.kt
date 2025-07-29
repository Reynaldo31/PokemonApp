package com.pokemonapp.feature_pokemon.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemonapp.domain.model.Pokemon
import com.pokemonapp.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    // Color de fondo basado en el tipo del Pokémon
    var backgroundColor by mutableStateOf(Color(0xFFA0A0A0))
        private set

    init {
        val pokemonId = savedStateHandle.get<Int>("pokemonId") ?: 1
        loadPokemonDetails(pokemonId)
    }

    private fun loadPokemonDetails(pokemonId: Int) {
        viewModelScope.launch {
            _state.value = PokemonDetailState.Loading
            try {
                val pokemon = repository.getPokemonDetails(pokemonId)
                backgroundColor = determineBackgroundColor(pokemon.types.firstOrNull())
                _state.value = PokemonDetailState.Success(pokemon)
            } catch (e: Exception) {
                _state.value = PokemonDetailState.Error(
                    message = "Failed to load Pokémon details: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }

    private fun determineBackgroundColor(type: String?): Color {
        return when (type?.lowercase()) {
            "fire" -> Color(0xFFFFA756)
            "water" -> Color(0xFF76BDFE)
            "grass" -> Color(0xFF49D0B0)
            "electric" -> Color(0xFFFFD76F)
            "psychic" -> Color(0xFFFF9FF3)
            "ice" -> Color(0xFF98D8D8)
            "dragon" -> Color(0xFF7038F8)
            "dark" -> Color(0xFF705848)
            "fairy" -> Color(0xFFEE99AC)
            "normal" -> Color(0xFFA8A878)
            "fighting" -> Color(0xFFC03028)
            "flying" -> Color(0xFFA890F0)
            "poison" -> Color(0xFFA040A0)
            "ground" -> Color(0xFFE0C068)
            "rock" -> Color(0xFFB8A038)
            "bug" -> Color(0xFFA8B820)
            "ghost" -> Color(0xFF705898)
            "steel" -> Color(0xFFB8B8D0)
            else -> Color(0xFFA0A0A0)
        }
    }
}

// Estados para la pantalla de detalle
sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Success(val pokemon: Pokemon) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}