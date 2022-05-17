package com.restofast.salon.orderTaking.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.formaters.TimeFormater
import com.restofast.salon.entities.order.PedidoToGo

class PedidosToGoListAdapter(
    private var listaPedidosToGo: MutableList<PedidoToGo>,
    private val itemClickListener: OnClickListener
) : RecyclerView.Adapter<PedidosToGoListAdapter.OrdersToGoHolder>() {

    interface OnClickListener {
        fun onClickCardOrderToGo(pedidoToGo: PedidoToGo)
        fun onClickRemoveOrderToGo(pedidoToGo: PedidoToGo)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersToGoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_to_go, parent, false)
        return (OrdersToGoHolder(view, listaPedidosToGo.size))
    }

    override fun onBindViewHolder(holder: OrdersToGoHolder, position: Int) {
        holder.setNombrecliente(listaPedidosToGo[position].customerName)
        holder.setHoraIniciado(listaPedidosToGo[position])
        holder.setCard(position)
        holder.getCard().setOnClickListener {
            itemClickListener.onClickCardOrderToGo(listaPedidosToGo[position])
        }
        holder.showButtonRemoveAndNoItemsMessage(listaPedidosToGo[position])
        holder.getButtonRemovePedidoToGo().setOnClickListener {
            itemClickListener.onClickRemoveOrderToGo(listaPedidosToGo[position])
        }
    }

    override fun getItemCount(): Int {
        return listaPedidosToGo.size
    }


    //INICIO de OrdersToGoHolder
    class OrdersToGoHolder(v: View, sizeLista: Int) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var cantDePedidos: Int = sizeLista

        fun getCard(): CardView {
            return view.findViewById(R.id.cardOrderToGo)
        }

        fun setCard(position: Int) {
            if (position == 0) {
                agregarEfectoFadeInACard(getCard())
            }
        }

        fun setNombrecliente(name: String) {
            val txt: TextView = view.findViewById(R.id.nombreClienteOrderToGoItem)
            txt.text = name
        }

        fun setHoraIniciado(pedidoToGo: PedidoToGo) {
            val txt: TextView = view.findViewById(R.id.horaIniciadoItemOrderToGo)
            txt.text = TimeFormater.formatearTimestampAString(pedidoToGo.openingTime)
        }

        fun showButtonRemoveAndNoItemsMessage(pedidoToGo: PedidoToGo) {
            val linearLayoutDeNoItems: LinearLayout =
                view.findViewById(R.id.containerMensajePedidoSinItemsOrderToGo)
            if (pedidoToGo.itemsDePedido.isEmpty()) {
                linearLayoutDeNoItems.visibility = View.VISIBLE
                getButtonRemovePedidoToGo().visibility = View.VISIBLE
            } else {
                linearLayoutDeNoItems.visibility = View.GONE
                getButtonRemovePedidoToGo().visibility = View.GONE
            }

        }

        fun getButtonRemovePedidoToGo(): Button {
            return view.findViewById(R.id.buttonRemovePedidoToGo)
        }

        fun agregarEfectoFadeInACard(card: CardView) {
            val fadeIn = AlphaAnimation(0.0f, 1.0f)
            fadeIn.duration = 1000;
            fadeIn.fillAfter = true;
            card.animation = fadeIn;
        }


    }//FIN de OrdersToGoHolder


}