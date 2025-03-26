package eu.anifantakis.networkapp.jokes.screens.joke_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.anifantakis.networkapp.jokes.model.Joke

@Composable
fun JokeDetailsScreenRoot(
    joke: Joke,
    modifier: Modifier = Modifier,
    viewModel: JokesDetailsViewModel = viewModel(),
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
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        Text(
            text = state.joke?.setup ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            lineHeight = 56.sp
        )
        Text(
            text = state.joke?.punchline ?: "",
            fontSize = 24.sp,
            lineHeight = 32.sp
        )

        Button(
            onClick = { onIntent(JokeDetailsIntent.GoBack) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Go Back")
        }
    }
}