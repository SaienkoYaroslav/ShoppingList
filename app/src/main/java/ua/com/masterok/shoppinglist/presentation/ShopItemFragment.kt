package ua.com.masterok.shoppinglist.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ua.com.masterok.shoppinglist.ListApp
import ua.com.masterok.shoppinglist.databinding.FragmentShopItemBinding
import ua.com.masterok.shoppinglist.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ListApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    // Якщо активіті в якій є фрагмент забов’язана реалізовувати певний інтерфейс, то об’єкт цього інтерфейсу
    // у фрагменті робиться не нулабельним і якщо активіті не реалізує інтерфейс, то кидається виключення
    private lateinit var onEditingFinishListener: OnEditingFinishListener

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        Log.d("ShopItemFragment", "onAttach")
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
        Log.d("ShopItemFragment", "onCreate")
        parseParams()
    }

    // В цьому методі з макета створюється Вью
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ShopItemFragment", "onCreateView")
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Викликається, коли вью створена. В цей метод прилітає вью яку ми створили в onCreateView()
    // Тут працюємо з кодом, так як у onCreate() в Активіті
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ShopItemFragment", "onViewCreated")
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeCloseScreen()
        addTextChangeListeners()
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

        binding.saveButton.setOnClickListener {
            val inputName = binding.etName.text?.toString()
            val inputCount = binding.etCount.text?.toString()
//            viewModel.editShopItem(inputName, inputCount)

            thread {
                context?.contentResolver?.update(
                    Uri.parse("content://ua.com.masterok.shoppinglist/shop_items"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", binding.etName.text?.toString())
                        put("count", binding.etCount.text?.toString()?.toInt())
                        put("enabled", true)
                    },
                    null,
                    arrayOf(shopItemId.toString())
                )
            }
        }

    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
//            val name = binding.etName.text?.toString()
//            val count = binding.etCount.text?.toString()
//            viewModel.addNewShopItem(name, count)
            thread {
                context?.contentResolver?.insert(
                    Uri.parse("content://ua.com.masterok.shoppinglist/shop_items"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", binding.etName.text?.toString())
                        put("count", binding.etCount.text?.toString()?.toInt())
                        put("enabled", true)
                    }
                )
            }
        }
    }

    private fun observeCloseScreen() {
        viewModel.closeScreen.observe(viewLifecycleOwner) {
            onEditingFinishListener.onEditingFinish()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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

    override fun onStart() {
        super.onStart()
        Log.d("ShopItemFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ShopItemFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ShopItemFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ShopItemFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("ShopItemFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShopItemFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("ShopItemFragment", "onDetach")
    }
}