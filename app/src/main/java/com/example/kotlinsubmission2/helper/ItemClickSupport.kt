package com.example.kotlinsubmission2.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsubmission2.R

class ItemClickSupport(private var recyclerView: RecyclerView) {
    private var onItemClickListener: OnItemClickListener? = null

    init {
        recyclerView.setTag(R.id.item_click_support, this)
        recyclerView.addOnChildAttachStateChangeListener(attachListener())
    }

    companion object{
        fun addTo(recyclerView: RecyclerView) : ItemClickSupport{
            var support : ItemClickSupport? = recyclerView.getTag(R.id.item_click_support) as? ItemClickSupport

            if(support == null){
                support = ItemClickSupport(recyclerView)
            }
            return support
        }
    }

    private fun attachListener() : RecyclerView.OnChildAttachStateChangeListener = object :RecyclerView.OnChildAttachStateChangeListener{
        override fun onChildViewDetachedFromWindow(view: View) {
            return
        }

        override fun onChildViewAttachedToWindow(view: View) {
            if(onItemClickListener != null){
                view.setOnClickListener(onClickListener())
            }
        }

    }

    fun onClickListener() : View.OnClickListener = View.OnClickListener { v ->
        if(onItemClickListener != null){
            val holder : RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
            onItemClickListener?.onItemClicked(recyclerView, holder.adapterPosition,v)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClicked(recyclerView: RecyclerView, position:Int, v:View)
    }
}