package ighorosipov.cocktailapp.presentation.details

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.FragmentDetailsBinding
import ighorosipov.cocktailapp.presentation.BUNDLE_COCKTAIL
import ighorosipov.cocktailapp.presentation.BUNDLE_COCKTAIL_ID
import ighorosipov.cocktailapp.presentation.utils.extensions.appComponent
import ighorosipov.cocktailapp.presentation.utils.extensions.lazyViewModel
import ighorosipov.cocktailapp.presentation.utils.extensions.showAlert

class DetailsFragment : Fragment() {
    private var mBinding: FragmentDetailsBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by lazyViewModel {
        requireContext().appComponent().detailsViewModel().create()
    }

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
        subscribeOnCocktailDetail()
        subscribeOnLoading()
        editButton()
        deleteButton()
    }

    private fun findCocktail() {
        val id = getBundle()!!
        viewModel.findCocktailById(id)
    }

    private fun subscribeOnLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                findNavController().navigate(R.id.action_detailsFragment_to_mainFragment)
        }
    }

    private fun subscribeOnCocktailDetail() {
        viewModel.cocktail.observe(viewLifecycleOwner) { cocktail ->
            binding.apply {
                cocktail.data?.let {
                    titleText.text = it.name
                    descriptionText.text = it.description
                    ingredientsText.text = TextUtils.join("\n-\n", it.ingredients)
                    recipeText.text = it.recipe
                }
            }
        }
    }

    private fun editButton() {
        binding.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_COCKTAIL, viewModel.cocktail.value?.data)
            findNavController().navigate(R.id.action_detailsFragment_to_editFragment, bundle)
        }
    }

    private fun deleteButton() {
        binding.deleteButton.setOnClickListener {
            viewModel.cocktail.value?.data?.let {
                showAlert(
                    title = getString(R.string.confirm_action),
                    message = getString(R.string.dialog_delete_the_recipe)
                ) {
                    viewModel.deleteCocktail(it)
                }
            }
        }
    }

    private fun getBundle(): Int? {
        return arguments?.getInt(BUNDLE_COCKTAIL_ID)
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