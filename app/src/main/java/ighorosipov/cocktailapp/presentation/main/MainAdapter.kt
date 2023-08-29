package ighorosipov.cocktailapp.presentation.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.ItemCocktailBinding
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.presentation.utils.MainDiff

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
            itemTitle.text = cocktailList[position].name
            Glide.with(holder.itemView.context)
                .load(cocktailList[position].image?.let { Uri.parse(it) })
                .centerCrop()
                .placeholder(R.drawable.cocktail)
                .into(itemImage)
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