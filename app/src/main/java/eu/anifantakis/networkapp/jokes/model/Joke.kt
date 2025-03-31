package eu.anifantakis.networkapp.jokes.model

import android.os.Parcelable
import eu.anifantakis.navhelper.serialization.StringSanitizer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@Serializable
@Parcelize
data class Joke @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int,

    @Serializable(with = StringSanitizer::class)
    val question: String,

    @Serializable(with = StringSanitizer::class)
    val answer: String,
): Parcelable

/**
 * Extension function to convert a Joke domain model to a JokeEntity.
 */
fun Joke.toEntity(): JokeEntity = JokeEntity(
    id = id,
    question = question,
    answer = answer,
)