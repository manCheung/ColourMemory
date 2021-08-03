package com.man.colourmemory.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.man.colourmemory.R
import com.man.colourmemory.databinding.CellScoreItemBinding
import com.man.colourmemory.model.ScoreRecordItem
import com.man.colourmemory.viewModel.ScoresViewModel

class ScoreRecordAdapter(private val viewModel: ScoresViewModel) :
    RecyclerView.Adapter<ScoreRecordAdapter.ViewHolder>() {

    var list: List<ScoreRecordItem>? = viewModel.scoreRecordList.value

    fun setListRecord(listValue: List<ScoreRecordItem>){
        list = listValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list!![position]
        holder.bind(viewModel, item)
    }

    override fun getItemCount(): Int {
        return list?.count() ?: 0
    }

    class ViewHolder private constructor(private val binding: CellScoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ScoresViewModel, item: ScoreRecordItem) {

            binding.scoresViewModel = viewModel
            binding.scoreRecordItem = item

            when(item.rank){
                1 -> {
                    binding.avatarIcon.setBackgroundResource(R.drawable.no1)
                    binding.cardViewScoreRankItem.setCardBackgroundColor(Color.parseColor("#FFD700"))
                }
                2 -> {
                    binding.avatarIcon.setBackgroundResource(R.drawable.no2)
                    binding.cardViewScoreRankItem.setCardBackgroundColor(Color.parseColor("#C0C0C0"))
                }
                3 -> {
                    binding.avatarIcon.setBackgroundResource(R.drawable.no3)
                    binding.cardViewScoreRankItem.setCardBackgroundColor(Color.parseColor("#b87333"))
                }
                else -> {
                    binding.avatarIcon.setBackgroundResource(R.drawable.no4)
                    binding.cardViewScoreRankItem.setCardBackgroundColor(Color.parseColor("#F8F8F8"))
                }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CellScoreItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}