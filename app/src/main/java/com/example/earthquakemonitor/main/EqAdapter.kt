package com.example.earthquakemonitor.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquakemonitor.Earthquake
import com.example.earthquakemonitor.databinding.EqListItemBinding

private val TAG = EqAdapter::class.java.simpleName

class EqAdapter: ListAdapter<Earthquake, EqAdapter.EqViewHolder>(DiffCallBack) {

    companion object DiffCallBack: DiffUtil.ItemCallback<Earthquake>() {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: (Earthquake) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqAdapter.EqViewHolder{
        val binding = EqListItemBinding.inflate(LayoutInflater.from(parent.context))
        return EqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EqAdapter.EqViewHolder, position: Int) {
        val earthquake: Earthquake = getItem(position)
        holder.bind(earthquake)
    }

    inner class EqViewHolder(private val binding: EqListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(earthquake: Earthquake){
            binding.eqPlaceText.text = earthquake.place
            binding.eqMagnitudeText.text = earthquake.magnitude.toString()
            binding.root.setOnClickListener {
                if(::onItemClickListener.isInitialized)
                    onItemClickListener(earthquake)
                else
                    Log.d(TAG, "onItemClickListener not initialized")
            }
            binding.executePendingBindings()
        }
    }
}