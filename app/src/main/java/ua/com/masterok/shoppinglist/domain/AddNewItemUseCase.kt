package ua.com.masterok.shoppinglist.domain

class AddNewItemUseCase(private val shopLIstRepository: ShopListRepository) {

    fun addNewItem(shopItem: ShopItem) {
        shopLIstRepository.addNewItem(shopItem)
    }

}