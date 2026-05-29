package eu.anifantakis.networkapp.jokes.features.jokes.appfunctions

import androidx.appfunctions.AppFunctionSerializable

// Boundary DTO for the AppFunctions layer — the agent-facing counterpart of the network JokeDto
// and the Room JokeEntity. We can't annotate the domain `Joke` with @AppFunctionSerializable
// (it is androidx/Android code and would break domain purity), so the AppFunctions boundary gets
// its own model + mapper, exactly like the network and persistence boundaries do. As a bonus the
// agent-facing schema can be shaped independently of the domain model (note we drop `isFavorite`).
//
// Heads up: with `isDescribedByKDoc = true`, the KDoc *summary* below becomes the type description
// the agent reads, and each property's inline KDoc becomes that property's description — so keep
// those agent-facing and concise. KSP ignores class-level @param/@property tags for serializables;
// document properties inline, as below. This developer note is a plain `//` comment on purpose, so
// KSP does NOT ship it into the schema.

/**
 * A single joke from this app, modelled as a question-and-answer pair: the question is the set-up
 * and the answer is the punchline. Returned by joke-reading functions such as the user's favorites
 * list, and identified by a stable numeric id that other functions accept to act on this joke.
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AppFunctionJoke(
    /** Stable unique identifier of the joke. Pass this back to functions that act on a single joke. */
    val id: Int,
    /** The set-up line of the joke (its "question" part). */
    val question: String,
    /** The punchline of the joke (its "answer" part). */
    val answer: String,
)
