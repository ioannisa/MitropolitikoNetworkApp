package eu.anifantakis.networkapp

import android.app.Application
import eu.anifantakis.networkapp.jokes.data.di.AppModule

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize our dependencies
        AppModule.initialize(applicationContext)
    }

}