package ighorosipov.cocktailapp.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
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
        setBottomAppBarBackground()
        setupAdapter()
        getCocktails()
        cocktailsObserver()
        itemClickListener()
        addButton()
        shareButton()
    }

    private fun getCocktails() {
        viewModel.getCocktails()
    }

    private fun cocktailsObserver() {
        viewModel.cocktails.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        setupUi(View.GONE)
                        noCocktailSetupUi(View.VISIBLE)
                        binding.loadingProgressBar.visibility = View.GONE
                    } else {
                        setupUi(View.VISIBLE)
                        noCocktailSetupUi(View.GONE)
                        binding.loadingProgressBar.visibility = View.GONE
                        adapter.setList(result.data)
                    }
                }
                is Result.Error -> {
                    showToast(result.message ?: "Something went wrong",  false)
                    binding.loadingProgressBar.visibility = View.GONE
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
            shareImageView.visibility = visibility
            mainRecycler.visibility = visibility
        }
    }

    private fun noCocktailSetupUi(visibility: Int) {
        binding.apply {
            noCocktailsGroup.visibility = visibility
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

    private fun shareButton() {
        binding.shareImageView.setOnClickListener {
            onShareClick()
        }
    }

    private fun setBottomAppBarBackground() {
        val radius = resources.getDimension(R.dimen.bottomappbar_corners_radius)
        val bottomBarBackground = binding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel =
            bottomBarBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius).build()
    }

    private fun onShareClick() {
        val cocktails = viewModel.cocktails.value?.data?.reversed()?.take(4) ?: return
        val shareText = generateShareText(cocktails.map { it.name })

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun generateShareText(cocktails: List<String>): String {
        val displayedCocktails = cocktails.joinToString(", ")

        return getString(R.string.share_message, displayedCocktails)
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