package ua.com.masterok.shoppinglist.presentation


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ua.com.masterok.shoppinglist.data.ShopListRepositoryImpl
import ua.com.masterok.shoppinglist.domain.EditItemUseCase
import ua.com.masterok.shoppinglist.domain.GetShopListUseCase
import ua.com.masterok.shoppinglist.domain.RemoveItemUseCase
import ua.com.masterok.shoppinglist.domain.ShopItem

// успадковуємся від ViewModel(), якщо далі нам не потрібно передавати контекст, якщо потрібно, то
// успадковуємось від AndroidViewModel() куди передаємо аплікейшн в якості контенту (AndroidViewModel(Application()))
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getSHopListUseCase = GetShopListUseCase(repository)
    private val removeItemUseCase = RemoveItemUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)

    val shopListLiveData = getSHopListUseCase.getShopList()


    fun removeItem(shopItem: ShopItem) {
        removeItemUseCase.removeItem(shopItem)
    }


    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editItemUseCase.editItem(newItem)
    }
}