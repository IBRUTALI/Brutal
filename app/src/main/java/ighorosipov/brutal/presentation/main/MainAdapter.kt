package ighorosipov.brutal.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ighorosipov.brutal.R
import ighorosipov.brutal.databinding.ItemCocktailBinding
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.presentation.utils.MainDiff

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var cocktailList = emptyList<Cocktail>()

    class MainViewHolder(val binding: ItemCocktailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder.binding) {
            itemImage.setImageResource(R.drawable.cocktail)
            itemTitle.text = cocktailList[position].title
        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener?.onClick(position, cocktailList[position])
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, cocktail: Cocktail)
    }

    fun setList(newList: List<Cocktail>) {
        val diffUtil = MainDiff(cocktailList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
        cocktailList = newList
    }


    override fun getItemCount(): Int {
        return cocktailList.size
    }


}