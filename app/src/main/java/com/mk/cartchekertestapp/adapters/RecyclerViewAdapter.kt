package com.mk.cartchekertestapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mk.cartchekertestapp.R
import com.mk.cartchekertestapp.models.RecentCallItem

class RecyclerViewAdapter(context: Context,calls :ArrayList<RecentCallItem>, private val onClickListener: OnCallClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder internal constructor(view: View) :RecyclerView.ViewHolder(view){
        val cartNumber = view.findViewById<TextView>(R.id.numberCartextViewItem)
        val bankName = view.findViewById<TextView>(R.id.bankTextViewItem)
    }
    interface OnCallClickListener {
        fun onCallClick(call: RecentCallItem?, position: Int)
    }
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var calls = calls
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = inflater.inflate(R.layout.call_item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return calls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCall = calls[position]
        holder.bankName.text=itemCall.bank
        holder.cartNumber.text=itemCall.number
        holder.itemView.setOnClickListener { v: View? ->
            onClickListener.onCallClick(
                itemCall,
                position
            )
        }

    }

}