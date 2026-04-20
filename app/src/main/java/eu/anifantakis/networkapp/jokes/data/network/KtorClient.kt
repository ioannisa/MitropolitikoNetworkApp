package eu.anifantakis.networkapp.jokes.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    val httpClient = HttpClient(CIO) {
        // expectSuccess = true // shorthand for: "install a built-in validator that throws when status 200..299."

        // For a customized management for success range, instead of "expectSuccess" use "HttpResponseValidator".
        HttpResponseValidator {
            validateResponse { response ->
                val code = response.status.value
                if (code !in 200..399) {
                    throw ResponseException(response, "HTTP $code")
                }
            }
        }

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