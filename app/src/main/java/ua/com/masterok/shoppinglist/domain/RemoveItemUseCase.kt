package ua.com.masterok.shoppinglist.domain

class RemoveItemUseCase(private val shopLIstRepository: ShopLIstRepository) {

    fun removeItem(shopItem: ShopItem) {
        shopLIstRepository.removeItem(shopItem)
    }

}