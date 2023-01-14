package ua.com.masterok.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import ua.com.masterok.shoppinglist.domain.ShopItem
import ua.com.masterok.shoppinglist.domain.ShopListRepository

// Реалізація Репозиторія з Домейн шару
class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    override fun addNewItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun removeItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override fun editItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun getItemFromId(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    // перетворення типу лайвДати, під капотом використовується МедіаторЛайвДата
    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }


}