package eu.anifantakis.networkapp.jokes.data

import eu.anifantakis.networkapp.jokes.data.database.JokesDao
import eu.anifantakis.networkapp.jokes.model.Joke
import eu.anifantakis.networkapp.jokes.model.JokeDto
import eu.anifantakis.networkapp.jokes.model.toEntity
import eu.anifantakis.networkapp.jokes.model.toJoke
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository implementation for jokes data with offline-first approach.
 */
class JokesRepository(
    private val httpClient: HttpClient,
    private val database: JokesDao
) {
    private val baseUrl = "https://official-joke-api.appspot.com"
    private val randomJokesPath = "/random_ten"

    /**
     * Gets a Flow of all jokes from the database.
     * This should be observed by the UI to get database updates.
     */
    fun getJokes(): Flow<List<Joke>> {
        return database.getAllJokes().map { entities ->
            entities.map { it.toJoke() }
        }
    }

    /**
     * Fetches jokes from the network API and updates the database.
     * This doesn't return the fetched data directly - instead, the database
     * will be updated, which will trigger observers of the getJokes() Flow.
     *
     * @return Result indicating success or failure of the fetch operation
     */
    suspend fun fetchJokesFromApi(): Result<Unit> {
        return runCatching {
            println("Fetching jokes from API")

            // Fetch jokes from the API
            val remoteJokes = httpClient.get("$baseUrl$randomJokesPath").body<List<JokeDto>>()

            val jokesToUpsert = remoteJokes.map { jokeDto ->
                jokeDto.toJoke().toEntity()
            }

            // Delete all jokes and upsert new ones
            database.deleteAllJokes()
            database.upsertJokes(jokesToUpsert)

            println("Successfully updated database with ${remoteJokes.size} jokes")
        }
    }

    /**
     * Get a joke by its ID from the database.
     */
    suspend fun getJokeById(id: Int): Result<Joke?> {
        return runCatching {
            database.getJokeById(id)?.toJoke()
        }
    }
}