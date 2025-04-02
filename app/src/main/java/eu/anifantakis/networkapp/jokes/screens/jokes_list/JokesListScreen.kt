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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import eu.anifantakis.networkapp.jokes.data.JokesRepository
import eu.anifantakis.networkapp.jokes.data.di.AppModule
import eu.anifantakis.networkapp.jokes.model.Joke

@Composable
fun JokesListScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: JokesListViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                JokesListViewModel(
                    repository = JokesRepository(
                        httpClient = AppModule.ktorClient,
                        database = AppModule.jokesDatabase.jokesDao()
                    )
                )
            }
        }
    ),
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
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = joke.question, fontWeight = FontWeight.Bold)
                                Text(text = joke.answer)
                            }

                            // Favorite icon
                            IconButton(
                                onClick = { onIntent(JokesListIntent.ToggleFavorite(joke)) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                if (joke.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Remove from favorites",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Add to favorites",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.loading) {
            CircularProgressIndicator()
        }
    }
}