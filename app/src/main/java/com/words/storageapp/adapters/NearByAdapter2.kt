package com.words.storageapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.words.storageapp.database.model.MiniWokrData
import com.words.storageapp.databinding.ItemNearbyListBinding
import com.words.storageapp.databinding.ResultListCardBinding
import com.words.storageapp.domain.NearBySkill


class NearByAdapter2(val clickListener: ClickListener) :
    ListAdapter<NearBySkill, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SkillsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SkillsViewHolder -> {
                val data = getItem(position)
                holder.bind(data, clickListener)
            }
        }
    }

    class SkillsViewHolder(private val binding: ItemNearbyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nearBySkill: NearBySkill, clickListener: ClickListener) {
            binding.skill = nearBySkill
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SkillsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return SkillsViewHolder(ItemNearbyListBinding.inflate(inflater, parent, false))
            }
        }

    }

    class ClickListener(val listener: (skills: NearBySkill) -> Unit) {
        fun onClick(skill: NearBySkill) = listener(skill)
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<NearBySkill>() {
            override fun areItemsTheSame(oldItem: NearBySkill, newItem: NearBySkill): Boolean {
                return oldItem.skillId == oldItem.skillId
            }

            override fun areContentsTheSame(oldItem: NearBySkill, newItem: NearBySkill): Boolean {
                return oldItem == newItem
            }

        }
    }
}