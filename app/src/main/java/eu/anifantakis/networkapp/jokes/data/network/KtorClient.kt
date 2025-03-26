package eu.anifantakis.networkapp.jokes.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Be resilient to new fields in the JSON
                prettyPrint = true       // Useful for logging
                isLenient = true         // Be lenient to no-compliant JSON features

            })
        }

        engine {
            requestTimeout = 10000
        }
    }

}