package com.harman.wearosapp.app.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harman.wearosapp.app.databinding.FeatureListItemBinding
import com.harman.wearosapp.domain.model.FeatureModel

class FeatureListAdapter(
    val onItemPressedCallBack: (featureId: Int) -> Unit
) : ListAdapter<FeatureModel, FeatureListAdapter.FeatureListViewHolder>(Companion) {


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
        val height = Resources.getSystem().displayMetrics.heightPixels

        holder.binding.btFeatureButton.apply {
            val params = this.layoutParams as ConstraintLayout.LayoutParams
            params.height = height/3
            textSize =
                (height / 18).toFloat() / Resources.getSystem().displayMetrics.scaledDensity
            layoutParams = params

        }



        val currentFeature = currentList[position]
        val layoutParams = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
        holder.binding.btFeatureButton.text = currentList[position].name
        holder.binding.btFeatureButton.setOnClickListener {
            onItemPressedCallBack(currentFeature.id)
        }
        layoutParams.updateMargins(top = height/50, bottom = height/50)
        val noItemOffset = height /2.8
        if (currentFeature.id == 0) {
            layoutParams.updateMargins(top = noItemOffset.toInt())
        }
        if (currentFeature.id == currentList.size - 1) {
            layoutParams.updateMargins(bottom = noItemOffset.toInt())
        }
    }
}