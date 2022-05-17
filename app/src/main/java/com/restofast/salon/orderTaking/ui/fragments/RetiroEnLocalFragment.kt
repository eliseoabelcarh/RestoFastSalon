package com.restofast.salon.orderTaking.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.restofast.salon.R
import com.restofast.salon.orderTaking.ui.viewmodels.RetiroEnLocalViewModel

class RetiroEnLocalFragment : Fragment() {

    companion object {
        fun newInstance() = RetiroEnLocalFragment()
    }

    private lateinit var viewModel: RetiroEnLocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.retiro_en_local_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RetiroEnLocalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}