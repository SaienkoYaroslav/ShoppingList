package ua.com.masterok.shoppinglist.data

import ua.com.masterok.shoppinglist.domain.ShopItem
import ua.com.masterok.shoppinglist.domain.ShopListRepository

// Реалізація Репозиторія з Домейн шару
object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0


    override fun addNewItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId
            autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun removeItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editItem(shopItem: ShopItem) {
        val oldElement = getItemFromId(shopItem.id)
        shopList.remove(oldElement)
        addNewItem(shopItem)
    }

    override fun getItemFromId(shopItemId: Int): ShopItem {
        // find - шукає елемент в колекції. Повертає нулабельний тип
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        // повертаєм копію Ліста
        return shopList.toList()
    }


}