package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecycleAdapter(val clickListener: AsteroidListener) : androidx.recyclerview.widget.ListAdapter<DataItem,
        RecyclerView.ViewHolder>(AsteroidDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val astroid = getItem(position)
                holder.bind(clickListener, astroid.astroid)
            }
        }    }
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addList(list: List<Asteroid>?) {
        adapterScope.launch {
            val items = list?.map { DataItem.AstroidItem(it)  }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


     class ViewHolder private constructor(val binding:ListItemAsteroidBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind( listener: AsteroidListener,item: Asteroid) {
            binding.asteroid = item
            binding.clickListener=listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}

class AsteroidDiffCallback : DiffUtil.ItemCallback<DataItem>(){
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.astroid.id==newItem.astroid.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem==newItem
    }

}
class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
sealed class DataItem {
    data class AstroidItem(val asteroid: Asteroid): DataItem() {
        override val astroid = asteroid
    }


    abstract val astroid: Asteroid

}
