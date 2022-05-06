package com.harman.wearosapp.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harman.wearosapp.app.databinding.FeatureListItemBinding
import com.harman.wearosapp.domain.model.FeatureModel

class FeatureListAdapter(
    val onItemPressedCallBack: (featureId: Int) -> Unit
) : ListAdapter<FeatureModel, FeatureListAdapter.FeatureListViewHolder>(Companion) {
    private val noItemOffset = 110

    class FeatureListViewHolder(val binding: FeatureListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<FeatureModel>() {
        override fun areItemsTheSame(oldItem: FeatureModel, newItem: FeatureModel) =
            oldItem == newItem


        override fun areContentsTheSame(oldItem: FeatureModel, newItem: FeatureModel) =
            newItem.id == oldItem.id && newItem.name == newItem.name

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureListViewHolder =
        FeatureListViewHolder(
            FeatureListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: FeatureListViewHolder, position: Int) {
        val currentFeature = currentList[position]
        val layoutParams = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
        holder.binding.btFeatureButton.text = currentList[position].name
        holder.binding.btFeatureButton.setOnClickListener {
            onItemPressedCallBack(currentFeature.id)
        }
        if (currentFeature.id == 0) {
            layoutParams.updateMargins(top = noItemOffset)
        }
        if (currentFeature.id == currentList.size - 1) {
            layoutParams.updateMargins(bottom = noItemOffset)
        }
    }
}