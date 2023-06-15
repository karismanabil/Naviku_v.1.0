package com.example.naviku_versi_karisma.ui.kodeku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.naviku_versi_karisma.data.model.Category
import com.example.naviku_versi_karisma.R
import com.example.naviku_versi_karisma.databinding.ItemRowCategoryBinding

class ListCategoryAdapter(private val listCategory: ArrayList<Category>) : RecyclerView.Adapter<ListCategoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, icon) = listCategory[position]
        holder.binding.tvItemName.text = name
        holder.binding.imgItemPhoto.setImageResource(icon)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listCategory[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(category: Category)
    }
}