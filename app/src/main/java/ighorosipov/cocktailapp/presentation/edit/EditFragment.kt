package ighorosipov.cocktailapp.presentation.edit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.FragmentEditBinding
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.presentation.BUNDLE_COCKTAIL
import ighorosipov.cocktailapp.presentation.utils.extensions.appComponent
import ighorosipov.cocktailapp.presentation.utils.extensions.lazyViewModel

class EditFragment : Fragment() {
    private var mBinding: FragmentEditBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by lazyViewModel {
        requireContext().appComponent().editViewModel().create()
    }

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
        subscribeOnInsert()
        addIngredientButton()
        saveButton()
        cancelButton()
        setupIfFromBundle()
    }

    private fun addIngredientButton() {
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

    private fun saveButton() {
        binding.saveButton.setOnClickListener {
                insertCocktail()
        }
    }

    private fun cancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun insertCocktail() {
        val cocktail = validateData()
        cocktail?.let {
            viewModel.insertCocktail(it)
        }
    }

    private fun subscribeOnInsert() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) findNavController().popBackStack()
        }
    }

    private fun validateData(): Cocktail? {
        binding.apply {
            return if (titleEditText.text.isNullOrBlank()) {
                titleInputLayout.error = getString(R.string.add_title)
                null
            } else if (chipGroup.size == 1) {
                Toast.makeText(requireContext(),
                    getString(R.string.add_ingredient), Toast.LENGTH_SHORT).show()
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
                            val text = "${view.text}\n"
                            ingredients.append(text)
                        }
                    }
                }
                buildCocktail(
                    image = image,
                    name = title,
                    description = description,
                    recipe = recipe,
                    ingredients = ingredients.split("\n")
                )
            }
        }
    }

    private fun buildCocktail(
        name: String,
        image: String? = null,
        description: String?,
        recipe: String?,
        ingredients: List<String>
    ): Cocktail {
        val cocktail = getBundle()
        return cocktail?.copy(
            name = name,
            image = image,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        ) ?: Cocktail(
            name = name,
            image = image,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
    }

    private fun setupIfFromBundle() {
        val cocktail = getBundle()
        if (cocktail != null) {
            binding.apply {
                titleEditText.setText(cocktail.name, TextView.BufferType.EDITABLE)
                descriptionEditText.setText(cocktail.description, TextView.BufferType.EDITABLE)
                recipeEditText.setText(cocktail.recipe, TextView.BufferType.EDITABLE)
                cocktail.ingredients.forEach { ingredient ->
                    addChip(ingredient)
                }
            }
        }
    }

    private fun getBundle(): Cocktail? {
        return arguments?.getSerializable(BUNDLE_COCKTAIL) as Cocktail?
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
            } else inputLayout.error = getString(R.string.add_ingredient)
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun inject() {
        requireContext().appComponent().inject(this)
    }

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}