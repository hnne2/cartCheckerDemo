package com.mk.cartchekertestapp.Io

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mk.cartchekertestapp.ViewModel.MainViewModel
import com.mk.cartchekertestapp.R
import com.mk.cartchekertestapp.databinding.CartInfoFragmentBinding
import com.mk.cartchekertestapp.models.CartInfoCallBody
import com.push700.io.CartsDao
import com.push700.io.PackageAppDataBase
import com.push700.io.cartsInRoom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CartInfoFragment: Fragment() {
    val viewModel: MainViewModel by viewModels()

    companion object {
        fun newInstance(
            type: String = "",
            bankName: String = "",
            scheme: String = "",
            city: String = "",
            country: String = "",
            phone: String = "",
            url: String = "",
            firstWindow:Int
        ): CartInfoFragment {
            val fragment = CartInfoFragment()
            val args = Bundle().apply {
                putString("type", type)
                putString("bank_name", bankName)
                putString("scheme", scheme)
                putString("city", city)
                putString("country", country)
                putString("phone", phone)
                putString("url", url)
                putInt("firstWindow", firstWindow)
            }
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding: CartInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CartInfoFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val type = arguments?.getString("type", "") ?: ""
        val bankName = arguments?.getString("bank_name", "") ?: ""
        val scheme = arguments?.getString("scheme", "") ?: ""
        val city = arguments?.getString("city", "") ?: ""
        val country = arguments?.getString("country", "") ?: ""
        val phone = arguments?.getString("phone", "") ?: ""
        val url = arguments?.getString("url", "") ?: ""
        val firstWindow = arguments?.getInt("firstWindow", 0) ?: ""

        if (firstWindow==0){
            binding.cartNumberEditTextText.visibility = View.GONE
            binding.recentCallImageButton.visibility = View.GONE
        }
        binding.TypeCartTextView.text = "Type: $type"
        binding.BankTextView.text = "Bank: $bankName"
        binding.SchemeCartTextView.text = "Scheme / network: $scheme"
        binding.cityTextView.text = "City: $city"
        binding.countryTextView.text = "Country: $country"
        binding.phoneTextVIew.text = "Phone: $phone"
        binding.urlTextVIew.text = "Url: $url"

        val database = PackageAppDataBase.getDatabase(requireContext())
        val cartsDao = database.cartsDao()

        binding.cartNumberEditTextText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputText = binding.cartNumberEditTextText.text.toString()
                if (inputText.length < 8) {
                    Toast.makeText(requireContext(), "Введите более 8 цифр", Toast.LENGTH_SHORT).show()
                } else {
                    val first8Digits = inputText.filter { it.isDigit() }.take(8)
                    viewModel.getCartInfo(first8Digits)
                }
                true
            } else {
                false
            }
        }

        viewModel.mutableCartInfo.observe(viewLifecycleOwner) { cartInfo ->
            if (cartInfo != null) {
                binding.TypeCartTextView.text = "Type: ${cartInfo.type}"
                binding.BankTextView.text = "Bank: ${cartInfo.bank.name}"
                binding.SchemeCartTextView.text = "Scheme / network: ${cartInfo.scheme}"
                binding.cityTextView.text = "City: ${cartInfo.bank.city}"
                binding.countryTextView.text = "Country: ${cartInfo.country.name}"
                binding.phoneTextVIew.text = "Phone: ${cartInfo.bank.phone}"
                binding.urlTextVIew.text = "Url: ${cartInfo.bank.url}"
                val inputText = binding.cartNumberEditTextText.text.toString()
                val first8Digits = inputText.filter { it.isDigit() }.take(8)
                addToBase(cartInfo, first8Digits, cartsDao)
            }
        }

        binding.recentCallImageButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main, RecentCallsFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    fun addToBase(cartInfo: CartInfoCallBody, number: String, cartsDao: CartsDao) {
        CoroutineScope(Dispatchers.IO).launch {
            cartsDao.insert(
                cartsInRoom(
                    typeCart = cartInfo.type,
                    bank = cartInfo.bank.name,
                    SchemeCart = cartInfo.scheme,
                    city = cartInfo.bank.city,
                    country = cartInfo.country.name,
                    phone = cartInfo.bank.phone,
                    url = cartInfo.bank.url,
                    number = number
                )
            )
        }
    }
}
