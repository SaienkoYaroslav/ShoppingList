package ua.com.masterok.shoppinglist.domain

class GetSHopListUseCase(private val shopLIstRepository: ShopListRepository) {

    fun getShopList(): List<ShopItem> {
        return shopLIstRepository.getShopList()
    }

}