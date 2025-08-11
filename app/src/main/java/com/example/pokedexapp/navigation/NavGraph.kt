package com.example.pokedexapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp.ui.screen.PokemonListScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "pokemon_list"
    ) {
        composable(
            route = NavigationRoute.PokemonList.route
        ) {
            PokemonListScreen(
                navController = navController
            )
        }
    }
}

sealed class NavigationRoute(val route: String) {
    object PokemonList : NavigationRoute("pokemon_list")
    object PokemonDetail : NavigationRoute("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}