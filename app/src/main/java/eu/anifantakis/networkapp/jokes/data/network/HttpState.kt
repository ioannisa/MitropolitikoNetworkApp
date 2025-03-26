package eu.anifantakis.networkapp.jokes.data.network

/**
 * A generic sealed class representing the state of data loading for the UI.
 * @param T The type of data being loaded.
 */
sealed class DataUiState<out T> { // Use 'out T' for covariance if needed, makes it read-only usable
    /** Represents the initial or data loading state. */
    data object Loading : DataUiState<Nothing>() // Can use Nothing as no data is present

    /**
     * Represents the state when data has been successfully loaded.
     * @param data The successfully loaded data of type T.
     */
    data class Success<T>(val data: T) : DataUiState<T>()

    /**
     * Represents the state when an error occurred during data loading.
     * @param message A descriptive message about the error.
     */
    data class Error(val message: String) : DataUiState<Nothing>() // Can use Nothing as no data is present
}