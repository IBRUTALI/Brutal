package ighorosipov.cocktailapp

import android.app.Application
import ighorosipov.cocktailapp.di.AppComponent
import ighorosipov.cocktailapp.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

}