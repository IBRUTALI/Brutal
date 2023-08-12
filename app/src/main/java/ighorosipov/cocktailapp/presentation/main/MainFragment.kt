package ighorosipov.cocktailapp.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.FragmentMainBinding
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.utils.Result
import ighorosipov.cocktailapp.presentation.BUNDLE_COCKTAIL_ID
import ighorosipov.cocktailapp.presentation.utils.extensions.appComponent
import ighorosipov.cocktailapp.presentation.utils.extensions.lazyViewModel
import ighorosipov.cocktailapp.presentation.utils.extensions.showToast

class MainFragment : Fragment() {
    private var mBinding: FragmentMainBinding? = null
    private val binding get() = mBinding!!
    private val adapter by lazy { MainAdapter() }
    private val viewModel: MainViewModel by lazyViewModel {
        requireContext().appComponent().mainViewModel().create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getCocktails()
        cocktailsObserver()
        itemClickListener()
        addButton()
    }

    private fun getCocktails() {
        viewModel.getCocktails()
    }

    private fun cocktailsObserver() {
        viewModel.cocktails.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        setupUi(View.VISIBLE)
                    } else {
                        setupUi(View.GONE)
                        adapter.setList(result.data)
                    }
                }
                is Result.Error -> {
                    showToast(result.message ?: "Something went wrong",  false)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.mainRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.mainRecycler.adapter = adapter
    }

    private fun setupUi(visibility: Int) {
        binding.apply {
            mainTitle.visibility = if (visibility == View.VISIBLE) {
                View.GONE
            } else View.VISIBLE
            mainImage.visibility = visibility
            emptyTitle.visibility = visibility
            emptyDescription.visibility = visibility
            emptyArrow.visibility = visibility
        }
    }

    private fun itemClickListener() {
        adapter.setOnClickListener(object : MainAdapter.OnClickListener {
            override fun onClick(position: Int, cocktail: Cocktail) {
                val bundle = Bundle()
                (cocktail.id)?.let {
                    bundle.putInt(BUNDLE_COCKTAIL_ID, it)
                    findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
                }
            }
        })
    }

    private fun addButton() {
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editFragment)
        }
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