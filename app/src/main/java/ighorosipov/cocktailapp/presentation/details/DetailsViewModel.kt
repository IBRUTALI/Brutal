package ighorosipov.cocktailapp.presentation.details

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

class DetailsViewModel @AssistedInject constructor(
    private val repository: CocktailRepository
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _cocktail = MutableLiveData<Result<Cocktail>>()
    val cocktail: LiveData<Result<Cocktail>> = _cocktail

    fun findCocktailById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _cocktail.postValue(Result.Loading())
            _cocktail.postValue(repository.findCocktailById(id))
        }
    }

    fun deleteCocktail(cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCocktail(cocktail)
            _isLoading.postValue(true)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): DetailsViewModel
    }

}