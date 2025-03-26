package eu.anifantakis.networkapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import eu.anifantakis.navhelper.navtype.mapper
import eu.anifantakis.networkapp.jokes.model.Joke
import eu.anifantakis.networkapp.jokes.screens.joke_details.JokeDetailsScreenRoot
import eu.anifantakis.networkapp.jokes.screens.jokes_list.JokesListScreenRoot
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface RandomJokesRoute {
    @Serializable data object JokesList: RandomJokesRoute
    @Serializable data class SelectedJoke(val joke: Joke): RandomJokesRoute
}

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RandomJokesRoute.JokesList,
        modifier = modifier
    ) {
        composable<RandomJokesRoute.JokesList> {
            JokesListScreenRoot(
                modifier = modifier,
                onGoToJokeDetails = { joke ->
                    navController.navigate(RandomJokesRoute.SelectedJoke(joke))
                }
            )
        }

        composable<RandomJokesRoute.SelectedJoke>(
            typeMap = mapOf(typeOf<Joke>() to NavType.mapper<Joke>())
        ) {
            val args = it.toRoute<RandomJokesRoute.SelectedJoke>()
            val joke = args.joke

            JokeDetailsScreenRoot(
                joke = joke,
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                })

        }
    }
}