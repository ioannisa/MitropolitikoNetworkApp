package eu.anifantakis.networkapp.jokes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity representing a joke in the local Room database.
 */
@Entity(tableName = "joke")
data class JokeEntity(
    @PrimaryKey
    val id: Int,
    val question: String,
    val answer: String,
)

/**
 * Extension function to convert a JokeEntity to a Joke domain model.
 */
fun JokeEntity.toJoke(): Joke = Joke(
    id = id,
    question = question,
    answer = answer
)