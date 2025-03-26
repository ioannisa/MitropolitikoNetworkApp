package eu.anifantakis.networkapp.jokes.data

import eu.anifantakis.networkapp.jokes.model.Joke
import kotlinx.coroutines.delay

class JokesRepository {
    suspend fun getJokes(): List<Joke> {
        delay(1000L)
        return listOf(
            Joke(setup = "Joke 1", punchline = "Answer 1"),
            Joke(setup = "Joke 2", punchline = "Answer 2"),
            Joke(setup = "Joke 3", punchline = "Answer 3"),
        )
    }
}