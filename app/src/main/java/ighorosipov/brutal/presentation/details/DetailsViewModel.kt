package ighorosipov.brutal.presentation.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ighorosipov.brutal.data.data_source.CocktailDatabase
import ighorosipov.brutal.data.repository.CocktailRepositoryImpl
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.domain.repository.CocktailRepository
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: CocktailRepository

    private val _cocktail = MutableLiveData<Cocktail>()
    val cocktail: LiveData<Cocktail> = _cocktail

    init {
        val mainDB = CocktailDatabase.getDB(application)
        val cocktailDao = mainDB.cocktailDao
        repository = CocktailRepositoryImpl(cocktailDao)
    }

    fun findCocktailById(id: Int) {
        viewModelScope.launch {
            _cocktail.value = repository.findCocktailById(id)
        }
    }

    fun deleteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.deleteCocktail(cocktail)
        }
    }

}