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
import ighorosipov.cocktailapp.domain.utils.Result
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
                repository.findCocktailById(cocktailId)?.let {result ->
                    when(result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            _startCocktailValues.postValue(result.data)
                            _cocktail.postValue(result.data)
                        }
                        is Result.Error -> {

                        }
                    }

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

    fun setIngredientChanges(ingredient: String) {
        cocktail.value?.let {
            if (ingredient in it.ingredients) return
            _cocktail.postValue(
                it.copy(ingredients = it.ingredients.plus(ingredient))
            )
        }
    }

    fun deleteIngredient(ingredient: String) {
        cocktail.value?.let {
            _cocktail.postValue(
                it.copy(ingredients = it.ingredients.remove(ingredient.trim()))
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

    private fun List<String>.remove(string: String): List<String> {
        val result = this.toMutableList()
        result.remove(string)
        return result
    }

}