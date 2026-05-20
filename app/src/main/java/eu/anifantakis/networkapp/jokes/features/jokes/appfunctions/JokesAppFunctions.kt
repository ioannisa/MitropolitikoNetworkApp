package eu.anifantakis.networkapp.jokes.features.jokes.appfunctions

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction
import eu.anifantakis.networkapp.jokes.features.jokes.domain.JokesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * App Functions that can be exposed to AI agents via MCP.
 */
class JokesAppFunctions : KoinComponent {

    private val repository: JokesRepository by inject()

    /**
     * Clears all favorite jokes from the database.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun clearFavorites(context: AppFunctionContext): String {
        val result = repository.clearAllFavorites()
        
        return if (result.isSuccess) {
            "All favorite jokes have been cleared successfully."
        } else {
            "Failed to clear favorite jokes: ${result.exceptionOrNull()?.message}"
        }
    }
}


/* Test by running:

adb shell cmd app_function list-app-functions | grep -A 10 "eu.anifantakis.networkapp"

adb shell cmd app_function execute-app-function \
  --package eu.anifantakis.networkapp \
  --function eu.anifantakis.networkapp.jokes.features.jokes.appfunctions.JokesAppFunctions#clearFavorites \
  --parameters '{}'

 */



