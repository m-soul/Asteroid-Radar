package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.RecyclerItemBinding

class RecyclerViewAdapter(val clickListener: AsteroidListener) : ListAdapter<Asteroid, RecyclerViewAdapter.AsteroidViewHolder>(DiffCallback)
{
    class AsteroidViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, clickListener : AsteroidListener)
        {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>()
    {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid,clickListener)
    }
    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}