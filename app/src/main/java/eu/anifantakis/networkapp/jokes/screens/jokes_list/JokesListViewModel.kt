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
}

sealed interface JokesListEvent {
    data class GotoJokeDetails(val joke: Joke): JokesListEvent
    data class ShowError(val message: String): JokesListEvent
}

class JokesListViewModel(
    val repository: JokesRepository
): ViewModel() {

    private val _state = MutableStateFlow(JokesListState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesListEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onIntent(intent: JokesListIntent) {
        when(intent) {
            is JokesListIntent.Refresh -> {
                loadJokes()
            }

            is JokesListIntent.ClickOnJoke -> {
                viewModelScope.launch {
                    _eventChannel.send(JokesListEvent.GotoJokeDetails(intent.joke))
                }
            }
        }
    }

    init {
        onIntent(JokesListIntent.Refresh)
    }

    private fun loadJokes() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                loading = true
            )

            repository.getJokes()
                .onSuccess { jokes ->
                    _state.value = _state.value.copy(
                        jokes = jokes,
                        loading = false
                    )
                }
                .onFailure { error ->
                    val errorMessage = error.localizedMessage ?: "Unknown error"
                    _eventChannel.send(JokesListEvent.ShowError(errorMessage))
                    _state.value = _state.value.copy(
                        loading = false
                    )
                }
        }
    }
}