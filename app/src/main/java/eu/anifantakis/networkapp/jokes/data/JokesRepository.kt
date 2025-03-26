package eu.anifantakis.networkapp.jokes.data

import eu.anifantakis.networkapp.jokes.data.network.KtorClient
import eu.anifantakis.networkapp.jokes.model.Joke
import eu.anifantakis.networkapp.jokes.model.JokeDto
import eu.anifantakis.networkapp.jokes.model.toJoke
import io.ktor.client.call.body
import io.ktor.client.request.get

class JokesRepository {
    private val httpClient = KtorClient.httpClient
    private val baseUrl = "https://official-joke-api.appspot.com"
    private val randomJokesPath = "/random_ten"

    /**
     * Fetches a list of random jokes from the API.
     * Returns a Result wrapping the list of jokes on success, or an exception on failure.
     */
    suspend fun getJokes(): Result<List<Joke>> {
        return runCatching {    // Use runCatching for concise success/failure handling
            httpClient.get("$baseUrl$randomJokesPath").body<List<JokeDto>>()
                .map { it.toJoke() }
        }
    }
}