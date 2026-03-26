package eu.anifantakis.networkapp.jokes.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke (
    val id: Int,
    val question: String,
    val answer: String,
)