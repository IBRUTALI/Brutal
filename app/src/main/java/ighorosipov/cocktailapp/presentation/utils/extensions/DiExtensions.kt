package ighorosipov.cocktailapp.presentation.utils.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ighorosipov.cocktailapp.App
import ighorosipov.cocktailapp.di.AppComponent

fun Context.appComponent(): AppComponent {
    return when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent()
    }
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
) = viewModels<T> {
    ViewModelFactory(this, create)
}