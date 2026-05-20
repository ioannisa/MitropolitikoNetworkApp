package eu.anifantakis.networkapp.jokes.di

import androidx.room.Room
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.networkapp.jokes.features.core.data.JokesDatabase
import eu.anifantakis.networkapp.jokes.features.core.data.KtorClient
import eu.anifantakis.networkapp.jokes.features.core.data.MIGRATION_1_2
import eu.anifantakis.networkapp.jokes.features.jokes.data.JokesRepositoryImpl
import eu.anifantakis.networkapp.jokes.features.jokes.data.datasource.LocalJokesDataSourceImpl
import eu.anifantakis.networkapp.jokes.features.jokes.data.datasource.RemoteJokesDataSourceImpl
import eu.anifantakis.networkapp.jokes.features.jokes.domain.JokesRepository
import eu.anifantakis.networkapp.jokes.features.jokes.domain.datasource.LocalJokesDataSource
import eu.anifantakis.networkapp.jokes.features.jokes.domain.datasource.RemoteJokesDataSource
import eu.anifantakis.networkapp.jokes.features.jokes.presentation.screens.joke_details.JokesDetailsViewModel
import eu.anifantakis.networkapp.jokes.features.jokes.presentation.screens.jokes_list.JokesListViewModel
import androidx.appfunctions.service.AppFunctionConfiguration
import eu.anifantakis.networkapp.jokes.features.jokes.appfunctions.JokesAppFunctions
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { KtorClient.httpClient }
    single {
        Room.databaseBuilder(
            androidContext(),
            JokesDatabase::class.java,
            "jokes_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single { KSafe(androidContext()) }
    single<LocalJokesDataSource> { LocalJokesDataSourceImpl(get<JokesDatabase>().jokesDao()) }
    single<RemoteJokesDataSource> { RemoteJokesDataSourceImpl(get()) }

    factory<JokesRepository> {
        JokesRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            kSafe = get()
        )
    }

    // App Functions
    single { JokesAppFunctions() }
    single {
        AppFunctionConfiguration.Builder()
            .addEnclosingClassFactory(JokesAppFunctions::class.java) { get<JokesAppFunctions>() }
            .build()
    }

    viewModel { JokesListViewModel(get()) }
    viewModel { parameters -> JokesDetailsViewModel(joke = parameters.get(), repository = get()) }
}
