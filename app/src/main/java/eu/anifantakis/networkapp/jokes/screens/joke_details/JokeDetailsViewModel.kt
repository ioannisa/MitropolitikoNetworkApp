package eu.anifantakis.networkapp.jokes.screens.joke_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import eu.anifantakis.networkapp.jokes.model.Joke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class JokeDetailsState(
    val joke: Joke? = null,
    val loading: Boolean = false
)

sealed interface JokeDetailsIntent {
    data object MarkFavorite: JokeDetailsIntent
    data object GoBack: JokeDetailsIntent
}

sealed interface JokesDetailsEvent {
    data object GoBack: JokesDetailsEvent
}

class JokesDetailsViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(JokeDetailsState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesDetailsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onIntent(intent: JokeDetailsIntent) {
        when(intent) {
            JokeDetailsIntent.GoBack -> { _eventChannel.trySend(JokesDetailsEvent.GoBack) }
            JokeDetailsIntent.MarkFavorite -> {}
        }
    }

    init {
        _state.value = _state.value.copy(
            joke = savedStateHandle.get<Joke>("joke")
        )
    }
}
