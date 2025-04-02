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
     * Jokes are sorted with favorites at the top.
     */
    fun getJokes(): Flow<List<Joke>> {
        // The ordering is handled by the DAO query (ORDER BY isFavorite DESC, id ASC)
        return database.getAllJokes().map { entities ->
            entities.map { it.toJoke() }
        }
    }

    /**
     * Fetches jokes from the network API and updates the database.
     * Preserves favorites during refresh.
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

            // Delete only non-favorite jokes
            database.deleteAllNonFavoriteJokes()

            // Upsert new jokes (this won't affect existing favorites due to Room's upsert behavior)
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

    /**
     * Toggle the favorite status of a joke.
     */
    suspend fun toggleFavorite(jokeId: Int): Result<Unit> {
        return runCatching {
            database.toggleFavorite(jokeId)
        }
    }

    /**
     * Set the favorite status of a joke.
     */
    suspend fun setFavorite(jokeId: Int, isFavorite: Boolean): Result<Unit> {
        return runCatching {
            database.setFavorite(jokeId, isFavorite)
        }
    }

    /**
     * Get the current favorite status of a joke.
     */
    suspend fun isFavorite(jokeId: Int): Result<Boolean> {
        return runCatching {
            database.getJokeById(jokeId)?.isFavorite ?: false
        }
    }
}