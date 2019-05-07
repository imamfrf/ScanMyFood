package com.scanmyfood.imamf.scanmyfood.ui.RecommendationFragment

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.scanmyfood.imamf.scanmyfood.Model.CateringFood
import com.scanmyfood.imamf.scanmyfood.R
import com.scanmyfood.imamf.scanmyfood.util.Constant.DEFAULT.DEFAULT_NOT_SET

class RecommendationAdapter(
        val items: ArrayList<CateringFood>, val listener: RecommendationListener, val mContext: Context
) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_food, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textViewFoodName.text = item.namaMakanan
        holder.textViewCateringName.text = item.namaCatering
        holder.textViewCalorie.text = item.kaloriMakanan + " kal"
        holder.textViewHarga.text = "Rp" + item.harga
        val firstPhotoUrl = item.photo
        if (firstPhotoUrl == DEFAULT_NOT_SET) {
            Glide.with(mContext).load(R.drawable.default_image_not_set).into(holder.imageViewThumbnail)
        } else {
            Glide.with(mContext).load(firstPhotoUrl).into(holder.imageViewThumbnail)
        }
        holder.buttonBuy.setOnClickListener {
            listener.onBuyClick(item.idMakanan, item.nomorHp)
        }
        holder.detailButton.setOnClickListener {
            listener.onItemClick(item.idMakanan, item.namaMakanan, item.namaCatering, item.latitude, item.longitude, item.nomorHp)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        // MARK: - Public Properties
        val cardView: CardView
        val textViewFoodName: TextView
        val textViewCateringName: TextView
        val textViewCalorie: TextView
        val textViewHarga: TextView
        val imageViewThumbnail: ImageView
        val buttonBuy: Button
        val detailButton: Button

        // MARK: - Initialization
        init {
            cardView = itemView?.findViewById(R.id.cardView) as CardView
            textViewFoodName = itemView.findViewById(R.id.tv_history_home) as TextView
            textViewCateringName = itemView.findViewById(R.id.textViewCateringName) as TextView
            textViewCalorie = itemView.findViewById(R.id.textViewCalorie) as TextView
            textViewHarga = itemView.findViewById(R.id.textViewHarga) as TextView
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail) as ImageView
            buttonBuy = itemView.findViewById(R.id.buttonPesan) as Button
            detailButton = itemView.findViewById(R.id.buttonDetail) as Button
        }
    }
}