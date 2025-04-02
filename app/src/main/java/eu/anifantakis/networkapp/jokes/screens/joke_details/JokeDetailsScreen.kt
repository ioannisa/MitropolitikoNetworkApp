package eu.anifantakis.networkapp.jokes.screens.joke_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import eu.anifantakis.networkapp.jokes.data.di.AppModule
import eu.anifantakis.networkapp.jokes.model.Joke

@Composable
fun JokeDetailsScreenRoot(
    joke: Joke,
    modifier: Modifier = Modifier,
    viewModel: JokesDetailsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                JokesDetailsViewModel(
                    joke = joke,
                    repository = AppModule.jokesRepository
                )
            }
        }
    ),
    onGoBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect {
            when (it) {
                JokesDetailsEvent.GoBack -> onGoBack()
            }
        }
    }

    state.joke?.let {
        JokeDetailsScreen(
            modifier = modifier,
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
private fun JokeDetailsScreen(
    modifier: Modifier = Modifier,
    state: JokeDetailsState,
    onIntent: (JokeDetailsIntent) -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header with favorite button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Joke Details",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            // Favorite toggle button
            IconButton(
                onClick = { onIntent(JokeDetailsIntent.ToggleFavorite) },
                modifier = Modifier.size(48.dp)
            ) {
                if (state.joke?.isFavorite == true) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Remove from favorites",
                        tint = Color.Red,
                        modifier = Modifier.size(36.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Joke content
        Text(
            text = state.joke?.question ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = state.joke?.answer ?: "",
            fontSize = 20.sp,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onIntent(JokeDetailsIntent.GoBack) },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Go Back")
        }
    }
}