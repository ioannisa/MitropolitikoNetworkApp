package eu.anifantakis.networkapp.jokes.features.jokes.domain.datasource

import eu.anifantakis.networkapp.jokes.features.jokes.data.model.JokeEntity

interface RemoteJokesDataSource {

    suspend fun fetchJokesFromApi() : List<JokeEntity>

}