package ua.com.masterok.shoppinglist.domain

class AddNewItemUseCase(private val shopLIstRepository: ShopLIstRepository) {

    fun addNewItem(shopItem: ShopItem) {
        shopLIstRepository.addNewItem(shopItem)
    }

}