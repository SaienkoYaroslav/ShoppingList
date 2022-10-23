package ua.com.masterok.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import ua.com.masterok.shoppinglist.domain.ShopItem

// В <> передається тип який будемо порівнювати
class ShopItemDiffCallback: DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}