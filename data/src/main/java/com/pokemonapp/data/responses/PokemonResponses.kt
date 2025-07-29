package com.pokemonapp.data.responses

import com.google.gson.annotations.SerializedName

class PokemonResponses {
    data class PokemonListResponse(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: List<PokemonResult>
    )

    data class PokemonResult(
        val name: String,
        val url: String
    )

    data class PokemonDetailsResponse(
        val id: Int,
        val name: String,
        val height: Int,
        val weight: Int,
        val stats: List<StatResponse>,
        val types: List<TypeResponse>,
        val abilities: List<AbilityResponse>,
        val sprites: SpritesResponse
    )

    data class StatResponse(
        val stat: StatDetailResponse,
        @SerializedName("base_stat") val baseStat: Int
    )

    data class StatDetailResponse(
        val name: String
    )

    data class TypeResponse(
        val type: TypeDetailResponse
    )

    data class TypeDetailResponse(
        val name: String
    )

    data class AbilityResponse(
        val ability: AbilityDetailResponse
    )

    data class AbilityDetailResponse(
        val name: String
    )

    data class SpritesResponse(
        @SerializedName("other") val other: OtherSpritesResponse?
    )

    data class OtherSpritesResponse(
        @SerializedName("home") val home: HomeSpriteResponse?
    )

    data class HomeSpriteResponse(
        @SerializedName("front_default") val frontDefault: String?
    )
}