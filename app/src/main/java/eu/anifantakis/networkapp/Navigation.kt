package eu.anifantakis.networkapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import eu.anifantakis.networkapp.jokes.model.Joke
import eu.anifantakis.networkapp.jokes.screens.joke_details.JokeDetailsScreenRoot
import eu.anifantakis.networkapp.jokes.screens.jokes_list.JokesListScreenRoot
import kotlinx.serialization.Serializable

sealed interface RandomJokesRoute: NavKey{
    @Serializable data object JokesList: RandomJokesRoute
    @Serializable data class SelectedJoke(val joke: Joke): RandomJokesRoute
}

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(RandomJokesRoute.JokesList)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        entryProvider = entryProvider {
            entry<RandomJokesRoute.JokesList> {
                JokesListScreenRoot(
                    modifier = modifier,
                    onGoToJokeDetails = { joke ->
                        backStack.add(RandomJokesRoute.SelectedJoke(joke))
                    }
                )
            }

            entry<RandomJokesRoute.SelectedJoke> {
                println(it.joke.answer)

                JokeDetailsScreenRoot(
                    joke = it.joke,
                    modifier = modifier,
                    onGoBack = {
                        backStack.removeLastOrNull()
                    })
            }
        }
    )
}