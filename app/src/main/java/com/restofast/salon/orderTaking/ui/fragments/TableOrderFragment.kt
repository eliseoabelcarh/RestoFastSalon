package com.restofast.salon.orderTaking.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.databinding.TableOrderFragmentBinding
import com.restofast.salon.entities.enums.CategoriaPlato
import com.restofast.salon.entities.enums.OrderItemState
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.enums.RoleType
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderItem
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.entities.products.Plato
import com.restofast.salon.entities.products.Product
import com.restofast.salon.orderTaking.ui.adapters.CarritoItemListAdapter
import com.restofast.salon.orderTaking.ui.adapters.ProductsListAdapter
import com.restofast.salon.orderTaking.ui.viewmodels.TableOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
@WithFragmentBindings
class TableOrderFragment : Fragment(), ProductsListAdapter.OnButtonAddProductClickListener,
    CarritoItemListAdapter.OnCarritoItemClickListener {

    companion object {
        fun newInstance() = TableOrderFragment()
    }

    private lateinit var viewModel: TableOrderViewModel
    private lateinit var binding: TableOrderFragmentBinding
    private lateinit var table: Table
    private var empleado: Empleado? = null


    private val currentChipChecked = MutableLiveData<Chip>()
    private val listaProductosFiltrados = MutableLiveData<MutableList<Product>>()


    //RECYCLER DE PRODUCTOS MENU
    lateinit var recyclerViewProductos: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var productsListAdapter: ProductsListAdapter

    //RECYCLER DE CARRITO
    lateinit var recyclerViewCarritoItems: RecyclerView
    private lateinit var linearLayoutManagerCarrito: LinearLayoutManager
    private lateinit var carritoItemListAdapter: CarritoItemListAdapter

    // ELEMENTOS DE MENU
    lateinit var optionsMenu: Menu
    lateinit var itemComandaMenuItem: MenuItem
    lateinit var itemPagoMenuItem: MenuItem
    lateinit var itemVerificarYCerrarPedidoItem: MenuItem

    //DIALOG DE COMMENTS ITEM
    lateinit var vista: View
    lateinit var dialogRef: BottomSheetDialog
    lateinit var buttonGuardarAnotaciones: Button
    lateinit var comentarios: TextInputLayout
    lateinit var quantity: TextView
    lateinit var productName: TextView
    lateinit var subtotalPrice: TextView
    lateinit var stateOrderItem: TextView
    lateinit var buttonCloseBottonSheetDialog: Button

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
        /*table = TableOrderFragmentArgs.fromBundle(requireArguments()).table*/
        (activity as AppCompatActivity).supportActionBar?.title = "Pedido para mesa ${table.number}"
        binding = TableOrderFragmentBinding.inflate(layoutInflater)

        //recycler de productos
        recyclerViewProductos = binding.productos.recyclerProducts
        recyclerViewCarritoItems = binding.carritoDeTable.recyclerCartProducts


        //INICIALIZANDO DIALOG COMMENTS ORDER ITEM
        vista = layoutInflater.inflate(R.layout.item_add_comment_order_item, null)
        buttonGuardarAnotaciones = vista.findViewById(R.id.btnGuardarAnotaciones)
        comentarios = vista.findViewById(R.id.txt_comments)
        quantity = vista.findViewById(R.id.quantityItemComments)
        productName = vista.findViewById(R.id.productItemNameComments)
        subtotalPrice = vista.findViewById(R.id.subtotalPriceComments)
        stateOrderItem = vista.findViewById(R.id.stateItemComments)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TableOrderViewModel::class.java)
        viewModel.cargarCurrentEmpleado()
    }


    // ------- INICIO DE MENU ICONS----- copiar y pegar desde acá

    //habilitar en OncreateView --> setHasOptionsMenu(true)
    //agregar los sgtes atributos al Fragment actual:
    /*
    lateinit var optionsMenu : Menu
    lateinit var itemLapizMenuItem : MenuItem
    */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()

        //menu_perfil_paciente ---- nombre del menu a utilizar
        activity?.menuInflater?.inflate(R.menu.menu_table_order, menu)
        optionsMenu = menu //porsiacaso se guarda referencia
        itemComandaMenuItem = menu.findItem(R.id.itemComandaIcon)
        itemPagoMenuItem = menu.findItem(R.id.itemPagoIcon)
        itemVerificarYCerrarPedidoItem = menu.findItem(R.id.itemVerificarYCerrarPedidoIcon)


        mostrarPagoIcon(false)
        mostrarComandaIcon(false)
        mostrarCloseOrderIcon(false)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        /*    R.id.itemComandaIcon -> clickEnEnviarComandaACocina()
            R.id.itemPagoIcon -> clickEnGuardarPago()
            R.id.itemVerificarYCerrarPedidoIcon -> clickEnVerificarYCerrarPedido()*/
        }
        //habilita botón flecha Volver
        return super.onOptionsItemSelected(item)
        //return true
    }


   /* private fun clickEnEnviarComandaACocina() {
        mostrarCircularProgress(true)
        viewModel.enviarPedidosACocina(table.orderTable as Order)
    }

    private fun clickEnGuardarPago() {
        mostrarCircularProgress(true)
        val action =
            TableOrderFragmentDirections.actionTableOrderFragmentToCreatePaymentFragment(table.orderTable as Order)
        this.findNavController().navigate(action)
    }

    private fun clickEnVerificarYCerrarPedido() {
        mostrarCircularProgress(true)
        val order = table.orderTable as Order
        val action =
            TableOrderFragmentDirections.actionTableOrderFragmentToCloseOrderFragment(order)
        this.findNavController().navigate(action)

    }*/

// ------- FIN DE MENU ICONS-----


    override fun onStart() {
        super.onStart()

        setObservers()
        cargarChipGroup()

        viewModel.cargarListaProductosDeCarta()

        /* //descomentar luego
        viewModel.cargarTableDB(table)
        */


    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    private fun setObservers() {

        viewModel.currentEmpleado.observeForever { empleadoDB ->
            empleado = empleadoDB
        }

    /*    viewModel.tableDB.observeForever { mesaDB ->
            table = mesaDB
            setColorAIconoPagoSegunSumatoriaCorrecta(mesaDB.orderTable)
            if (mesaDB.orderTable != null) {
                binding.carritoDeTable.totalTable.text = mesaDB.orderTable!!.totalAPagar
                if (mesaDB.tieneOrderItemsQueFaltanAceptar()) {
                    mostrarComandaIcon(true)
                    mostrarPagoIcon(false)
                    mostrarCloseOrderIcon(false)
                } else if (mesaDB.estaHabilitadoParaGenerarCuenta()) {
                    mostrarComandaIcon(false)
                    mostrarPagoIcon(true)
                    if (mesaDB.orderTable!!.sumatoriaPagosCoincideConTotalAPagar()) {
                        mostrarCloseOrderIcon(true)
                    } else {
                        mostrarCloseOrderIcon(false)
                    }

                } else {
                    mostrarComandaIcon(false)
                    mostrarPagoIcon(false)
                    mostrarCloseOrderIcon(false)
                }

            } else {
                binding.carritoDeTable.totalTable.text = "0.00"
                mostrarComandaIcon(false)
                mostrarPagoIcon(false)
                mostrarCloseOrderIcon(false)
            }
            mostrarCircularProgress(false)
        }
*/

        viewModel.errorMessage.observeForever { result ->
            mostrarCircularProgress(false)
            if (result.isNotBlank()) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            }
        }

        currentChipChecked.observeForever { chipSelected ->
            viewModel.listaProductosDatabase.observeForever { result ->
               /* val filtrado = result.filter {
                    (it.productType == ProductType.PLATO && (it as Plato).category.name == chipSelected.text)
                            ||
                            (it.productType == ProductType.BEBIDA && chipSelected.text == ProductType.BEBIDA.nombrePlural)
                            ||
                            (it.productType == ProductType.ENVASE_DESCARTABLE && chipSelected.text == ProductType.ENVASE_DESCARTABLE.nombrePlural)
                }.sortedBy { item -> item.name }
                if (filtrado.isNotEmpty()) {
                    listaProductosFiltrados.value = filtrado as MutableList<Product>
                } else {
                    listaProductosFiltrados.value = ArrayList()
                }*/
                mostrarCircularProgress(false)
            }
        }

        listaProductosFiltrados.observeForever { listaFiltrada ->
            cargarRecyclerDeProductos(listaFiltrada)
            mostrarCircularProgress(false)
        }

        viewModel.orderItemsDeTableDB.observeForever { listaOrderItems ->
            var listaFinal: MutableList<OrderItem> = ArrayList()
            if (listaOrderItems.isNotEmpty()) {
                listaFinal =
                    listaOrderItems.sortedByDescending { orderItem -> orderItem.openingTime } as MutableList<OrderItem>
            }
            cargarRecyclerDeOrderItems(listaFinal)
            mostrarCircularProgress(false)
        }


    }//FIN DE SETOBSERVERS

    private fun cargarRecyclerDeOrderItems(lista: MutableList<OrderItem>) {
        recyclerViewCarritoItems.setHasFixedSize(true)
        linearLayoutManagerCarrito = LinearLayoutManager(context)
        recyclerViewCarritoItems.layoutManager = linearLayoutManagerCarrito
        carritoItemListAdapter = CarritoItemListAdapter(lista, this)
        recyclerViewCarritoItems.adapter = carritoItemListAdapter
    }

    private fun cargarRecyclerDeProductos(lista: MutableList<Product>) {
        recyclerViewProductos.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewProductos.layoutManager = linearLayoutManager
        productsListAdapter = ProductsListAdapter(lista, this)
        recyclerViewProductos.adapter = productsListAdapter
    }

    fun setColorAIconoPagoSegunSumatoriaCorrecta(order: Order?) {
        if (order != null) {
            if (order.sumatoriaPagosCoincideConTotalAPagar()) {
                itemPagoMenuItem.icon.setTint(Color.GREEN)
            } else {
                itemPagoMenuItem.icon.setTint(Color.RED)
            }
        }
    }


    //LISTA DE PRODUCTOS
    override fun onButtonAddClick(product: Product) {
        mostrarCircularProgress(true)
        viewModel.agregarProductoAPedidoDeMesa(table, product)
    }

    //carrito de mesa
    override fun onButtonAddClick(orderItem: OrderItem) {
        mostrarCircularProgress(true)
        viewModel.addOneOrderItemToOrderTable(table, orderItem)
    }

    override fun onButtonMinusClick(orderItem: OrderItem) {
        mostrarCircularProgress(true)
      /*  viewModel.minusOneOrderItemToOrderTable(table, orderItem)*/
    }

    override fun onButtonRemoveClick(orderItem: OrderItem) {
        mostrarCircularProgress(true)
      /*  viewModel.removeCompleteOrderItemFromTable(table, orderItem)*/
    }

    override fun onCardClick(orderItem: OrderItem) {
        if (orderItem.mozoPuedeAgregarComentarios()) {
            (vista.parent as? ViewGroup)?.removeView(vista)
            val dialog = BottomSheetDialog(this.requireContext())
            dialogRef = dialog
            dialog.setContentView(vista)
         /*   cargarDatosParaDialogOrderItemComments(orderItem)*/
            // dialog.setCancelable(false)
            dialog.show()
            comentarios.requestFocus()
        } else {
            Toast.makeText(
                context,
                "MOZO YA NO PUEDE AGREGAR NOTAS A ESTE ITEM",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

   /* fun guardarAnotaciones(orderItem: OrderItem, comentarios: String) {
        viewModel.addCommentToOrderItem(
            table, orderItem,
            comentarios.uppercase(Locale.getDefault())
        )
        dialogRef.dismiss()
        mostrarCircularProgress(true)
    }*/

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

   /* fun cargarDatosParaDialogOrderItemComments(orderItem: OrderItem) {
        val editable = Editable.Factory.getInstance().newEditable(orderItem.comentarios)
        comentarios.editText?.text = editable
        quantity.text = orderItem.qty.toString()
        productName.text = orderItem.product?.name.toString()
        subtotalPrice.text = orderItem.subTotal
        setOrderItemStateColors(stateOrderItem, orderItem.orderItemState)
        buttonGuardarAnotaciones.setOnClickListener {
            guardarAnotaciones(orderItem, comentarios.editText?.text.toString())
        }
    }*/

    private fun mostrarCircularProgress(value: Boolean) {
        if(value){
            binding.circularProgressScreenSinFondo.circularProgressSinFondo.visibility = View.VISIBLE
        }else{
            binding.circularProgressScreenSinFondo.circularProgressSinFondo.visibility = View.GONE
        }
    }

    private fun mostrarComandaIcon(value: Boolean) {
        itemComandaMenuItem.isVisible = value
    }

    private fun mostrarPagoIcon(value: Boolean) {
        itemPagoMenuItem.isVisible = value
    }

    private fun mostrarCloseOrderIcon(value: Boolean) {
        if (value) {
            if (empleado != null) {
                itemVerificarYCerrarPedidoItem.isVisible =
                    empleado!!.tieneAlgunPermisoSegunRoleTypes(listaDePermisosParaCerrarPedido)
            } else {
                itemVerificarYCerrarPedidoItem.isVisible = false
            }
        } else {
            itemVerificarYCerrarPedidoItem.isVisible = false
        }
    }

    private fun cargarChipGroup() {
        var number = 0
        val chipGroup = binding.productos.chipGroupCategorias
        chipGroup.removeAllViews()
        val listaDeChips: MutableList<Chip> = ArrayList()

        val listaDeCategorias = CategoriaPlato.values()
        for (categoria in listaDeCategorias) {
            if (categoria != CategoriaPlato.SIN_CATEGORIA) {
                val chip = crearChip(requireContext(), categoria.name, number++)
                listaDeChips.add(chip)
            }
        }
        val chipBebida = crearChip(requireContext(), ProductType.BEBIDA.nombrePlural, number++)
        listaDeChips.add(chipBebida)

        val chipEnvases = crearChip(requireContext(), ProductType.ENVASE_DESCARTABLE.nombrePlural, number++)
        listaDeChips.add(chipEnvases)

        for (chip in listaDeChips) {
            chipGroup.addView(chip)
        }

        chipGroup.isSelectionRequired = true
        chipGroup.isSingleSelection = true
        chipGroup.clearCheck()
        //seteo default
        val chipDefault = chipGroup.getChildAt(0)
        chipGroup.check(chipDefault.id)
        currentChipChecked.value = chipDefault as Chip
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chipChecked = getChipById(checkedId, group)
            if (chipChecked != null) {
                currentChipChecked.value = chipChecked!!
            }
        }
    }

    private fun getChipById(idBuscado: Int, chipGroup: ChipGroup): Chip? {
        var encontrado: Chip? = null
        var i = 0
        while (encontrado == null && i < chipGroup.size) {
            if (chipGroup[i].id == idBuscado) {
                encontrado = chipGroup[i] as Chip
            }
            i++
        }
        return encontrado
    }

    private fun crearChip(context: Context, texto: String, number: Int): Chip {
        return Chip(context).apply {
            id = number
            text = texto
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            chipBackgroundColor = colorStates()
            chipStrokeColor = colorStates()
            chipStrokeWidth = 2F
            setChipStrokeColorResource(R.color.common_google_signin_btn_text_light_disabled)
        }
    }

    // function to generate color state list for chip
    private fun colorStates(): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )
        val colors = intArrayOf(
            // chip checked color
            Color.parseColor("#D8D8D8"),
            // chip unchecked color
            Color.parseColor("#FFFFFF")
        )
        return ColorStateList(states, colors)
    }

    private fun setOrderItemStateColors(txt: TextView, state: OrderItemState) {
        txt.text = state.nombre
        txt.setTextColor(OrderItemState.getColorState(state))
    }

}