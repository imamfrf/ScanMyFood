package com.scanmyfood.imamf.scanmyfood.ui.HomeFragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.scanmyfood.imamf.scanmyfood.Model.Food
import com.scanmyfood.imamf.scanmyfood.R

class HomeHistoryAdapter(private val listItems: List<Food>, val listener: homeHistoryListener, private val context: HomeFragment) : RecyclerView.Adapter<HomeHistoryAdapter.ViewHolder>() {
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_history, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = listItems[position]

        holder.tv_history_home?.text = item.name

        Glide.with(context).load(item.img).into(holder.img_thumbnail_home as ImageView)


//        holder.cardView.setOnClickListener {
//            listener.onItemClick(item.id, item.name)
//        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        // MARK: - Public Properties
       // val cardView: CardView
        val tv_history_home: TextView?
        val img_thumbnail_home: ImageView?


        // MARK: - Initialization
        init {
            tv_history_home = itemView?.findViewById(R.id.tv_history_home)
            img_thumbnail_home = itemView?.findViewById(R.id.img_thumbnail_home)
           // cardView = itemView?.findViewById(R.id.cardView) as CardView

        }
    }
}