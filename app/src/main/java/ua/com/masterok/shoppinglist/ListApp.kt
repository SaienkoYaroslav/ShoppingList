package ua.com.masterok.shoppinglist

import android.app.Application
import ua.com.masterok.shoppinglist.di.DaggerApplicationComponent

class ListApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

}