package ighorosipov.cocktailapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel @AssistedInject constructor(
    private val repository: CocktailRepository
): ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun insertCocktail(cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCocktail(cocktail)
            _isLoading.postValue(true)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): EditViewModel
    }

}