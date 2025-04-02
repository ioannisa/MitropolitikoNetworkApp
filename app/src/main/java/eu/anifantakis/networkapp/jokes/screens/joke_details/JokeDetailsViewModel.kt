package eu.anifantakis.networkapp.jokes.screens.joke_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.networkapp.jokes.data.JokesRepository
import eu.anifantakis.networkapp.jokes.model.Joke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class JokeDetailsState(
    val joke: Joke? = null,
    val loading: Boolean = false
)

sealed interface JokeDetailsIntent {
    data object ToggleFavorite: JokeDetailsIntent
    data object GoBack: JokeDetailsIntent
}

sealed interface JokesDetailsEvent {
    data object GoBack: JokesDetailsEvent
}

class JokesDetailsViewModel(
    joke: Joke,
    private val repository: JokesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(JokeDetailsState(joke = joke))
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesDetailsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onIntent(intent: JokeDetailsIntent) {
        when(intent) {
            JokeDetailsIntent.GoBack -> {
                _eventChannel.trySend(JokesDetailsEvent.GoBack)
            }

            JokeDetailsIntent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    /**
     * Toggle the favorite status of the current joke
     */
    private fun toggleFavorite() {
        viewModelScope.launch {
            val jokeId = state.value.joke?.id ?: return@launch

            repository.toggleFavorite(jokeId)
                .onSuccess {
                    // Update the local state to reflect the change
                    val updatedJoke = repository.getJokeById(jokeId).getOrNull()
                    updatedJoke?.let {
                        _state.value = _state.value.copy(joke = it)
                    }
                }
        }
    }
}

/**
 * Alternative constructor for use with SavedStateHandle
 */
class JokesDetailsViewModelAlt(
    savedStateHandle: SavedStateHandle,
    private val repository: JokesRepository
): ViewModel() {

    private val _state = MutableStateFlow(JokeDetailsState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesDetailsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        _state.value = _state.value.copy(
            joke = savedStateHandle.get<Joke>("joke")
        )
    }

    fun onIntent(intent: JokeDetailsIntent) {
        when(intent) {
            JokeDetailsIntent.GoBack -> {
                _eventChannel.trySend(JokesDetailsEvent.GoBack)
            }

            JokeDetailsIntent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    /**
     * Toggle the favorite status of the current joke
     */
    private fun toggleFavorite() {
        viewModelScope.launch {
            val jokeId = state.value.joke?.id ?: return@launch

            repository.toggleFavorite(jokeId)
                .onSuccess {
                    // Update the local state to reflect the change
                    val updatedJoke = repository.getJokeById(jokeId).getOrNull()
                    updatedJoke?.let {
                        _state.value = _state.value.copy(joke = it)
                    }
                }
        }
    }
}