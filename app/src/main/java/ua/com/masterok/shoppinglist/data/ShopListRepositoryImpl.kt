package ua.com.masterok.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.com.masterok.shoppinglist.domain.ShopItem
import ua.com.masterok.shoppinglist.domain.ShopListRepository

// Реалізація Репозиторія з Домейн шару
object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val item = ShopItem("Name$i", i, true)
            addNewItem(item)
        }
    }


    override fun addNewItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId
            autoIncrementId++
        }
        shopList.add(shopItem)
        updateListLD()
    }

    override fun removeItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateListLD()
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

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateListLD() {
        shopListLD.value = shopList.toList()
    }


}