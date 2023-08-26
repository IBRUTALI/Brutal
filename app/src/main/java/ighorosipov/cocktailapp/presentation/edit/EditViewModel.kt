package ighorosipov.cocktailapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel @AssistedInject constructor(
    @Assisted private val cocktailId: Int?,
    private val repository: CocktailRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _startCocktailValues = MutableLiveData<Cocktail?>()
    val startCocktailValues: LiveData<Cocktail?> = _startCocktailValues

    private val _cocktail = MutableLiveData<Cocktail?>()
    val cocktail: LiveData<Cocktail?> = _cocktail


    init {
        if (cocktailId != null)
            viewModelScope.launch(Dispatchers.IO) {
                repository.findCocktailById(cocktailId)?.let {
                    _startCocktailValues.postValue(it.data)
                    _cocktail.postValue(it.data)
                }
            }
        else {
            _cocktail.postValue(
                Cocktail(
                    name = "",
                    image = null,
                    description = null,
                    recipe = null,
                    ingredients = listOf()
                )
            )
        }
    }

    fun insertCocktail() {
        viewModelScope.launch(Dispatchers.IO) {
            cocktail.value?.let { cocktail ->
                if (cocktail.name.isNotBlank() && cocktail.ingredients.isNotEmpty()) {
                    repository.insertCocktail(cocktail)
                    _isLoading.postValue(true)
                }
            }
        }
    }

    fun setTitleChanges(title: String) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(name = title.trim())
            )
        }
    }

    fun setDescriptionChanges(description: String) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(description = description.trim())
            )
        }
    }

    fun setIngredientChanges(ingredient: List<String>) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(ingredients = ingredient)
            )
        }
    }

    fun setNewStartValues() {
        _startCocktailValues.postValue(
            cocktail.value
        )
    }

    fun setRecipeChanges(recipe: String) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(recipe = recipe.trim())
            )
        }
    }

    fun setImageChanges(image: String) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(image = image)
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted cocktailId: Int?): EditViewModel
    }

}