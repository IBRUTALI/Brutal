package ighorosipov.cocktailapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.repository.CocktailRepository
import ighorosipov.cocktailapp.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val repository: CocktailRepository
) : ViewModel() {

    private val _cocktails = MutableLiveData<Result<List<Cocktail>>>()
    val cocktails: LiveData<Result<List<Cocktail>>> = _cocktails

    fun getCocktails() {
        viewModelScope.launch(Dispatchers.IO) {
            _cocktails.postValue(Result.Loading())
            _cocktails.postValue(repository.getCocktails())
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainViewModel
    }

}