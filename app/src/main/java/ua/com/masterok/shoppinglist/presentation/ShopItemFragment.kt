package ua.com.masterok.shoppinglist.presentation

import android.content.Context
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

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    // Якщо активіті в якій є фрагмент забов’язана реалізовувати певний інтерфейс, то об’єкт цього інтерфейсу
    // у фрагменті робиться не нулабельним і якщо активіті не реалізує інтерфейс, то кидається виключення
    private lateinit var onEditingFinishListener: OnEditingFinishListener

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishListener) {
            onEditingFinishListener = context
        } else {
            // кидається виключення
            throw RuntimeException("Activity must implement OnEditingFinishListener")
        }
    }

    // зазвичай перевірки у фрагментах запускають в цьому методі, щоб якщо чогось необхідного немає
    // програма відразу падала і не виконувався подальший код
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

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
            onEditingFinishListener.onEditingFinish()
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
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
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

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            // так передаємо параметри у фрагмент
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

    interface OnEditingFinishListener {
        fun onEditingFinish()
    }

}