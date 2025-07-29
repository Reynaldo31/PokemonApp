package com.pokemonapp.domain.model

data class Pokemon (
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val stats: List<Stat>,
    val height: Int,
    val weight: Int,
    val abilities: List<String>,
    val description: String
)

data class Stat(
    val name: String,
    val value: Int
)

data class PokemonListItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>
)

data class PokemonSpeciesResponse(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language,
    val version: GameVersion
)

data class Language(
    val name: String,
    val url: String
)

data class GameVersion(
    val name: String,
    val url: String
)