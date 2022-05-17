package com.restofast.salon.orderTaking.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.databinding.ParaLlevarFragmentBinding
import com.restofast.salon.entities.order.PedidoToGo
import com.restofast.salon.orderTaking.ui.adapters.PedidosToGoListAdapter
import com.restofast.salon.orderTaking.ui.viewmodels.ParaLlevarViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings


@AndroidEntryPoint
@WithFragmentBindings
class ParaLlevarFragment : Fragment(), PedidosToGoListAdapter.OnClickListener {

    companion object {
        fun newInstance() = ParaLlevarFragment()
    }

    private lateinit var viewModel: ParaLlevarViewModel
    private lateinit var binding: ParaLlevarFragmentBinding

    // ELEMENTOS DE MENU
    lateinit var optionsMenu: Menu
    lateinit var itemAddOrderToGoIcon: MenuItem

    private lateinit var containerAddOrderToGo: LinearLayout

    //RECYCLER DE movimientos
    lateinit var recyclerViewOrdersToGo: RecyclerView
    private lateinit var linearLayoutManagerOrdersToGo: LinearLayoutManager
    private lateinit var pedidosToGoListAdapter: PedidosToGoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        setHasOptionsMenu(true)
        binding = ParaLlevarFragmentBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Para Llevar"
        recyclerViewOrdersToGo = binding.recyclerActiveOrdersToGo
        containerAddOrderToGo = binding.containerAddOrderToGo
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ParaLlevarViewModel::class.java)
    }

    // ------- INICIO DE MENU ICONS----- copiar y pegar desde acá
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        activity?.menuInflater?.inflate(R.menu.menu_para_llevar, menu)
        itemAddOrderToGoIcon = menu.findItem(R.id.itemAddOrderToGoIcon)
        optionsMenu = menu //porsiacaso se guarda referencia

        mostrarContainerAddOrderToGo(false)
        mostrarMenuItemAddOrderToGoIcon(true)
        mostrarCircularProgress(false)
    }

    private fun mostrarCircularProgress(value: Boolean) {
        binding.circularProgressScreenParaLlevar.circularProgress.isVisible = value
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddOrderToGoIcon -> clickEnItemAddOrderToGoIcon()
        }
        //habilita botón flecha Volver
        return super.onOptionsItemSelected(item)
        //return true
    }
    private fun clickEnItemAddOrderToGoIcon() {
        mostrarContainerAddOrderToGo(true)
        mostrarMenuItemAddOrderToGoIcon(false)
    }
// ------- FIN DE MENU ICONS-----



    override fun onStart() {
        super.onStart()
        setObservers()
        viewModel.cargarOrdersToGoDB()
    }

    private fun setObservers() {
        viewModel.errorMessage.observeForever { message ->
            if (!message.isBlank()) {
                mostrarSnackBar(message)
                mostrarCircularProgress(false)
            }
        }
        viewModel.orderIDCreado.observeForever { idCreado ->
            if (idCreado.isNotBlank()) {
                limpiarTodosLosInputsYData()
               // redirectToCarritoDeOrderToGo(idCreado)
            } else {
                mostrarCircularProgress(false)
            }
        }

        viewModel.listaOrdersToGoDB.observeForever { listaOrdersToGoDB ->
            cargarDataContainerAddOrderToGo()
            if (listaOrdersToGoDB.isNotEmpty()) {
                mostrarNoHayPedidos(false)
                cargarRecyclerListaOrdersToGo(listaOrdersToGoDB)
            } else {
                cargarRecyclerListaOrdersToGo(ArrayList())
                mostrarNoHayPedidos(true)
                mostrarCircularProgress(false)
            }
        }
    }

    private fun limpiarTodosLosInputsYData() {
        mostrarContainerAddOrderToGo(false)
        mostrarMenuItemAddOrderToGoIcon(true)
        mostrarCircularProgress(false)
        hideKeyboard()
        val inputname = binding.inputNameClienteActiveOrderToGo
        inputname.editText?.text?.clear()
        mostrarErrorInput(inputname, "")
        val inputApellido = binding.inputApellidoClienteActiveOrderToGo
        inputApellido.editText?.text?.clear()
        mostrarErrorInput(inputApellido, "")
        val inputCelu = binding.inputWhatasappClienteActiveOrderToGo
        inputCelu.editText?.text?.clear()
        mostrarErrorInput(inputCelu, "")
    }

    private fun cargarRecyclerListaOrdersToGo(listaOrdersToGoDB: MutableList<PedidoToGo>) {
        val listaFiltrada = listaOrdersToGoDB.sortedByDescending { ord -> ord.openingTime }
            .toMutableList()
        recyclerViewOrdersToGo.setHasFixedSize(true)
        linearLayoutManagerOrdersToGo = LinearLayoutManager(context)
        recyclerViewOrdersToGo.layoutManager = linearLayoutManagerOrdersToGo
        pedidosToGoListAdapter = PedidosToGoListAdapter(listaFiltrada, this)
        recyclerViewOrdersToGo.adapter = pedidosToGoListAdapter
        mostrarCircularProgress(false)
    }

    private fun mostrarNoHayPedidos(value: Boolean) {
        binding.textoNoHayPedidosParaLlevar.isVisible = value
    }

    private fun redirectToCarritoDeOrderToGo(orderToGoID: String) {
        val action =
            ParaLlevarFragmentDirections.actionParaLlevarFragment2ToCarritoOrderFragment(orderToGoID)
        view?.findNavController()?.navigateSafe(action)
        // this.findNavController().navigate(action)
    }

    fun NavController.navigateSafe(
        navDirections: NavDirections? = null
    ) {
        try {
            navDirections?.let {
                this.navigate(navDirections)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cargarDataContainerAddOrderToGo() {
        val inputNameCliente = binding.inputNameClienteActiveOrderToGo
        errorOnChangeListener(inputNameCliente)
        val btnCancelar = binding.btnCancelarActiveOrderToGo
        btnCancelar.setOnClickListener {
            mostrarContainerAddOrderToGo(false)
            mostrarMenuItemAddOrderToGoIcon(true)
        }
        val btnCrearActive = binding.btnCrearActiveOrderToGo
        btnCrearActive.setOnClickListener {
            if (!inputNameCliente.isErrorEnabled) {
                hideKeyboard()
                mostrarCircularProgress(true)
                val nombreCliente = inputNameCliente.editText?.text.toString()
                val apellidoCliente =
                    binding.inputApellidoClienteActiveOrderToGo.editText?.text.toString()
                val celularCliente =
                    binding.inputWhatasappClienteActiveOrderToGo.editText?.text.toString()
                viewModel.crearActiveOrderToGo(nombreCliente, apellidoCliente, celularCliente)
            } else {
                mostrarErrorInput(inputNameCliente, "Requerido")
            }
        }
    }

    private fun mostrarContainerAddOrderToGo(value: Boolean) {
        containerAddOrderToGo.isVisible = value
    }

    private fun mostrarMenuItemAddOrderToGoIcon(value: Boolean) {
        itemAddOrderToGoIcon.isVisible = value
    }

    private fun errorOnChangeListener(input: TextInputLayout) {
        input.editText?.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                mostrarErrorInput(input, "Requerido")
            } else {
                mostrarErrorInput(input, "")
            }
        }
    }

    private fun mostrarSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    private fun habilitarToast(message: String?) {
        if (!message.isNullOrBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarErrorInput(input: TextInputLayout, message: String) {
        if (message.isBlank()) {
            input.isErrorEnabled = false
            input.error = ""
        } else {
            input.isErrorEnabled = true
            input.error = message
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onClickCardOrderToGo(pedidoToGo: PedidoToGo) {
        redirectToCarritoDeOrderToGo(pedidoToGo.id)
    }

    override fun onClickRemoveOrderToGo(pedidoToGo: PedidoToGo) {
        if (pedidoToGo.mozoPuedeEliminarPedido()) {
            habilitarToast("Eliminando Pedido de ${pedidoToGo.customerName}")
            viewModel.eliminarPedido(pedidoToGo.id)
        } else {
            habilitarToast("No se permite eliminar este item")
        }
    }


}