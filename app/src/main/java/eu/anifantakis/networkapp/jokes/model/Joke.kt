package eu.anifantakis.networkapp.jokes.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke (
    val id: Int,
    val question: String,
    val answer: String,
)
/**
 * Extension function to convert a Joke domain model to a JokeEntity.
 */
fun Joke.toEntity(): JokeEntity = JokeEntity(
    id = id,
    question = question,
    answer = answer,
)