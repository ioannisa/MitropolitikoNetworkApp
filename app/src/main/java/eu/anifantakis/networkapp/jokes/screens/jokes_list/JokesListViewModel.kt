package eu.anifantakis.networkapp.jokes.screens.jokes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.networkapp.jokes.data.JokesRepository
import eu.anifantakis.networkapp.jokes.model.Joke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class JokesListState(
    val jokes: List<Joke> = emptyList(),
    val loading: Boolean = false
)

sealed interface JokesListIntent {
    data object Refresh: JokesListIntent
    data class ClickOnJoke(val joke: Joke): JokesListIntent
    data class ToggleFavorite(val joke: Joke): JokesListIntent
}

sealed interface JokesListEvent {
    data class GotoJokeDetails(val joke: Joke): JokesListEvent
    data class ShowError(val message: String): JokesListEvent
}

/**
 * ViewModel for the jokes list screen with manual database observation and network loading.
 */
class JokesListViewModel(
    private val repository: JokesRepository
): ViewModel() {

    private val _state = MutableStateFlow(JokesListState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesListEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        // Start observing database and then load from network
        loadFromDB()
        loadFromNetwork()
    }

    fun onIntent(intent: JokesListIntent) {
        when(intent) {
            is JokesListIntent.Refresh -> {
                refresh()
            }

            is JokesListIntent.ClickOnJoke -> {
                viewModelScope.launch {
                    _eventChannel.send(JokesListEvent.GotoJokeDetails(intent.joke))
                }
            }

            is JokesListIntent.ToggleFavorite -> {
                toggleFavorite(intent.joke)
            }
        }
    }

    /**
     * Toggle the favorite status of a joke
     */
    private fun toggleFavorite(joke: Joke) {
        viewModelScope.launch {
            repository.toggleFavorite(joke.id)
                .onFailure { error ->
                    _eventChannel.send(JokesListEvent.ShowError("Failed to update favorite status: ${error.localizedMessage}"))
                }
        }
    }

    /**
     * Sets up observation of the local database.
     * This starts immediately to show cached data.
     */
    private fun loadFromDB() {
        viewModelScope.launch {
            // Start collecting from the Flow of jokes from the database
            repository.getJokes().collect { jokes ->
                _state.value = _state.value.copy(
                    jokes = jokes
                )
            }
        }
    }

    /**
     * Loads fresh data from the network API.
     * This doesn't directly update the UI state, but triggers
     * database updates which will be observed via loadFromDB().
     */
    private fun loadFromNetwork() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)

            repository.fetchJokesFromApi()
                .onSuccess {
                    // Success is handled through database Flow updates
                    _state.value = _state.value.copy(loading = false)
                }
                .onFailure { error ->
                    val errorMessage = error.localizedMessage ?: "Network error. Using cached data."
                    _eventChannel.send(JokesListEvent.ShowError(errorMessage))
                    _state.value = _state.value.copy(loading = false)
                }
        }
    }

    /**
     * Refresh jokes from the network.
     * This is effectively the same as loadFromNetwork but with
     * a different name to clarify its use as a user-triggered action.
     */
    private fun refresh() {
        loadFromNetwork()
    }
}