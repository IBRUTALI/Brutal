package ighorosipov.cocktailapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppModule {

    companion object {
        @Singleton
        @Provides
        fun provideContext(application: Application): Context {
            return application
        }
    }

}