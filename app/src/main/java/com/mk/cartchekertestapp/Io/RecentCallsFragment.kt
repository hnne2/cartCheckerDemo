package com.mk.cartchekertestapp.Io

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mk.cartchekertestapp.R
import com.mk.cartchekertestapp.adapters.RecyclerViewAdapter
import com.mk.cartchekertestapp.databinding.RecentCallsFragmentBinding
import com.mk.cartchekertestapp.models.RecentCallItem
import com.push700.io.PackageAppDataBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class RecentCallsFragment :Fragment() {

    lateinit var binding: RecentCallsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val database = PackageAppDataBase.getDatabase(requireContext())
        val cartsDao = database.cartsDao()
        binding = RecentCallsFragmentBinding.inflate(inflater,container,false)

        val view =binding.root
        val calls = arrayListOf(RecentCallItem(number = "тут пока", bank = "пусто"))

        val calsClickListener: RecyclerViewAdapter.OnCallClickListener = object :
            RecyclerViewAdapter.OnCallClickListener {
            override fun onCallClick(call: RecentCallItem?, position: Int) {
                // Проверяем, что call не равен null
                call?.let {
                    // Создаем новый экземпляр CartInfoFragment, передаем данные через Bundle
                    val fragment = CartInfoFragment.newInstance(
                        type = it.typeCart ?: "",
                        bankName = it.bank ?: "",
                        scheme = it.SchemeCart ?: "",
                        city = it.city ?: "",
                        country = it.country ?: "",
                        phone = it.phone ?: "",
                        url = it.url ?: "",
                        firstWindow = 0
                    )

                    // Заменяем фрагмент с передачей данных
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }



        }
        val adapter: RecyclerViewAdapter = RecyclerViewAdapter(requireContext(),calls, onClickListener = calsClickListener)
        CoroutineScope(Dispatchers.IO).launch {
            val callsInRoom = cartsDao.getAllPushes()
            val recyclerviewCalls:ArrayList<RecentCallItem> = ArrayList<RecentCallItem>()
            withContext(Dispatchers.Main){
                for (item in callsInRoom) {
                    recyclerviewCalls.add(
                        RecentCallItem(
                            number = item.number ?: "", // если item.number == null, то присваиваем пустую строку
                            typeCart = item.typeCart ?: "",
                            bank = item.bank ?: "",
                            SchemeCart = item.SchemeCart ?: "",
                            country = item.country ?: "",
                            url = item.url ?: "",
                            city = item.city ?: "",
                            phone = item.phone ?: ""
                        )
                    )
                }

                adapter.calls=recyclerviewCalls
                adapter.notifyDataSetChanged()
            }
        }
        binding.callsRecyclerVIew.layoutManager = LinearLayoutManager(requireContext())
        binding.callsRecyclerVIew.adapter=adapter
        return view
    }
}