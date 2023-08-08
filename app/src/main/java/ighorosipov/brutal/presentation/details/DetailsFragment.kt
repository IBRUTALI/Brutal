package ighorosipov.brutal.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import ighorosipov.brutal.R
import ighorosipov.brutal.databinding.FragmentDetailsBinding
import ighorosipov.brutal.presentation.COCKTAIL
import ighorosipov.brutal.presentation.COCKTAIL_ID
import ighorosipov.brutal.presentation.SPLIT_INGREDIENT_CHARS
import ighorosipov.brutal.presentation.edit.EditFragment
import ighorosipov.brutal.presentation.main.MainFragment

class DetailsFragment: Fragment() {
    private var mBinding: FragmentDetailsBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findCocktail()
        cocktailObserver()
        editCocktail()
        deleteCocktail()
    }

    private fun findCocktail() {
        val id = getBundle()!!
        viewModel.findCocktailById(id)
    }

    private fun cocktailObserver() {
        viewModel.cocktail.observe(viewLifecycleOwner) {cocktail ->
            binding.apply {
                titleText.text = cocktail.title
                descriptionText.text = cocktail.description
                splitString(cocktail.ingredients)
                recipeText.text = cocktail.recipe
            }
        }
    }

    private fun splitString(string: String) {
        val ingredientList = string.split(SPLIT_INGREDIENT_CHARS)
        ingredientList.forEach {ingredient ->
            val text = "${binding.ingredientText.text}\n -\n$ingredient"
            binding.ingredientText.text = text
        }
    }

    private fun editCocktail() {
        binding.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(COCKTAIL, viewModel.cocktail.value)
            val editFragment = EditFragment()
            editFragment.arguments = bundle
            activity?.supportFragmentManager?.commit {
                replace(R.id.mainContainer, editFragment)
                setReorderingAllowed(true)
                addToBackStack("name")
            }
        }
    }

    private fun deleteCocktail() {
        binding.deleteButton.setOnClickListener {
            viewModel.cocktail.value?.let {
                viewModel.deleteCocktail(it)
                activity?.supportFragmentManager?.commit {
                    replace(R.id.mainContainer, MainFragment())
                }
            }
        }
    }

    private fun getBundle(): Int? {
        return arguments?.getInt(COCKTAIL_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}