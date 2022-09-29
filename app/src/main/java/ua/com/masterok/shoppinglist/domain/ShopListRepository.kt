package ua.com.masterok.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addNewItem(shopItem: ShopItem)

    fun removeItem(shopItem: ShopItem)

    fun editItem(shopItem: ShopItem)

    fun getItemFromId(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

}