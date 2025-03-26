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
    val setup: String,

    @Serializable(with = StringSanitizer::class)
    val punchline: String,
): Parcelable