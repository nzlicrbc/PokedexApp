package com.example.pokedexapp.navigation

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedexapp.ui.screen.PokemonDetailScreen
import com.example.pokedexapp.ui.screen.PokemonListScreen
import com.example.pokedexapp.util.Constants

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.PokemonList.route
    ) {
        composable(
            route = NavigationRoute.PokemonList.route
        ) {
            PokemonListScreen(
                navController = navController
            )
        }

        composable(
            route = NavigationRoute.PokemonDetail.route,
            arguments = listOf(navArgument(Constants.POKEMON_ID_ARG) { type = NavType.IntType })
        ){ backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt(Constants.POKEMON_ID_ARG) ?: Constants.DEFAULT_POKEMON_ID
            PokemonDetailScreen(
                pokemonId = pokemonId,
                navController = navController
            )
        }
    }
}

sealed class NavigationRoute(val route: String) {
    object PokemonList : NavigationRoute(Constants.ROUTE_POKEMON_LIST)
    object PokemonDetail : NavigationRoute(Constants.ROUTE_POKEMON_DETAIL_WITH_ARG) {
        fun createRoute(pokemonId: Int) = "${Constants.ROUTE_POKEMON_DETAIL}/$pokemonId"
    }
}