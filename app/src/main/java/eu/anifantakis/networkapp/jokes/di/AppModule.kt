package eu.anifantakis.networkapp.jokes.di

import android.content.Context
import androidx.room.Room
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.networkapp.jokes.features.jokes.domain.JokesRepository
import eu.anifantakis.networkapp.jokes.features.jokes.data.JokesRepositoryImpl
import eu.anifantakis.networkapp.jokes.features.core.data.JokesDatabase
import eu.anifantakis.networkapp.jokes.features.core.data.MIGRATION_1_2
import eu.anifantakis.networkapp.jokes.features.jokes.domain.datasource.LocalJokesDataSource
import eu.anifantakis.networkapp.jokes.features.jokes.data.datasource.LocalJokesDataSourceImpl
import eu.anifantakis.networkapp.jokes.features.jokes.domain.datasource.RemoteJokesDataSource
import eu.anifantakis.networkapp.jokes.features.jokes.data.datasource.RemoteJokesDataSourceImpl
import eu.anifantakis.networkapp.jokes.features.core.data.KtorClient
import io.ktor.client.HttpClient

object AppModule {

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    // SINGLETONS: We keep these as lazy so we don't open multiple database
    // connections or create multiple heavy Ktor clients.
    val ktorClient: HttpClient by lazy {
        KtorClient.httpClient
    }

    val jokesDatabase: JokesDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            JokesDatabase::class.java,
            "jokes_database"
        )
            .addMigrations(MIGRATION_1_2) // Add migration
            .build()
    }

    val kSafe: KSafe by lazy {
        KSafe(appContext)
    }

    val localJokesDataSource: LocalJokesDataSource by lazy {
        LocalJokesDataSourceImpl(jokesDatabase.jokesDao())
    }

    val remoteJokesDataSource: RemoteJokesDataSource by lazy {
        RemoteJokesDataSourceImpl(ktorClient)
    }

    // FACTORY: Using get() means a new instance is created on every call.
    val jokesRepository: JokesRepository
        get() = JokesRepositoryImpl(
            remoteDataSource = remoteJokesDataSource,
            localDataSource = localJokesDataSource,
            kSafe = kSafe
        )
}