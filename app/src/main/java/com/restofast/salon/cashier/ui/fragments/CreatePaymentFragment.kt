package com.restofast.salon.cashier.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.restofast.salon.cashier.ui.adapters.FormasDePagoListAdapter
import com.restofast.salon.cashier.ui.viewmodels.CreatePaymentViewModel
import com.restofast.salon.databinding.CreatePaymentFragmentBinding
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.enums.RoleType
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.payment.FormaDePago
import com.restofast.salon.entities.payment.FormaDePagoEfectivo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.math.BigDecimal

@AndroidEntryPoint
@WithFragmentBindings
class CreatePaymentFragment : Fragment(), FormasDePagoListAdapter.OnButtonClickListener {

    companion object {
        fun newInstance() = CreatePaymentFragment()
    }

    private lateinit var viewModel: CreatePaymentViewModel
    private lateinit var binding: CreatePaymentFragmentBinding
    private lateinit var orderEntrante: Order

    //RECYCLER DE formas de pago
    lateinit var recyclerViewFormasDePago: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var formasDePagoListAdapter: FormasDePagoListAdapter

    private val sumaPreviaLiveData = MutableLiveData<String>()
    private var sumasCoinciden: Boolean = false
    private var listaFormasDePagoGeneral: MutableList<FormaDePago> = ArrayList()


    //LISTA DE PERMISOS PARA VER BOTON DE CERRAR PEDIDO
    private val listaDePermisosParaCerrarPedido = mutableListOf(
        RoleType.ADMIN, RoleType.DUENIO,
        RoleType.SUPERVISOR, RoleType.CAJERO
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = CreatePaymentFragmentBinding.inflate(layoutInflater)
       /* orderEntrante = CreatePaymentFragmentArgs.fromBundle(requireArguments()).order*/
        recyclerViewFormasDePago = binding.recyclerDeMetodosDePagoEnCreatePayment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreatePaymentViewModel::class.java)
        // TODO: Use the ViewModel
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Crear Pago"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()


    }

    override fun onStart() {
        super.onStart()

       /* setObservers()*/
        viewModel.cargarOrderDB(orderEntrante)
        mostrarQueExistePagoCreado(false)
        setearValorInicialParaSumaPrevia()
        mostrarCircularProgress(false)

        //viewModel.cargarUltimoNroBoletaSugerido()

        viewModel.cargarCurrentEmpleado()


        binding.btnAddCashPaymentEnCreatePayment.setOnClickListener {
            viewModel.agregarFormaDePago(TipoFormaDePago.EFECTIVO)
            mostrarBotonAddPagoEfectivo(false)
        }
        binding.btnAddCardPaymentEnCreatePayment.setOnClickListener {
            viewModel.agregarFormaDePago(TipoFormaDePago.VENDEMAS)
        }
        binding.btnSavePaymentEnCreatePayment.setOnClickListener {
            mostrarCircularProgress(true)
            hideKeyboard()
            if (sumasCoinciden) {
                val inputsSonValidos = todosLosImportesSonValidos(listaFormasDePagoGeneral)
                val inputComprobante =
                    binding.inputComprobanteEnCreatePayment.editText?.text.toString()



                if (inputsSonValidos) {
                    setErrorMessage("")
                    viewModel.btnSavePayment(
                        orderEntrante,
                        listaFormasDePagoGeneral,
                        inputComprobante
                    )
                } else {
                    mostrarCircularProgress(false)
                }
            } else {
                mostrarCircularProgress(false)
                setErrorMessage("La Suma NO coincide con Total a pagar")
            }

        }


    }

    fun setearValorInicialParaSumaPrevia() {
        binding.sumaPreviaEnCreatePayment.setTextColor(Color.RED)
        binding.sumaPreviaEnCreatePayment.text = "0.00"
    }

/*    fun setObservers() {
        viewModel.orderDB.observeForever { orderDB ->
            if (orderDB != null) {
                orderEntrante = orderDB
                when (orderDB.orderType) {
                    OrderType.TABLE -> {
                        val order: OrderTable = orderDB as OrderTable
                        binding.importeTotalParaPagarEnCreatePayment.text = order.totalAPagar
                        if (order.pago != null) {
                            val listaExistente = order.pago!!.formasDePago
                            val nroComprobante = order.pago!!.nroComprobante
                            if (!listaExistente.isNullOrEmpty()) {
                                mostrarQueExistePagoCreado(true)
                                viewModel.agregarListaYaExistente(listaExistente)
                            }
                            if (nroComprobante.isNotBlank()) {
                                val editable =
                                    Editable.Factory.getInstance().newEditable(nroComprobante)
                                binding.inputComprobanteEnCreatePayment.editText?.text = editable
                                binding.inputComprobanteEnCreatePayment.hint = "Nro Boleta GUARDADO"
                            }else{
                                viewModel.cargarUltimoNroBoletaSugerido()
                            }
                        } else {
                            //No Existe pago creado para esta order
                            mostrarQueExistePagoCreado(false)
                            viewModel.cargarUltimoNroBoletaSugerido()
                        }

                    }
                    OrderType.DELIVERY -> {
                        //TODO
                    }
                }
            } else {
                //orderDB es null
            }

            viewModel.listaMetodosPago.observeForever { listaMetodosPago ->
                mostrarCircularProgress(false)
                var lista: MutableList<FormaDePago> = ArrayList()
                if (!listaMetodosPago.isNullOrEmpty()) {
                    lista = listaMetodosPago
                } else {
                    sumaPreviaLiveData.value = "0.00"
                    binding.sumaPreviaEnCreatePayment.text = "0.00"
                }
                //siempre setea
                listaFormasDePagoGeneral = lista
                cargarRecyclerView(listaFormasDePagoGeneral)
            }

            sumaPreviaLiveData.observeForever { sumaPrevia ->
                if (orderDB.totalAPagar == sumaPrevia) {
                    binding.sumaPreviaEnCreatePayment.setTextColor(Color.rgb(0, 150, 40))
                    sumasCoinciden = true
                } else {
                    binding.sumaPreviaEnCreatePayment.setTextColor(Color.RED)
                    sumasCoinciden = false
                }
            }
        }
        viewModel.pagoCreadoExitosamente.observeForever { exitoso ->
            mostrarCircularProgress(false)
            var dialogTitle = "Pago Creado Exitosamente!"
            var dialogMessage = "Pago agregado a pedido."
            var btnPositiveText = "OK"
            if (!exitoso) {
                dialogTitle = "ERROR!"
                dialogMessage = "Hubo algún Error, intenta de nuevo!"
                btnPositiveText = "OK"
                Toast.makeText(context, "Ocurrió Error", Toast.LENGTH_LONG).show()
            }
            //DIALOG
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(btnPositiveText) { _, _ ->
                    volverAtras()
                }.show()
            //FIN DE DIALOG
        }


        viewModel.ultimoNroBoletaSugerido.observeForever { nroBoleta ->
            if(!nroBoleta.isNullOrBlank()){
                val editable = Editable.Factory.getInstance().newEditable(nroBoleta)
                binding.inputComprobanteEnCreatePayment.editText?.text = editable
                binding.inputComprobanteEnCreatePayment.hint = "Nro Boleta SUGERIDO"
            }
        }

        viewModel.currentEmpleado.observeForever { empleado ->
            //POR AHORA NO HAY NADA PARA HACER CONN EMPLEADO
        }


    }//fin de sertobservers*/

    private fun cargarRecyclerView(lista: MutableList<FormaDePago>) {
        recyclerViewFormasDePago.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewFormasDePago.layoutManager = linearLayoutManager
        formasDePagoListAdapter = FormasDePagoListAdapter(lista, this)
        recyclerViewFormasDePago.adapter = formasDePagoListAdapter
    }

    override fun onButtonRemoveClick(formaDePago: FormaDePago) {
        setErrorMessage("")
        if (formaDePago is FormaDePagoEfectivo) {
            mostrarBotonAddPagoEfectivo(true)
        }
        viewModel.removeFormaDePagoDeListaMetodosPago(formaDePago)
    }

    override fun onChangeImporte(lista: MutableList<FormaDePago>) {
        setErrorMessage("")
        var sumaPrevia = "0.00"
        if (!lista.isNullOrEmpty()) {
            sumaPrevia = sumarImportes(lista)
            Log.d("TAG--", "sumaPrevia: $sumaPrevia")
        }
        sumaPreviaLiveData.value = sumaPrevia
        binding.sumaPreviaEnCreatePayment.text = sumaPrevia
        //listaFormasDePagoGeneral = lista
    }

    override fun setearVuelto(
        listaFormasDePago: MutableList<FormaDePago>,
        position: Int
    ): MutableLiveData<FormaDePago> {

        val formaDePagoObservable = MutableLiveData<FormaDePago>()

        var sumaDeLosDemas = BigDecimal("0.00")
        var i = 0

        while (i < listaFormasDePago.size) {
            if (i != position) {
                val importe = listaFormasDePago[i].importeCobrado
                if(importe.isNotBlank() && importe != "0.00"){
                    val importeCobrado = BigDecimal(listaFormasDePago[i].importeCobrado)
                    sumaDeLosDemas = sumaDeLosDemas.add(importeCobrado)
                }
            }
            i++
        }
       /* val totalAPagarDeTodoElPedido = BigDecimal(orderEntrante.totalAPagar)

        val diferenciaQueRestaPorPagar = totalAPagarDeTodoElPedido.subtract(sumaDeLosDemas)

        val clientePagaCon = BigDecimal(listaFormasDePago[position].clientePagaCon)

        if (clientePagaCon >= diferenciaQueRestaPorPagar) {
            val vuelto = clientePagaCon.subtract(diferenciaQueRestaPorPagar)
            listaFormasDePago[position].importeCobrado = diferenciaQueRestaPorPagar.toPlainString()
            listaFormasDePago[position].vuelto = vuelto.toPlainString()
        }else{
            listaFormasDePago[position].importeCobrado= clientePagaCon.toPlainString()
            listaFormasDePago[position].vuelto = "0.00"
        }
        */
        listaFormasDePago[position].actualizarValores()
        listaFormasDePagoGeneral = listaFormasDePago

        formaDePagoObservable.value =  listaFormasDePago[position]

        return formaDePagoObservable
    }

    fun mostrarBotonAddPagoEfectivo(value: Boolean) {
        binding.btnAddCashPaymentEnCreatePayment.isEnabled = value
    }

    private fun mostrarQueExistePagoCreado(value: Boolean) {
        binding.containerExistePagoEnCreatePayment.isVisible = value
    }

    private fun mostrarCircularProgress(value: Boolean) {
        binding.circularProgressEnCreatePayment.circularProgressSinFondo.isVisible = value
    }

    fun sumarImportes(lista: MutableList<FormaDePago>): String {
        var sumaTotal = BigDecimal("0.00")
        for (item in lista) {
            if (item.importeCobrado.isNotBlank()) {
                sumaTotal = sumaTotal.add(BigDecimal(item.importeCobrado))
            } else {
                sumaTotal = sumaTotal.add(BigDecimal("0.00"))
            }
        }
        return sumaTotal.toPlainString()
    }

    fun todosLosImportesSonValidos(lista: MutableList<FormaDePago>): Boolean {
        var resultado: Boolean = true
        lista.forEach {
            if (!importeEsValido(it)) {
                resultado = false
            }
        }
        return resultado
    }

    private fun importeEsValido(formaDePago: FormaDePago): Boolean {
        if (formaDePago.importeCobrado.isBlank()) {
            setErrorMessage("Existe Campo vacío")
            return false
        } else {
            setErrorMessage("")
            return true
        }
    }

    private fun volverAtras() {
        requireActivity().onBackPressed()
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setErrorMessage(message: String) {
        if (message.isNotBlank()) {
            binding.errorMessageEnCreatePayment.isVisible = true
            binding.errorMessageEnCreatePayment.text = message
        } else {
            binding.errorMessageEnCreatePayment.isVisible = false
            binding.errorMessageEnCreatePayment.text = ""
        }
    }


}