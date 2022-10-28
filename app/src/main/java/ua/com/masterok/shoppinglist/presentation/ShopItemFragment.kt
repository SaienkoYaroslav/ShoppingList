package ua.com.masterok.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ua.com.masterok.shoppinglist.R
import ua.com.masterok.shoppinglist.domain.ShopItem

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button


    // В цьому методі з макета створюється Вью
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    // Викликається, коли вью створена. В цей метод прилітає вью яку ми створили в onCreateView()
    // Тут працюємо з кодом, так як у onCreate() в Активіті
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        observeCloseScreen()
        observeErrorInputName()
        observeErrorInputCount()
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {

        viewModel.getShopItem(shopItemId)
        // у фрагментах параметром в обсервер потрібно передавати не зіс, як в активіті, viewLifecycleOwner
        // тому що у втб і у фрагмента різний життєвий цикл і може бути таке, що вью вже не має, а
        // фрагмент ще існує, тоді буде краш.
        // viewLifecycleOwner - використовується лайф цикл вью
        viewModel.shopItem.observe(viewLifecycleOwner) {
            val (name, count) = it
            etName.setText(name)
            etCount.setText(count.toString())
        }

        buttonSave.setOnClickListener {
            val inputName = etName.text?.toString()
            val inputCount = etCount.text?.toString()
            viewModel.editShopItem(inputName, inputCount)
        }

    }

    private fun launchAddMode() {

        buttonSave.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            viewModel.addNewShopItem(name, count)
        }

    }

    private fun observeCloseScreen() {
        viewModel.closeScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun observeErrorInputName() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it == true) {
                tilName.error = getString(R.string.error_name)
            } else {
                tilName.error = null
            }
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.resetErrorInputName()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    private fun observeErrorInputCount() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it == true) {
                tilCount.error = getString(R.string.error_count)
            } else {
                tilCount.error = null
            }
            etCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.resetErrorInputCount()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun parseParams() {
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Param screen mode is absent")
        }
        if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNDEFINED_ID) {
            throw RuntimeException("Param shop item id is absent")
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }

    }
}