package com.words.storageapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.words.storageapp.database.model.MiniWokrData
import com.words.storageapp.databinding.ResultListCardBinding

class ResultListAdapter(private val listener: ClickListener) :
    ListAdapter<MiniWokrData, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //val inflater = LayoutInflater.from(parent.context)
        return SkillsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SkillsViewHolder -> {
                val data = getItem(position)
                holder.bind(data, listener)
            }
        }
    }

    class SkillsViewHolder(private val binding: ResultListCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            //setting up onClickListener for the search Result
//            binding.setClickListener {
//                binding.skills?.let { skill ->
//                    navToDetailFragment(skill,it)
//                }
//            }
//        }

//        fun navToDetailFragment(skill: MiniWokrData, view: View){
//            val direction = FragmentD
//            view.findNavController().navigate(direction)
//        }

        fun bind(data: MiniWokrData, listener: ClickListener) {
            binding.skills = data
            binding.clickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SkillsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return SkillsViewHolder(ResultListCardBinding.inflate(inflater, parent, false))
            }
        }

    }

    class ClickListener(val listener: (skills: MiniWokrData) -> Unit) {
        fun onClick(skill: MiniWokrData) = listener(skill)
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<MiniWokrData>() {
            override fun areItemsTheSame(oldItem: MiniWokrData, newItem: MiniWokrData): Boolean {
                return oldItem.userId == oldItem.userId
            }

            override fun areContentsTheSame(oldItem: MiniWokrData, newItem: MiniWokrData): Boolean {
                return oldItem == newItem
            }

        }
    }
}