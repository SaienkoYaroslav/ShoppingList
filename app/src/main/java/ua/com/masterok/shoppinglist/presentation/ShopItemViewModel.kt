package ua.com.masterok.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.masterok.shoppinglist.data.ShopListRepositoryImpl
import ua.com.masterok.shoppinglist.domain.AddNewItemUseCase
import ua.com.masterok.shoppinglist.domain.EditItemUseCase
import ua.com.masterok.shoppinglist.domain.GetItemFromIdUseCase
import ua.com.masterok.shoppinglist.domain.ShopItem

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShopListRepositoryImpl = ShopListRepositoryImpl(application)

    private val addNewItemUseCase = AddNewItemUseCase(repository)
    private val getItemFromIdUseCase = GetItemFromIdUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)

    // робота з ЛД в котліні. У ВМ працюємо зі змінною _errorInputName, а в Активіті викликаємо гетер
    // errorInputName
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
    get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
    get() = _shopItem

    // можна використати тип <Boolean>, але в котлін прийнято, якщо з типом який передається в ЛД
    // активіті не буде працювати, а ми просто щосб сповіщаємо, то тип робиться UNIT
    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit>
    get() = _closeScreen



    fun getShopItem(shopItemId: Int) {
        val item = getItemFromIdUseCase.getItemFromId(shopItemId)
        _shopItem.value = item
    }

    fun addNewShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            val shopItem = ShopItem(name, count, true)
            addNewItemUseCase.addNewItem(shopItem)
            finishWork()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editItemUseCase.editItem(item)
                finishWork()
            }
        }
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    private fun finishWork() {
        _closeScreen.value = Unit
    }

}