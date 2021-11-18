package com.resurrection.popuptranslator.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.resurrection.popuptranslator.ui.base.BaseAdapter.*

open class BaseAdapter<T, viewDataBinding : ViewDataBinding>(
    @LayoutRes var layoutResource: Int,
    var currentList: MutableList<T>,
    var itemId: Int,
    var onItemClick: (T) -> Unit
) : RecyclerView.Adapter<BaseHolder<T>>(), Filterable {

    lateinit var binding: viewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutResource,
            parent,
            false
        )

        return BaseHolder(binding, itemId, onItemClick)
    }

    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        holder.bind(currentList[position])

    }

    override fun getItemCount(): Int = currentList.size

    fun updateList(newList: ArrayList<T>) {
        val diffCallBack = BaseDiffUtil<T>(currentList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(item: T) {
        val pos = getItemPosition(item)
        if (pos > -1) {
            currentList.remove(item)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, itemCount)
        }
    }

    fun addItem(item: T) {
        val pos = getItemPosition(item)
        if (pos > -1) {
            currentList.add(item)
            notifyItemInserted(pos)
            notifyItemRangeChanged(pos, itemCount)
        }
    }

    private fun getItemPosition(item: T): Int {
        if (currentList.isNotEmpty()) {
            var i = 0
            var pos = -1
            for (value in currentList) {
                if (item == value) {
                    pos = i
                    break
                }
                i++
            }
            return pos
        }
        return -1
    }

    class BaseHolder<T>(
        private var binding: ViewDataBinding,
        private var itemId: Int,
        var onItemClick: (T) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.setVariable(itemId, item)
            itemView.setOnClickListener { onItemClick((item)) }
        }
    }

    class BaseDiffUtil<T>(
        private val oldNumbers: List<T>,
        private val newNumbers: List<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldNumbers.size

        override fun getNewListSize(): Int = newNumbers.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldNumbers[oldItemPosition] == newNumbers[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldNumbers[oldItemPosition] == newNumbers[newItemPosition]

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<T>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(currentList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in currentList) {
                        if (item.toString().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                currentList.clear()
                currentList.addAll(results?.values as List<T>)
                notifyDataSetChanged()
            }
        }
    }
}

