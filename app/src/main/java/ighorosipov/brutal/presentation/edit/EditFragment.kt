package ighorosipov.brutal.presentation.edit

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ighorosipov.brutal.R
import ighorosipov.brutal.databinding.FragmentEditBinding
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.presentation.COCKTAIL
import ighorosipov.brutal.presentation.SPLIT_INGREDIENT_CHARS
import ighorosipov.brutal.presentation.main.MainFragment

class EditFragment : Fragment() {
    private var mBinding: FragmentEditBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by viewModels<EditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addIngredient()
        saveCocktail()
        cancel()
        setupIfFromBundle()
    }

    private fun addIngredient() {
        binding.addIngredient.setOnClickListener {
            openDialog()
        }
    }

    private fun addChip(text: String) {
        if (text.isNotBlank()) {
            val chip = Chip(requireContext())
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setCloseIconResource(R.drawable.cancel)
            chip.setOnCloseIconClickListener {
                binding.chipGroup.removeView(chip)
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun saveCocktail() {
        binding.saveButton.setOnClickListener {
            insertCocktail()
        }
    }

    private fun cancel() {
        binding.cancelButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun insertCocktail() {
        val cocktail = validateData()
        cocktail?.let {
            viewModel.insertCocktail(it)
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun validateData(): Cocktail? {
        binding.apply {
            return if (titleEditText.text.isNullOrBlank()) {
                titleInputLayout.error = "Add title"
                null
            } else if (chipGroup.size == 1) {
                Toast.makeText(requireContext(), "Add ingredient", Toast.LENGTH_SHORT).show()
                null
            } else {
                val image = null
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val recipe = recipeEditText.text.toString()
                val ingredients = StringBuilder()
                chipGroup.forEach { view ->
                    when (view) {
                        is Chip -> {
                            val text = "${view.text}$SPLIT_INGREDIENT_CHARS"
                            ingredients.append(text)
                        }
                    }
                }
                buildCocktail(
                    image = image,
                    title = title,
                    description = description,
                    recipe = recipe,
                    ingredients = ingredients.toString()
                )
            }
        }
    }

    private fun buildCocktail(
        image: String? = null,
        title: String,
        description: String?,
        recipe: String?,
        ingredients: String
    ): Cocktail {
        val cocktail = getBundle()
        return cocktail?.copy(
            image = image,
            title = title,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        ) ?: Cocktail(
            image = image,
            title = title,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
    }

    private fun setupIfFromBundle() {
        val cocktail = getBundle()
        if (cocktail != null) {
            binding.apply {
                titleEditText.setText(cocktail.title, TextView.BufferType.EDITABLE)
                descriptionEditText.setText(cocktail.description, TextView.BufferType.EDITABLE)
                recipeEditText.setText(cocktail.recipe, TextView.BufferType.EDITABLE)
                val ingredientList = cocktail.ingredients.split(SPLIT_INGREDIENT_CHARS)
                ingredientList.forEach { ingredient ->
                    addChip(ingredient)
                }
            }
        }
    }

    private fun getBundle(): Cocktail? {
        return arguments?.getSerializable(COCKTAIL) as Cocktail?
    }

    private fun openDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customDialog =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        builder.setView(customDialog)
        val dialog = builder.create()
        val buttonAdd = customDialog.findViewById<Button>(R.id.addButton)
        val buttonCancel = customDialog.findViewById<Button>(R.id.cancelButton)
        val editText = customDialog.findViewById<TextInputEditText>(R.id.titleEditText)
        val inputLayout = customDialog.findViewById<TextInputLayout>(R.id.titleInputLayout)

        buttonAdd.setOnClickListener {
            val ingredient = editText.text.toString()
            if (ingredient.isNotBlank()) {
                addChip(ingredient)
                dialog.dismiss()
            } else inputLayout.error = "Add ingredient"
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}