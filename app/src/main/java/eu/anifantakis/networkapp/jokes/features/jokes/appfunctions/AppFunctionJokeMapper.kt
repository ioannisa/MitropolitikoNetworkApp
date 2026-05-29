package eu.anifantakis.networkapp.jokes.features.jokes.appfunctions

import eu.anifantakis.networkapp.jokes.features.jokes.domain.Joke

/**
 * Extension function to convert a domain [Joke] into its AppFunctions boundary model [AppFunctionJoke].
 */
fun Joke.toAppFunctionJoke(): AppFunctionJoke = AppFunctionJoke(
    id = id,
    question = question,
    answer = answer,
)
