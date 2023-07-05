package com.example.naviku_versi_karisma.ui.kode_pribadi

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.naviku_versi_karisma.data.local.Code
import com.example.naviku_versi_karisma.databinding.ItemRowCodeBinding
import com.example.naviku_versi_karisma.helper.CodeDiffCallback
import com.example.naviku_versi_karisma.ui.detail_kode_pribadi.PersonalCodeDetailActivity

class CodeAdapter : RecyclerView.Adapter<CodeAdapter.CodeViewHolder>() {

    private val listCodes = ArrayList<Code>()

    fun setListCodes(listCodes: List<Code>) {
        val diffCallback = CodeDiffCallback(this.listCodes, listCodes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listCodes.clear()
        this.listCodes.addAll(listCodes)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CodeViewHolder(private val binding: ItemRowCodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(code: Code) {
            with(binding) {
                tvItemName.text = code.name
                cvItemCode.setOnClickListener {
                    val intent = Intent(it.context, PersonalCodeDetailActivity::class.java)
                    intent.putExtra(PersonalCodeDetailActivity.EXTRA_CODE, code)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeViewHolder {
        val binding = ItemRowCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CodeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCodes.size
    }

    override fun onBindViewHolder(holder: CodeViewHolder, position: Int) {
        holder.bind(listCodes[position])
    }
}