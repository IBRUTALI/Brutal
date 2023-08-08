package ighorosipov.brutal.presentation.main

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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CocktailRepository

    private val _cocktails = MutableLiveData<List<Cocktail>>()
    val cocktails: LiveData<List<Cocktail>> = _cocktails

    init {
        val mainDB = CocktailDatabase.getDB(application)
        val cocktailDao = mainDB.cocktailDao
        repository = CocktailRepositoryImpl(cocktailDao)
    }

    fun getCocktails() {
        viewModelScope.launch {
            _cocktails.value = repository.getCocktails()
        }
    }

}