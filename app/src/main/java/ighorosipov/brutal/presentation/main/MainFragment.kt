package ighorosipov.brutal.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import ighorosipov.brutal.R
import ighorosipov.brutal.databinding.FragmentMainBinding
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.presentation.COCKTAIL_ID
import ighorosipov.brutal.presentation.details.DetailsFragment
import ighorosipov.brutal.presentation.edit.EditFragment

class MainFragment : Fragment() {
    private var mBinding: FragmentMainBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by viewModels<MainViewModel>()
    private val adapter by lazy { MainAdapter() }

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
        addButtonClick()
    }

    private fun getCocktails() {
        viewModel.getCocktails()
    }

    private fun cocktailsObserver() {
        viewModel.cocktails.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                setupUi(View.VISIBLE)
            } else {
                setupUi(View.GONE)
                adapter.setList(list)
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
                (cocktail.id)?.let { bundle.putInt(COCKTAIL_ID, it) }
                openFragment(DetailsFragment(), bundle)
            }
        })
    }

    private fun addButtonClick() {
        binding.addButton.setOnClickListener {
            openFragment(EditFragment(), null)
        }
    }

    private fun openFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        activity?.supportFragmentManager?.commit {
            replace(R.id.mainContainer, fragment)
            setReorderingAllowed(true)
            addToBackStack("name")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}