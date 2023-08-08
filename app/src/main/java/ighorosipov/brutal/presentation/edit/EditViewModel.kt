package ighorosipov.brutal.presentation.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ighorosipov.brutal.data.data_source.CocktailDatabase
import ighorosipov.brutal.data.repository.CocktailRepositoryImpl
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.domain.repository.CocktailRepository
import kotlinx.coroutines.launch

class EditViewModel(application: Application): AndroidViewModel(application) {

    private val repository: CocktailRepository

    init {
        val mainDB = CocktailDatabase.getDB(application)
        val cocktailDao = mainDB.cocktailDao
        repository = CocktailRepositoryImpl(cocktailDao)
    }

    fun insertCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.insertCocktail(cocktail)
        }
    }

}