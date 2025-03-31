package eu.anifantakis.networkapp.jokes.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import eu.anifantakis.networkapp.jokes.model.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokesDao {
    @Query("SELECT * FROM joke")
    fun getAllJokes(): Flow<List<JokeEntity>>

    @Upsert
    suspend fun upsertJokes(jokes: List<JokeEntity>)

    @Query("DELETE FROM joke")
    suspend fun deleteAllJokes()

    @Query("SELECT * FROM joke WHERE id = :jokeId")
    suspend fun getJokeById(jokeId: Int): JokeEntity?
}