package ua.com.masterok.shoppinglist.domain

interface ShopLIstRepository {

    fun addNewItem(shopItem: ShopItem)

    fun removeItem(shopItem: ShopItem)

    fun editItem(shopItem: ShopItem)

    fun getItemFromId(shopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>

}