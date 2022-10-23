package ua.com.masterok.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.com.masterok.shoppinglist.R
import ua.com.masterok.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            // краща альтернатива notifyDataSetChanged()
            // старий список це той який тут був, а новий той який хочемо встановити
            val callback = ShopListDiffCallback(shopList, value)
            val difResult = DiffUtil.calculateDiff(callback)
            difResult.dispatchUpdatesTo(this)
            field = value
        }

    //     interface OnShopItemLongClickListener {
    //        fun onShopItemLongClick(shopItem: ShopItem)
    //    }
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            // .invoke - дозволяє викликати метод, якщо він != нал, якби тип onShopItemLongClickListener
            // ну був би нулабельним, то можна було обійтись без методу .invoke
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) {
            VIEW_ENABLED
        } else {
            VIEW_DISABLED
        }

    }

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }


    companion object {

        const val VIEW_ENABLED = R.layout.item_shop_enabled
        const val VIEW_DISABLED = R.layout.item_shop_disabled
        const val MAX_POOL_SIZE = 30

    }

}