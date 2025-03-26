package eu.anifantakis.networkapp.jokes.model

import kotlinx.serialization.Serializable

@Serializable
data class JokeDto(
    val id: Int,
    val setup: String,
    val punchline: String
)

fun JokeDto.toJoke(): Joke {
    return Joke(
        id = id,
        setup = setup,
        punchline = punchline
    )
}