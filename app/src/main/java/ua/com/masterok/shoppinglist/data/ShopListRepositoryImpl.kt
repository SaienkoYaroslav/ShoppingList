package ua.com.masterok.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ua.com.masterok.shoppinglist.domain.ShopItem
import ua.com.masterok.shoppinglist.domain.ShopListRepository
import javax.inject.Inject

// Реалізація Репозиторія з Домейн шару
class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper
) : ShopListRepository {

    //private val shopListDao = AppDatabase.getInstance(application).shopListDao()


    override suspend fun addNewItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun removeItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getItemFromId(shopItemId: Int): ShopItem {
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