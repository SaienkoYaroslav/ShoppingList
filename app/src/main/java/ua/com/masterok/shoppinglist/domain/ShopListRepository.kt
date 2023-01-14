package ua.com.masterok.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    suspend fun addNewItem(shopItem: ShopItem)

    suspend fun removeItem(shopItem: ShopItem)

    suspend fun editItem(shopItem: ShopItem)

    suspend fun getItemFromId(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

}