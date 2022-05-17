package com.restofast.salon.orderTaking.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.enums.OrderItemState
import com.restofast.salon.entities.order.OrderItem

class CarritoItemListAdapter(
    private var listaCarritoItems: MutableList<OrderItem>,
    private val itemClickListener: OnCarritoItemClickListener
) : RecyclerView.Adapter<CarritoItemListAdapter.CarritoItemsHolder>() {

    interface OnCarritoItemClickListener {
        fun onButtonAddClick(orderItem: OrderItem)
        fun onButtonMinusClick(orderItem: OrderItem)
        fun onButtonRemoveClick(orderItem: OrderItem)
        fun onCardClick(orderItem: OrderItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carritoitem, parent, false)
        return (CarritoItemsHolder(view))
    }

    override fun onBindViewHolder(holder: CarritoItemsHolder, position: Int) {

        holder.setGenericSymbols(listaCarritoItems[position])

        holder.setProductItemName(listaCarritoItems[position])
        holder.setQuantityItem(listaCarritoItems[position])
        holder.setSubtotalItem(listaCarritoItems[position])
        holder.setComments(listaCarritoItems[position])


        holder.setOrderItemState(listaCarritoItems[position].orderItemState)

        holder.setButtonMinusOne(listaCarritoItems[position].orderItemState)
        holder.setButtonAddOne(listaCarritoItems[position].orderItemState)
        holder.setButtonRemoveItem(listaCarritoItems[position].orderItemState)

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
    class CarritoItemsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setButtonMinusOne(state: OrderItemState){
            val btn : Button = view.findViewById(R.id.btnMinusOne)
            setDefaults(btn,state)
        }
        fun setButtonAddOne(state: OrderItemState){
            val btn : Button = view.findViewById(R.id.btnAddOne)
            setDefaults(btn,state)
        }

        fun setButtonRemoveItem(state: OrderItemState){
            val btn : Button = view.findViewById(R.id.btnRemoveItem)
            setDefaults(btn,state)
        }
        fun setDefaults(btn: Button, state: OrderItemState){
            when(state){
                OrderItemState.FALTA_ACEPTAR -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                OrderItemState.ESPERANDO_ACEPTACION -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                OrderItemState.PREPARANDO -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
                OrderItemState.SERVIDO -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
                OrderItemState.ENTREGA_MOZO -> {
                    btn.isEnabled = true
                    btn.isVisible = true
                }
                else -> {
                    btn.isEnabled = false
                    btn.isVisible = false
                }
            }
        }
        fun setProductItemName(orderItem: OrderItem) {
            val name = orderItem.product?.name
            val txt: TextView = view.findViewById(R.id.productItemName)
            txt.text = name
            if(orderItem.orderItemState == OrderItemState.ANULADO){
                txt.setTextColor(Color.LTGRAY)
            }
        }

        fun setQuantityItem(orderItem: OrderItem) {
            val cantidad = orderItem.qty
            val txt: TextView = view.findViewById(R.id.quantityItem)
            txt.text = cantidad.toString()
            if(orderItem.orderItemState == OrderItemState.ANULADO){
                txt.setTextColor(Color.LTGRAY)
            }
        }

        fun setSubtotalItem(orderItem: OrderItem) {
            val subtotal = orderItem.subTotal
            val txt: TextView = view.findViewById(R.id.subtotalPrice)
            txt.text = subtotal
            if(orderItem.orderItemState == OrderItemState.ANULADO){
                txt.setTextColor(Color.LTGRAY)
            }
        }
        fun setComments(orderItem: OrderItem){
            val comments = orderItem.comentarios
            val txt: TextView = view.findViewById(R.id.orderItemComentarios)
            if(comments.isNotBlank()){
                txt.visibility = View.VISIBLE
                txt.text = comments
                if(orderItem.orderItemState == OrderItemState.ANULADO){
                    txt.setTextColor(Color.LTGRAY)
                }
            }else{
                txt.visibility = View.GONE
            }
        }

        fun setGenericSymbols(orderItem: OrderItem){
            val simboloMoneda: TextView = view.findViewById(R.id.simboloMonedaDeCarritoMesa)
            val simboloX : TextView = view.findViewById(R.id.simboloXDeCarritoMesa)
            if(orderItem.orderItemState == OrderItemState.ANULADO){
                simboloMoneda.setTextColor(Color.LTGRAY)
                simboloX.setTextColor(Color.LTGRAY)
            }
        }

        fun setOrderItemState(state: OrderItemState) {
            val txt: TextView = view.findViewById(R.id.stateItem)
            when (state) {
                OrderItemState.FALTA_ACEPTAR -> {
                    txt.text = "FALTA ACEPTAR"
                    txt.setTextColor(Color.argb(255,255,136,49))
                }
                OrderItemState.ESPERANDO_ACEPTACION -> {
                    txt.text = "ESPERANDO ACEPTACION"
                    txt.setTextColor(Color.argb(255,0,150,255))
                }
                OrderItemState.PREPARANDO -> {
                    txt.text = state.name
                    txt.setTextColor(Color.argb(255,0,214,29))
                }
                OrderItemState.SERVIDO -> {
                    txt.text = state.name
                    txt.setTextColor(Color.RED)
                }
                OrderItemState.ENTREGA_MOZO -> {
                    txt.text = "ENTREGA MOZO"
                    txt.setTextColor(Color.rgb(60,60,60))
                }
                OrderItemState.ANULADO -> {
                    txt.text = "ANULADO"
                    txt.setTextColor(Color.LTGRAY)
                }
                else -> {
                    txt.text = state.name
                }
            }
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