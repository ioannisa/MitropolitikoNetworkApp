package eu.anifantakis.networkapp.jokes.screens.jokes_list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.anifantakis.networkapp.jokes.data.di.AppModule
import eu.anifantakis.networkapp.jokes.model.Joke

@Composable
fun JokesListScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: JokesListViewModel = viewModel {
        JokesListViewModel(
            repository = AppModule.jokesRepository
        )
    },
    onGoToJokeDetails: (Joke) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect {
            when (it) {
                is JokesListEvent.GotoJokeDetails -> {
                    onGoToJokeDetails(it.joke)
                }

                is JokesListEvent.ShowError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    JokesListScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun JokesListScreen(
    modifier: Modifier = Modifier,
    state: JokesListState,
    onIntent: (JokesListIntent) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    onIntent(JokesListIntent.Refresh)
                }
            ) {
                Text("Refresh")
            }

            LazyColumn {
                items(
                    items = state.jokes,
                    key = { it.id }
                ) { joke ->
                    JokesListItem(
                        joke = joke,
                        onIntent = onIntent
                    )
                }
            }
        }

        if (state.loading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun JokesListItem(
    joke: Joke,
    onIntent: (JokesListIntent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onIntent(JokesListIntent.ClickOnJoke(joke))
                }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = joke.question, fontWeight = FontWeight.Bold)
                    Text(text = joke.answer)
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(24.dp)
                ) {
                    if (joke.isFavorite) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Info",
                            tint = Color.Red
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Info",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewJokesListItem() {
    JokesListItem(
        joke = Joke(
            id = 1,
            question = "Why did the chicken cross the road?",
            answer = "To get to the other side!"
        ),
        onIntent = {}
    )
}

@Preview
@Composable
private fun PreviewJokesList() {
    JokesListScreen(
        state = JokesListState(
            jokes = listOf(
                Joke(
                    id = 1,
                    question = "Why did the chicken cross the road?",
                    answer = "To get to the other side!"
                ),
                Joke(
                    id = 2,
                    question = "What do you call a bear with no teeth?",
                    answer = "A gummy bear!"
                )
            ),
            loading = false
        ),
        onIntent = {}
    )
}