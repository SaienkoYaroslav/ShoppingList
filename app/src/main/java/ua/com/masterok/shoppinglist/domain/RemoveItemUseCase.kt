package ua.com.masterok.shoppinglist.domain

class RemoveItemUseCase(private val shopLIstRepository: ShopListRepository) {

    fun removeItem(shopItem: ShopItem) {
        shopLIstRepository.removeItem(shopItem)
    }

}