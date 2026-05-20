package eu.anifantakis.networkapp.jokes.features.jokes.domain

import kotlinx.coroutines.flow.Flow

interface JokesRepository {


    /**
     * Gets a Flow of all jokes from the database.
     * This should be observed by the UI to get database updates.
     * Jokes are sorted with favorites at the top.
     */
    fun getJokes(): Flow<List<Joke>>

    /**
     * Fetches jokes from the network API and updates the database.
     * Preserves favorites during refresh.
     *
     * @return Result indicating success or failure of the fetch operation
     */
    suspend fun fetchJokesFromApi(): Result<Unit>

    fun getLastUpdate(): String?

    /**
     * Get a joke by its ID from the database.
     */
    suspend fun getJokeById(id: Int): Result<Joke?>

    /**
     * Toggle the favorite status of a joke.
     */
    suspend fun toggleFavorite(jokeId: Int): Result<Unit>

    /**
     * Set the favorite status of a joke.
     */
    suspend fun setFavorite(jokeId: Int, isFavorite: Boolean): Result<Unit>

    /**
     * Get the current favorite status of a joke.
     */
    suspend fun isFavorite(jokeId: Int): Result<Boolean>

    /**
     * Clear all favorite jokes.
     */
    suspend fun clearAllFavorites(): Result<Unit>
}