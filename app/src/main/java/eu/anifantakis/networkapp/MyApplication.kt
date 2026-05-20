package eu.anifantakis.networkapp

import android.app.Application
import androidx.appfunctions.service.AppFunctionConfiguration
import eu.anifantakis.networkapp.jokes.di.AppModule
import eu.anifantakis.networkapp.jokes.features.jokes.appfunctions.JokesAppFunctions

class MyApplication: Application(), AppFunctionConfiguration.Provider {

    override fun onCreate() {
        super.onCreate()

        // Initialize our dependencies
        AppModule.initialize(applicationContext)
    }

    override val appFunctionConfiguration: AppFunctionConfiguration
        get() = AppFunctionConfiguration.Builder()
            .addEnclosingClassFactory(JokesAppFunctions::class.java) { JokesAppFunctions() }
            .build()

}