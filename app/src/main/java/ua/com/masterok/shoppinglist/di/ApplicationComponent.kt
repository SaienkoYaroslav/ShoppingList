package ua.com.masterok.shoppinglist.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ua.com.masterok.shoppinglist.presentation.MainActivity
import ua.com.masterok.shoppinglist.presentation.ShopItemFragment

@ApplicationScope
@Component(modules = [ViewModelsModule::class, DataModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

}