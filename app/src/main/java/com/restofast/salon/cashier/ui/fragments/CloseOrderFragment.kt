package com.restofast.salon.cashier.ui.fragments

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.restofast.salon.R
import com.restofast.salon.cashier.ui.adapters.OrderItemsCloseOrderListAdapter
import com.restofast.salon.cashier.ui.adapters.OrderPaymentsListAdapter
import com.restofast.salon.cashier.ui.viewmodels.CloseOrderViewModel
import com.restofast.salon.databinding.CloseOrderFragmentBinding
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderItem
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.payment.FormaDePago
import com.restofast.salon.orderTaking.ui.activities.ActivityOrderTaking
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class CloseOrderFragment : Fragment() {

    companion object {
        fun newInstance() = CloseOrderFragment()
    }

    private lateinit var viewModel: CloseOrderViewModel
    private lateinit var binding: CloseOrderFragmentBinding
    private lateinit var orderEntrante: Order
    private val listaTextViewDeErrors: MutableList<TextView> = ArrayList()

    //RECYCLER DE FORMASDEPAGO de Order
    lateinit var recyclerViewFormasDePago: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var orderPaymentsListAdapter: OrderPaymentsListAdapter

    //RECYCLER DE detalle de OrderItems de Order
    lateinit var recyclerViewOrderItems: RecyclerView
    private lateinit var linearLayoutManagerOrderItems: LinearLayoutManager
    private lateinit var orderItemsCloseOrderListAdapter: OrderItemsCloseOrderListAdapter

    // ELEMENTOS DE MENU
    lateinit var optionsMenu: Menu
    lateinit var itemCloseOrderMenuItem: MenuItem

    private val hayErrores = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = CloseOrderFragmentBinding.inflate(layoutInflater)

        recyclerViewFormasDePago = binding.recyclerFormasDePagoEnCloseOrder
        recyclerViewOrderItems = binding.recyclerDetalleDeOrderItemsEnCloseOrder

        /*orderEntrante = CloseOrderFragmentArgs.fromBundle(requireArguments()).order*/
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CloseOrderViewModel::class.java)
        // TODO: Use the ViewModel
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Cerrar Pedido"

    }

    // ------- INICIO DE MENU ICONS----- copiar y pegar desde acá

    //habilitar en OncreateView --> setHasOptionsMenu(true)
    //agregar los sgtes atributos al Fragment actual:
    /*
    lateinit var optionsMenu : Menu
    lateinit var itemLapizMenuItem : MenuItem
    lateinit var itemGuardarMenuItem :  MenuItem
    lateinit var itemCancelarMenuItem : MenuItem
    */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        activity?.menuInflater?.inflate(R.menu.menu_close_order, menu)
        optionsMenu = menu //porsiacaso se guarda referencia
        itemCloseOrderMenuItem = menu.findItem(R.id.itemCloseOrderIcon)
        mostrarIconCloseOrder(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemCloseOrderIcon -> clickEnCloseOrder()
        }
        //habilita botón flecha Volver
        return super.onOptionsItemSelected(item)
        //return true
    }

    private fun clickEnCloseOrder() {
        val dialogTitle = "Cerrar Pedido"
        val btnNeutralText = "Cancelar"
        var btnPositiveText = "Cerrar Pedido"
        var dialogMessage = ""

        val inputNroComprobante = binding.inputComprobanteEnCloseOrder.editText?.text.toString()
        if (inputNroComprobante.isBlank() ) {
            dialogMessage = "NO ha ingresado un NRO DE BOLETA, ¿Desea cerrar de todos modos?"
        }

        else {
            dialogMessage = "¿Está seguro de cerrar el pedido?"
        }
        //DIALOG
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setNeutralButton(btnNeutralText) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(btnPositiveText) { _, _ ->
                viewModel.closeOrderInDB(orderEntrante, inputNroComprobante)
            }.show()
        //FIN DE DIALOG
    }

// ------- FIN DE MENU ICONS-----


    override fun onStart() {
        super.onStart()
        setObservers()
        viewModel.cargarOrderDB(orderEntrante)

        binding.btnEditarPagoEnCloseOrder.setOnClickListener {
            redirectACreatePayment(orderEntrante)
        }

    }

    fun setObservers() {
      /*  viewModel.orderDB.observeForever { orderDB ->
            if(orderDB != null){
                orderEntrante = orderDB
                if (orderDB?.pago != null) {
                    when (orderDB.orderType) {
                        OrderType.TABLE -> {
                            emparejarViewConOrderTable(orderDB)
                            checkForErrors(orderDB)
                            checkForWarnings(orderDB)
                            mostrarErroresAndWarnings()
                            if (orderDB.estaHabilitadoParaGenerarCuenta() && orderDB.sumatoriaPagosCoincideConTotalAPagar()) {
                                mostrarIconCloseOrder(true)
                            } else {
                                mostrarIconCloseOrder(false)
                            }
                        }
                        else -> mostrarError("en Fragment CLoseOrder: ORDER es de tipo desconocido")
                    }


                } else {
                    //REDIRECT a CREAR PAGO NUEVO
                    //redirectACreatePayment(orderEntrante)
                }
            }
        }*/

        hayErrores.observeForever { hayErrores ->
            if (!hayErrores) {
                mostrarIconCloseOrder(true)
            } else {
                mostrarIconCloseOrder(false)
            }
        }

        viewModel.orderClosedExitosamente.observeForever { orderSeCerro ->
            val btnPositiveText = "OK"
            var dialogMessage = ""
            if (orderSeCerro) {
                dialogMessage = "Pedido Cerrado Exitosamente!"
            } else {
                dialogMessage = "Lo siento, Pedido NO pudo cerrarse, intente nuevamente"
            }
            //DIALOG
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(dialogMessage)
                .setPositiveButton(btnPositiveText) { _, _ ->
                    //volverAtras()
                    startActivity(Intent(activity, ActivityOrderTaking::class.java))
                    activity?.finish()
                }.show()
            //FIN DE DIALOG
        }
    }

    private fun redirectACreatePayment(order: Order) {
        /*val action =
            CloseOrderFragmentDirections.actionCloseOrderFragmentToCreatePaymentFragment(
                order
            )
        this.findNavController().navigate(action)*/
    }

    private fun mostrarIconCloseOrder(value: Boolean) {
        itemCloseOrderMenuItem.isVisible = value
    }

    private fun checkForErrors(order: Order) {
        if (!order.sumatoriaPagosCoincideConTotalAPagar()) {
            listaTextViewDeErrors.add(crearTextViewDeError("* Sumatoria de Pagos NO coinciden con el total a pagar"))
        }
        if (!order.estaHabilitadoParaGenerarCuenta()) {
            listaTextViewDeErrors.add(crearTextViewDeError("* Existen items inválidos"))
        }
    }

    /*private fun checkForWarnings(order: Order) {
        if (order.pago != null) {
            if (order.pago!!.nroComprobante.isBlank()) {
                listaTextViewDeErrors.add(crearTextViewDeWarning("* El pago no tiene un número de Boleta"))
            }
        }
    }*/

    private fun mostrarErroresAndWarnings() {
        val containerDeErrores: LinearLayout = binding.containerDeListaErrores
        if (listaTextViewDeErrors.size != 0) {
            containerDeErrores.removeAllViews()
            for (txtView in listaTextViewDeErrors) {
                containerDeErrores.addView(txtView)
            }
        } else {
            containerDeErrores.removeAllViews()
        }
    }

    private fun crearTextViewDeError(message: String): TextView {
        val txtView = TextView(context)
        txtView.setTextColor(Color.RED)
        txtView.text = message
        return txtView
    }

    private fun crearTextViewDeWarning(message: String): TextView {
        val txtView = TextView(context)
        txtView.setTextColor(Color.rgb(205, 150, 0))
        txtView.text = message
        return txtView
    }

    private fun vaciarErrorsAndWarnings() {
        val containerDeErrores: LinearLayout = binding.containerDeListaErrores
        containerDeErrores.removeAllViews()
        listaTextViewDeErrors.clear()
    }

    /*private fun emparejarViewConOrderTable(orderDB: Order) {
        vaciarErrorsAndWarnings()
        val order = orderDB as OrderTable
        binding.tipoPedidoEnCloseOrder.text = "MESA"
        binding.nroMesaEnCloseOrder.text = order.nroMesa
        binding.importeTotalEnCloseOrder.text = order.totalAPagar
        val editable = Editable.Factory.getInstance().newEditable(order.pago?.nroComprobante)
        binding.inputComprobanteEnCloseOrder.editText?.text = editable
        if (order.pago != null) {
            cargarRecyclerFormasDePagoCreados(order.pago!!.formasDePago)
        }
        if (order.orderItems.size != 0) {
            cargarRecyclerDetalleDePedidos(order.orderItems)
        }
    }*/

    fun cargarRecyclerFormasDePagoCreados(lista: MutableList<FormaDePago>) {
        recyclerViewFormasDePago.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewFormasDePago.layoutManager = linearLayoutManager
        orderPaymentsListAdapter = OrderPaymentsListAdapter(lista)
        recyclerViewFormasDePago.adapter = orderPaymentsListAdapter
    }

    fun cargarRecyclerDetalleDePedidos(lista: MutableList<OrderItem>) {
        recyclerViewOrderItems.setHasFixedSize(true)
        linearLayoutManagerOrderItems = LinearLayoutManager(context)
        recyclerViewOrderItems.layoutManager = linearLayoutManagerOrderItems
        orderItemsCloseOrderListAdapter = OrderItemsCloseOrderListAdapter(lista)
        recyclerViewOrderItems.adapter = orderItemsCloseOrderListAdapter
    }


    fun mostrarError(message: String) {
        if (message.isBlank()) {
            binding.containerTotalErrorEnCloseOrder.visibility = View.GONE
            binding.errorMessageEnCloseOrder.text = ""
        } else {
            binding.containerTotalErrorEnCloseOrder.visibility = View.VISIBLE
            binding.errorMessageEnCloseOrder.text = message
        }
    }
    private fun volverAtras() {
        requireActivity().onBackPressed()
    }

}