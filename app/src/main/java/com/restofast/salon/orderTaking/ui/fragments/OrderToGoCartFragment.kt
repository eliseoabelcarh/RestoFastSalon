package com.restofast.salon.orderTaking.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.restofast.salon.R
import com.restofast.salon.databinding.OrderToGoCartFragmentBinding
import com.restofast.salon.orderTaking.ui.viewmodels.OrderToGoCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings


@AndroidEntryPoint
@WithFragmentBindings
class OrderToGoCartFragment : Fragment() {

    companion object {
        fun newInstance() = OrderToGoCartFragment()
    }

    private lateinit var viewModel: OrderToGoCartViewModel
    private lateinit var binding: OrderToGoCartFragmentBinding
    private var orderToGoIDEntrante : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OrderToGoCartFragmentBinding.inflate(layoutInflater)

       /* orderToGoIDEntrante = OrderToGoCartFragmentArgs.fromBundle(requireArguments()).orderToGoID*/
        (activity as AppCompatActivity).supportActionBar?.title = "Para Llevar"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderToGoCartViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        binding.orderToGoID.text = orderToGoIDEntrante

    }

}