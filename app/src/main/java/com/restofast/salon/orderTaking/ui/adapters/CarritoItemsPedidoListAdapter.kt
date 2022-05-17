package com.restofast.salon.orderTaking.ui.adapters

import android.net.wifi.p2p.WifiP2pManager
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.enums.ItemDePedidoState
import com.restofast.salon.entities.order.ItemDePedido
import com.restofast.salon.entities.products.Product
import android.view.animation.AlphaAnimation
import java.util.*


class CarritoItemsPedidoListAdapter (
    private var listaCarritoItems: MutableList<ItemDePedido>,
    private val itemClickListener: OnCarritoItemClickListener,
    private val listaDeProductos : MutableList<Product>,
    private val itemDePedidoAIluminar : ItemDePedido,

    ) : RecyclerView.Adapter<CarritoItemsPedidoListAdapter.CarritoItemsHolder>() {

    interface OnCarritoItemClickListener {
        fun onButtonAddClick(orderItem: ItemDePedido)
        fun onButtonMinusClick(orderItem: ItemDePedido)
        fun onButtonRemoveClick(orderItem: ItemDePedido)
        fun onCardClick(orderItem: ItemDePedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carritoitem, parent, false)
        return (CarritoItemsHolder(view,listaDeProductos,itemDePedidoAIluminar))
    }

    override fun onBindViewHolder(holder: CarritoItemsHolder, position: Int) {

        holder.setGenericSymbols(listaCarritoItems[position])

        holder.setProductItemName(listaCarritoItems[position])
        holder.setQuantityItem(listaCarritoItems[position])
        holder.setSubtotalItem(listaCarritoItems[position])
        holder.setComments(listaCarritoItems[position])


        holder.setOrderItemState(listaCarritoItems[position])

        holder.setButtonMinusOne(listaCarritoItems[position].state)
        holder.setButtonAddOne(listaCarritoItems[position].state)
        holder.setButtonRemoveItem(listaCarritoItems[position].state)

        holder.getButtonMinusOne().setOnClickListener {
            itemClickListener.onButtonMinusClick(listaCarritoItems[position])
        }
        holder.getButtonAddOne().setOnClickListener {
            itemClickListener.onButtonAddClick(listaCarritoItems[position])
        }
        holder.getButtonRemoveItem().setOnClickListener {
            itemClickListener.onButtonRemoveClick(listaCarritoItems[position])
        }
        holder.getCard().setOnClickListener {
            itemClickListener.onCardClick(listaCarritoItems[position])
        }
    }

    override fun getItemCount(): Int {
        return listaCarritoItems.size
    }


    //INICIO de CarritoItemsHolder
    class CarritoItemsHolder(v: View,listaDeProductos: MutableList<Product>, itemDePedidoNuevo: ItemDePedido) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var listaDeProductosHolder = listaDeProductos
        private var itemDePedidoAIluminar = itemDePedidoNuevo


        private fun setearColorSiEsAnulado(txt: TextView, orderItem: ItemDePedido) {
            if(orderItem.state == ItemDePedidoState.ANULADO){
                txt.setTextColor(ItemDePedidoState.getColorState(orderItem.state))
            }
        }

        fun setButtonMinusOne(state: ItemDePedidoState){
            val btn : Button = view.findViewById(R.id.btnMinusOne)
            setDefaults(btn,state)
        }
        fun setButtonAddOne(state: ItemDePedidoState){
            val btn : Button = view.findViewById(R.id.btnAddOne)
            setDefaults(btn,state)
        }

        fun setButtonRemoveItem(state: ItemDePedidoState){
            val btn : Button = view.findViewById(R.id.btnRemoveItem)
            setDefaults(btn,state)
        }
        fun setDefaults(btn: Button, state: ItemDePedidoState){
            when(state){
                ItemDePedidoState.FALTA_ACEPTAR -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                ItemDePedidoState.ESPERANDO_ACEPTACION -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                ItemDePedidoState.PREPARANDO -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
                ItemDePedidoState.SERVIDO -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
                ItemDePedidoState.ENTREGA_MOZO -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                else -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
            }
        }

        fun setProductItemName(orderItem: ItemDePedido) {
            val name = getNameProductByID(orderItem.productoID)
            val txt: TextView = view.findViewById(R.id.productItemName)
            txt.text = name
            setearColorSiEsAnulado(txt,orderItem)
        }

        private fun getNameProductByID(productoID: String): String {
            var i = 0
            var buscado = ""
            while (buscado.isBlank() && i < listaDeProductosHolder.size){
                if(listaDeProductosHolder[i].id == productoID){
                    buscado = listaDeProductosHolder[i].name
                }
                i++
            }
            return buscado
        }

        fun setQuantityItem(orderItem: ItemDePedido) {
            val cantidad = orderItem.cantidadDeProductos
            val txt: TextView = view.findViewById(R.id.quantityItem)
            txt.text = cantidad.toString()
            setearColorSiEsAnulado(txt,orderItem)
            if(orderItem.id == itemDePedidoAIluminar.id){
                agregarEfectoFadeInParaCantidad(txt)
            }
        }

        private fun agregarEfectoFadeInParaCantidad(txt: TextView){
            val fadeIn = AlphaAnimation(0.0f, 1.0f)
            fadeIn.duration = 450;
            fadeIn.fillAfter = true;
            txt.animation = fadeIn
        }

        fun setSubtotalItem(orderItem: ItemDePedido) {
            val subtotal = orderItem.subTotal
            val txt: TextView = view.findViewById(R.id.subtotalPrice)
            txt.text = subtotal
            setearColorSiEsAnulado(txt,orderItem)
        }
        fun setComments(orderItem: ItemDePedido){
            val comments = orderItem.comentarios
            val txt: TextView = view.findViewById(R.id.orderItemComentarios)
            if(comments.isNotBlank()){
                txt.visibility = View.VISIBLE
                txt.text = comments.uppercase(Locale.getDefault())
                setearColorSiEsAnulado(txt,orderItem)
                if(orderItem.id == itemDePedidoAIluminar.id){
                    agregarEfectoFadeInParaComentarios(txt)
                }
            }else{
                txt.visibility = View.GONE
            }
        }
        private fun agregarEfectoFadeInParaComentarios(txt: TextView){
            val fadeIn = AlphaAnimation(0.0f, 1.0f)
            fadeIn.duration = 1000;
            fadeIn.fillAfter = true;
            txt.animation = fadeIn
        }

        fun setGenericSymbols(orderItem: ItemDePedido){
            val simboloMoneda: TextView = view.findViewById(R.id.simboloMonedaDeCarritoMesa)
            val simboloX : TextView = view.findViewById(R.id.simboloXDeCarritoMesa)
            setearColorSiEsAnulado(simboloMoneda,orderItem)
            setearColorSiEsAnulado(simboloX,orderItem)
        }

        fun setOrderItemState(orderItem: ItemDePedido) {
            val txt: TextView = view.findViewById(R.id.stateItem)
            txt.text = orderItem.state.nombre.uppercase(Locale.getDefault())
            txt.setTextColor(ItemDePedidoState.getColorState(orderItem.state))
        }

        fun getButtonMinusOne(): Button {
            return view.findViewById(R.id.btnMinusOne)
        }

        fun getButtonAddOne(): Button {
            return view.findViewById(R.id.btnAddOne)
        }

        fun getButtonRemoveItem(): Button {
            return view.findViewById(R.id.btnRemoveItem)
        }
        fun getCard(): CardView {
            return view.findViewById(R.id.card_item_carritoitem)
        }


    }//FIN de CarritoItemsHolder



}