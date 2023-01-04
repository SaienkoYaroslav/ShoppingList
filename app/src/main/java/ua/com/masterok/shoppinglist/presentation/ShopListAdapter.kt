package ua.com.masterok.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import ua.com.masterok.shoppinglist.R
import ua.com.masterok.shoppinglist.databinding.ItemShopDisabledBinding
import ua.com.masterok.shoppinglist.databinding.ItemShopEnabledBinding
import ua.com.masterok.shoppinglist.domain.ShopItem

// успадкування від ListAdapter для оптимізації ShopItemDiffCallback, який в свою чергу оптимізує і
// заміняє метод notifyDataSetChanged()
// ListAdapter - реалізує всю логіку роботи зі списками, тому багато звчиного відсутнє
// В <> передаємо 2 параметри. 1 - тип з яким працюємо. 2 - тип вьюхолдера і в конструктор класу
// ListAdapter передаємо екземпляр класу Колбек
class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {


    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        // getItem() - метод з ListAdapter
        val shopItem = getItem(position)
        val binding = holder.binding
        when(binding) {
            is ItemShopDisabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
            is ItemShopEnabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }

        binding.root.setOnLongClickListener {
            // .invoke - дозволяє викликати метод, якщо він != нал, якби тип onShopItemLongClickListener
            // ну був би нулабельним, то можна було обійтись без методу .invoke
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    // Непотрібний метод при, якщо успадкування від ListAdapter
//    override fun getItemCount(): Int {
//
//    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            VIEW_ENABLED
        } else {
            VIEW_DISABLED
        }

    }

    companion object {

        const val VIEW_ENABLED = R.layout.item_shop_enabled
        const val VIEW_DISABLED = R.layout.item_shop_disabled
        const val MAX_POOL_SIZE = 30

    }

}