package ighorosipov.cocktailapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ighorosipov.cocktailapp.presentation.MainActivity
import ighorosipov.cocktailapp.presentation.details.DetailsFragment
import ighorosipov.cocktailapp.presentation.details.DetailsViewModel
import ighorosipov.cocktailapp.presentation.edit.EditFragment
import ighorosipov.cocktailapp.presentation.edit.EditViewModel
import ighorosipov.cocktailapp.presentation.main.MainFragment
import ighorosipov.cocktailapp.presentation.main.MainViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, RepositoryModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(detailsFragment: DetailsFragment)
    fun inject(editFragment: EditFragment)

    fun mainViewModel(): MainViewModel.Factory
    fun detailsViewModel(): DetailsViewModel.Factory
    fun editViewModel(): EditViewModel.Factory

}