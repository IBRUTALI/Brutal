package ighorosipov.cocktailapp.presentation.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.FragmentEditBinding
import ighorosipov.cocktailapp.presentation.BUNDLE_COCKTAIL_ID
import ighorosipov.cocktailapp.presentation.edit.dialog.IngredientAddingDialog
import ighorosipov.cocktailapp.presentation.utils.extensions.appComponent
import ighorosipov.cocktailapp.presentation.utils.extensions.lazyViewModel

class EditFragment : Fragment() {
    private var mBinding: FragmentEditBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by lazyViewModel {
        requireContext().appComponent().editViewModel().create(getBundle())
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
        subscribeOnCocktailFieldChanges()
        validateEditFields()
    }

    private fun addIngredientButton() {
        binding.addIngredient.setOnClickListener {
            openAddingIngredientDialog()
        }
    }

    private fun addChip(text: String) {
        val chip = Chip(requireContext())
        chip.text = text
        chip.isCloseIconVisible = true
        chip.setCloseIconResource(R.drawable.cancel)
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(chip)
        }
        binding.chipGroup.addView(chip, 0)
    }

    private fun chipGroupToList() {
        val ingredients = StringBuilder()
        binding.chipGroup.forEach { view ->
            when (view) {
                is Chip -> {
                    val text = "${view.text}\n"
                    ingredients.append(text)
                }
            }
        }
        viewModel.setIngredientChanges(ingredients.trim().split("\n"))
    }

    private fun saveButton() {
        binding.saveButton.setOnClickListener {
            if (binding.titleInputLayout.editText?.text.isNullOrBlank()) {
                binding.titleInputLayout.error = getString(R.string.add_title)
            } else {
                viewModel.insertCocktail()
            }
        }
    }

    private fun cancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun subscribeOnInsert() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) findNavController().popBackStack()
        }
    }

    private fun subscribeOnCocktailFieldChanges() {
        viewModel.startCocktailValues.observe(viewLifecycleOwner) { cocktail ->
            binding.apply {
                titleInputLayout.editText?.setText(cocktail?.name, TextView.BufferType.EDITABLE)
                descriptionInputLayout.editText?.setText(cocktail?.description, TextView.BufferType.EDITABLE)
                recipeInputLayout.editText?.setText(cocktail?.recipe, TextView.BufferType.EDITABLE)
                cocktail?.ingredients?.forEach { ingredient ->
                    addChip(ingredient)
                }
            }
        }
    }

    private fun validateEditFields() {
        with(binding) {
            titleInputLayout.editText?.doAfterTextChanged {
                titleInputLayout.error = null
                viewModel.setTitleChanges(it?.toString() ?: "")
            }
            descriptionInputLayout.editText?.doAfterTextChanged {
                viewModel.setDescriptionChanges(it?.toString() ?: "")
            }
            recipeInputLayout.editText?.doAfterTextChanged {
                viewModel.setRecipeChanges(it?.toString() ?: "")
            }
        }
    }

    private fun getBundle(): Int? {
        return arguments?.getInt(BUNDLE_COCKTAIL_ID)

    }

    private fun openAddingIngredientDialog() {
        val dialogFragment = IngredientAddingDialog { ingredient ->
            addChip(ingredient)
            chipGroupToList()
        }
        dialogFragment.show(childFragmentManager, "ADD_INGREDIENT_DIALOG")
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