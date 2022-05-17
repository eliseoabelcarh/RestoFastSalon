package com.restofast.salon.orderTaking.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.databinding.CarritoOrderFragmentBinding
import com.restofast.salon.entities.enums.ItemDePedidoState
import com.restofast.salon.entities.order.ItemDePedido
import com.restofast.salon.entities.order.Pedido
import com.restofast.salon.entities.order.PedidoMesa
import com.restofast.salon.entities.order.PedidoToGo
import com.restofast.salon.entities.products.CategoriaProducto
import com.restofast.salon.entities.products.Product
import com.restofast.salon.orderTaking.ui.adapters.CarritoItemsPedidoListAdapter
import com.restofast.salon.orderTaking.ui.adapters.ProductsListAdapter
import com.restofast.salon.orderTaking.ui.viewmodels.CarritoOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
@WithFragmentBindings
class CarritoOrderFragment : Fragment(), ProductsListAdapter.OnButtonAddProductClickListener,
    CarritoItemsPedidoListAdapter.OnCarritoItemClickListener {

    companion object {
        fun newInstance() = CarritoOrderFragment()
    }

    private lateinit var viewModel: CarritoOrderViewModel
    private lateinit var binding: CarritoOrderFragmentBinding
    private var orderIDEntrante: String = ""
    private val todosLosProductosDB: MutableList<Product> = ArrayList()

    // ELEMENTOS DE MENU
    lateinit var optionsMenu: Menu
    lateinit var itemSaveMenuItem: MenuItem


    //RECYCLER DE PRODUCTOS MENU
    lateinit var recyclerViewProductos: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var productsListAdapter: ProductsListAdapter

    //RECYCLER DE CARRITO
    lateinit var recyclerViewCarritoItems: RecyclerView
    private lateinit var linearLayoutManagerCarrito: LinearLayoutManager
    private lateinit var carritoItemListAdapter: CarritoItemsPedidoListAdapter

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

    private var pedidoCambio: Boolean = false
    private var pedidoConItems: MutableList<ItemDePedido> = ArrayList()

    private var pedidoGuardado = MutableLiveData(true)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        binding = CarritoOrderFragmentBinding.inflate(layoutInflater)

        orderIDEntrante = CarritoOrderFragmentArgs.fromBundle(requireArguments()).orderID

        //recycler de productos
        recyclerViewProductos = binding.productos.recyclerProducts

        //recylcer de Carrito
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
        viewModel = ViewModelProvider(this).get(CarritoOrderViewModel::class.java)

    }


    // ------- INICIO DE MENU ICONS----- copiar y pegar desde acá
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        activity?.menuInflater?.inflate(R.menu.menu_carrito_order, menu)
        optionsMenu = menu //porsiacaso se guarda referencia
        itemSaveMenuItem = menu.findItem(R.id.itemSavePedidoIcon)
        mostrarSavePedidoIcon(false)
        pedidoGuardado.observeForever { pedidoGuardado ->
            if (!pedidoGuardado) {
                mostrarSavePedidoIcon(true)
            } else {
                mostrarSavePedidoIcon(false)
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemSavePedidoIcon -> clickEnSavePedidoYAvisarCocina()
        }
        //habilita botón flecha Volver
        return super.onOptionsItemSelected(item)
        //return true
    }

    private fun clickEnSavePedidoYAvisarCocina() {
        updateItemsDePedido()
        habilitarToast("Guardando y Notificando a Cocina...")
        pedidoGuardado.value = true
        pedidoCambio = false
    }


// ------- FIN DE MENU ICONS-----

    override fun onStop() {
        if (pedidoCambio) {
            habilitarToast("Guardando y Notificando a Cocina...")
            updateItemsDePedido()
        }
        super.onStop()
    }

    private fun updateItemsDePedido() {
        viewModel.guardarItemsEnPedidoDatabase(orderIDEntrante, pedidoConItems)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProductosDeSucursal()
        setObservers()

    }//fin onstart

    private fun habilitarToast(message: String?) {
        if (!message.isNullOrBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarSavePedidoIcon(value: Boolean) {
        itemSaveMenuItem.isVisible = value
    }

    private fun setObservers() {

        viewModel.itemsGuardados.observeForever { result ->
            if (result) {
                lifecycleScope.launch {
                    viewModel.notificarCocinaParaAceptarPedidos(orderIDEntrante)
                }
            }
        }

        viewModel.errorMessage.observeForever { message ->
            if (!message.isNullOrBlank()) {
                habilitarToast(message)
            }
        }

        viewModel.pedidoDatabase.observeForever { pedidoDB ->
            if (pedidoDB != null) {
                setearTituloDeFragment(pedidoDB)
                pedidoConItems.clear()
                if (pedidoDB.itemsDePedido.isNotEmpty()) {
                    pedidoConItems.addAll(pedidoDB.itemsDePedido)
                    cargarRecyclerDeCarrito(pedidoDB.itemsDePedido[0])//forzando q se ilumine el primero//arreglar luego
                } else {
                    cargarRecyclerDeCarrito(ItemDePedido("X"))//invento uno que se ilumina//arreglar luego
                }
            }
        }

        viewModel.listaProductosDatabase.observeForever { listaProductos ->
            if (!listaProductos.isNullOrEmpty()) {
                todosLosProductosDB.clear()
                todosLosProductosDB.addAll(listaProductos)
                cargarChipGroupYProductos()
                viewModel.cargarPedidoDesdeDatabase(orderIDEntrante)
            } else {
                habilitarToast("lista de Productos Vacío")
            }
        }
    }

    private fun setearTituloDeFragment(pedido: Pedido) {
        when (pedido) {
            is PedidoMesa -> {
                (activity as AppCompatActivity).supportActionBar?.title =
                    "Pedido Mesa Nro ${pedido.nroMesa}"
            }
            is PedidoToGo -> {
                (activity as AppCompatActivity).supportActionBar?.title =
                    "Para Llevar:  ${pedido.customerName.uppercase(Locale.getDefault())}"
            }
        }
    }

    private fun cargarChipGroupYProductos() {
        val currentChipChecked = MutableLiveData<Chip>()
        val listaProductosFiltrados = MutableLiveData<MutableList<Product>>()
        val todasLasCategoriasDeProductosDB: MutableList<CategoriaProducto> = ArrayList()
        val listaDeChips: MutableList<Chip> = ArrayList()

        var number = 0
        val chipGroup = binding.productos.chipGroupCategorias

        viewModel.getCategoriasDeProductoDeSucursal()

        viewModel.listaCategoriasDeProductoDatabase.observeForever { listaDeCategorias ->
            chipGroup.removeAllViews()
            todasLasCategoriasDeProductosDB.clear()
            listaDeChips.clear()

            if (!listaDeCategorias.isNullOrEmpty()) {
                todasLasCategoriasDeProductosDB.clear()
                todasLasCategoriasDeProductosDB.addAll(listaDeCategorias)

                for (categoria in listaDeCategorias) {
                    val chip = crearChip(requireContext(), categoria.name, number++)
                    listaDeChips.add(chip)
                }
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

            } else {
                habilitarToast("lista de Categorias Vacía")
            }
        }

        currentChipChecked.observeForever { chipSelected ->
            if (todosLosProductosDB.isNotEmpty() && todasLasCategoriasDeProductosDB.isNotEmpty()) {
                val filtrado = todosLosProductosDB.filter {
                    (getNameDeCategoriaProducto(
                        it.categoriaProductoID,
                        todasLasCategoriasDeProductosDB
                    ) == chipSelected.text)
                }.sortedBy { item -> item.name }.toMutableList()
                if (filtrado.isNotEmpty()) {
                    listaProductosFiltrados.value = filtrado
                } else {
                    listaProductosFiltrados.value = ArrayList()
                }
            }
        }

        listaProductosFiltrados.observeForever { listaFiltrada ->
            if (!listaFiltrada.isNullOrEmpty()) {
                cargarRecyclerDeProductos(listaFiltrada)
            }
        }

    }

    private fun cargarRecyclerDeProductos(lista: MutableList<Product>) {
        recyclerViewProductos.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewProductos.layoutManager = linearLayoutManager
        productsListAdapter = ProductsListAdapter(lista, this)
        recyclerViewProductos.adapter = productsListAdapter
    }

    override fun onButtonAddClick(product: Product) {
        pedidoCambio = true
        pedidoGuardado.value = false
        agregarProductoAPedido(product)
    }

    private fun agregarProductoAPedido(product: Product) {
        val productoID = product.id
        var itemDePedido = getItemDePedidoSiExisteYAumentarEnUnoSiStatePermite(productoID)
        if (itemDePedido == null) {
            itemDePedido = viewModel.crearItemDePedido(product)
            pedidoConItems.add(itemDePedido)
        }
        cargarRecyclerDeCarrito(itemDePedido)
    }


    private fun cargarRecyclerDeCarrito(itemDePedidoAIluminar: ItemDePedido) {
        recyclerViewCarritoItems.setHasFixedSize(true)
        linearLayoutManagerCarrito = LinearLayoutManager(context)
        recyclerViewCarritoItems.layoutManager = linearLayoutManagerCarrito
        carritoItemListAdapter = CarritoItemsPedidoListAdapter(
            pedidoConItems,
            this,
            todosLosProductosDB,
            itemDePedidoAIluminar
        )
        recyclerViewCarritoItems.adapter = carritoItemListAdapter
        actualizarSumaPreviaDePedido()
    }

    private fun actualizarSumaPreviaDePedido() {
        if (pedidoConItems.isEmpty()) {
            binding.carritoDeTable.totalTable.text = "0.00"
        } else {
            binding.carritoDeTable.totalTable.text = sumatoriaSubTotalesDePedidoConItems()
        }
    }

    private fun sumatoriaSubTotalesDePedidoConItems(): String {
        var total = BigDecimal("0.00")
        for (itemPedido in pedidoConItems) {
            total = total.add(BigDecimal(itemPedido.subTotal))
        }
        return total.setScale(2, RoundingMode.HALF_DOWN).toPlainString()
    }

    private fun getItemDePedidoSiExisteYAumentarEnUnoSiStatePermite(productID: String): ItemDePedido? {
        var i = 0
        var buscado: ItemDePedido? = null
        while (buscado == null && i < pedidoConItems.size) {
            if (pedidoConItems[i].productoID == productID && pedidoConItems[i].mozoPuedeAumentarEnUno()) {
                buscado = pedidoConItems[i]
                pedidoConItems[i].agregarUnoYActualizarPrecio()
            }
            i++
        }
        return buscado
    }

    private fun getItemDePedidoSiExisteRestarEnUnoOEliminarSiStatePermite(productID: String): ItemDePedido? {
        var i = 0
        var buscado: ItemDePedido? = null
        while (buscado == null && i < pedidoConItems.size) {
            if (pedidoConItems[i].productoID == productID && pedidoConItems[i].mozoPuedeRestarEnUno()) {
                buscado = pedidoConItems[i]
                if (pedidoConItems[i].cantidadDeProductos == 1) {
                    eliminarItemDePedidoDeFormaSegura(pedidoConItems[i])
                } else {
                    pedidoConItems[i].restarUnoYActualizarPrecio()
                }
            }
            i++
        }
        return buscado
    }

    private fun eliminarItemDePedidoDeFormaSegura(itemDePedido: ItemDePedido): ItemDePedido? {
        var i = 0
        var buscado: ItemDePedido? = null
        while (buscado == null && i < pedidoConItems.size) {
            if (pedidoConItems[i].id == itemDePedido.id && pedidoConItems[i].mozoPuedeBorrar()) {
                buscado = pedidoConItems[i]
                pedidoConItems.remove(pedidoConItems[i])
            }
            i++
        }
        return buscado
    }


    private fun getNameDeCategoriaProducto(
        idCategoria: String,
        listaCategorias: MutableList<CategoriaProducto>
    ): String {
        var i = 0
        var buscado = ""
        while (buscado.isBlank() && i < listaCategorias.size) {
            if (listaCategorias[i].id == idCategoria) {
                buscado = listaCategorias[i].name
            }
            i++
        }
        return buscado
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


    //////// METODOS PARA BOTONES DE LOS ITEMS DEL CARRITO

    override fun onButtonAddClick(itemDePedido: ItemDePedido) {
        val itemPedido =
            getItemDePedidoSiExisteYAumentarEnUnoSiStatePermite(itemDePedido.productoID)
        if (itemPedido == null) {
            habilitarToast("No se pudo aumentar el item adicional")
        } else {
            cargarRecyclerDeCarrito(itemDePedido)
        }
        pedidoCambio = true
        pedidoGuardado.value = false
    }

    override fun onButtonMinusClick(itemDePedido: ItemDePedido) {
        val itemPedido =
            getItemDePedidoSiExisteRestarEnUnoOEliminarSiStatePermite(itemDePedido.productoID)
        if (itemPedido == null) {
            habilitarToast("No se pudo restar el item adicional")
        } else {
            cargarRecyclerDeCarrito(itemDePedido)
        }
        pedidoCambio = true
        pedidoGuardado.value = false
    }

    override fun onButtonRemoveClick(itemDePedido: ItemDePedido) {
        val itemPedido = eliminarItemDePedidoDeFormaSegura(itemDePedido)
        if (itemPedido == null) {
            habilitarToast("No se pudo eliminar el item")
        } else {
            cargarRecyclerDeCarrito(itemDePedido)
        }
        pedidoCambio = true
        pedidoGuardado.value = false
    }

    override fun onCardClick(itemDePedido: ItemDePedido) {
        if (itemDePedido.mozoPuedeAgregarComentarios()) {
            (vista.parent as? ViewGroup)?.removeView(vista)
            val dialog = BottomSheetDialog(this.requireContext())
            dialogRef = dialog
            dialog.setContentView(vista)
            cargarDatosParaDialogOrderItemComments(itemDePedido)
            // dialog.setCancelable(false)
            dialog.show()
            comentarios.requestFocus()
        } else {
            habilitarToast("MOZO YA NO PUEDE AGREGAR NOTAS A ESTE ITEM")
        }
    }

    fun cargarDatosParaDialogOrderItemComments(itemDePedido: ItemDePedido) {
        val editable = Editable.Factory.getInstance().newEditable(itemDePedido.comentarios)
        comentarios.editText?.text = editable
        quantity.text = itemDePedido.cantidadDeProductos.toString()
        productName.text = getNameProductByID(itemDePedido.productoID)
        subtotalPrice.text = itemDePedido.subTotal
        stateOrderItem.setTextColor(ItemDePedidoState.getColorState(itemDePedido.state))
        stateOrderItem.text = itemDePedido.state.nombre
        buttonGuardarAnotaciones.setOnClickListener {
            guardarAnotaciones(itemDePedido, comentarios.editText?.text.toString())
        }
    }

    private fun guardarAnotaciones(itemDePedido: ItemDePedido, comentarios: String) {
        val itemDePedidoActualizado =
            buscarItemDePedidoYAgregarComentarios(itemDePedido, comentarios)
        if (itemDePedidoActualizado != null) {
            cargarRecyclerDeCarrito(itemDePedido)
        } else {
            habilitarToast("No se pudo agregar comentarios al item")
        }
        dialogRef.dismiss()
        pedidoCambio = true
        pedidoGuardado.value = false
    }

    private fun buscarItemDePedidoYAgregarComentarios(
        itemDePedido: ItemDePedido,
        comentarios: String
    ): ItemDePedido? {
        var i = 0
        var buscado: ItemDePedido? = null
        while (buscado == null && i < pedidoConItems.size) {
            if (pedidoConItems[i].id == itemDePedido.id) {
                pedidoConItems[i].comentarios = comentarios
                buscado = pedidoConItems[i]
            }
            i++
        }
        return buscado
    }

    private fun getNameProductByID(productoID: String): String {
        var i = 0
        var buscado = ""
        while (buscado.isBlank() && i < todosLosProductosDB.size) {
            if (todosLosProductosDB[i].id == productoID) {
                buscado = todosLosProductosDB[i].name
            }
            i++
        }
        return buscado
    }


}
