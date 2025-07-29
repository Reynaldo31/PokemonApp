package com.pokemonapp.pokemonapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pokemonapp.feature_pokemon.ui.detail.PokemonDetailScreen
import com.pokemonapp.feature_pokemon.ui.list.PokemonListScreen

@Composable
fun PokemonNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen { pokemonId ->
                navController.navigate(Screen.PokemonDetail.withId(pokemonId))
            }
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 1
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun withId(id: Int): String {
            return "pokemon_detail/$id"
        }
    }
}