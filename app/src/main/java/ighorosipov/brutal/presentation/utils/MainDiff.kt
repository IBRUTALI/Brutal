package ighorosipov.brutal.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import ighorosipov.brutal.domain.model.Cocktail

class MainDiff(
    private val oldList: List<Cocktail>,
    private val newList: List<Cocktail>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }

            oldList[oldItemPosition].image != newList[newItemPosition].image -> {
                false
            }

            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }

            oldList[oldItemPosition].description != newList[newItemPosition].description -> {
                false
            }

            oldList[oldItemPosition].recipe != newList[newItemPosition].recipe -> {
                false
            }

            oldList[oldItemPosition].ingredients != newList[newItemPosition].ingredients -> {
                false
            }

            else -> true
        }
    }
}