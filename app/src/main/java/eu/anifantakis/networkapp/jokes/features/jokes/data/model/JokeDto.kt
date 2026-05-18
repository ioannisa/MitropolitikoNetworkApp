package eu.anifantakis.networkapp.jokes.features.jokes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JokeDto(
    val id: Int,
    val setup: String,
    val punchline: String
)