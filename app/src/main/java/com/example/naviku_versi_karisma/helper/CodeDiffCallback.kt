package com.example.naviku_versi_karisma.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.naviku_versi_karisma.data.local.Code

class CodeDiffCallback(private val oldCodeList: List<Code>, private val newCodeList: List<Code>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldCodeList.size

    override fun getNewListSize(): Int = oldCodeList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCodeList[oldItemPosition].id == newCodeList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCode = oldCodeList[oldItemPosition]
        val newCode = newCodeList[newItemPosition]
        return oldCode.name == newCode.name
    }
}