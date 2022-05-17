package com.restofast.salon.cashier.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.enums.OrderItemState
import com.restofast.salon.entities.formaters.TimeCalculator
import com.restofast.salon.entities.order.OrderItem

class OrderItemsCloseOrderListAdapter (
    private var listaOrderItems: MutableList<OrderItem>
) : RecyclerView.Adapter<OrderItemsCloseOrderListAdapter.OrderItemsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detalle_orderitem_en_close_order, parent, false)
        return (OrderItemsHolder(view))
    }

    override fun getItemCount(): Int {
        return listaOrderItems.size
    }

    override fun onBindViewHolder(holder: OrderItemsHolder, position: Int) {

        holder.setCantidad(listaOrderItems[position])
        holder.setSimboloX(listaOrderItems[position])
        holder.setNombreProducto(listaOrderItems[position])
        holder.setTituloPrecioUnitario(listaOrderItems[position])
        holder.setSimboloCurrencyPrecioUnitario(listaOrderItems[position])
        holder.setImportePrecioUnitario(listaOrderItems[position])
        holder.setSimboloCurrencyDeOrderItem(listaOrderItems[position])
        holder.setImporteOrderItem(listaOrderItems[position])
        holder.setColoresDeTituloEstadoAmbos(listaOrderItems[position])
        holder.setVisibilidadDeOpcionEstadoSegunStateOrderItem(listaOrderItems[position])
        holder.setTiempoMinutosHastaServido(listaOrderItems[position])
        holder.setVisibilidadAnotaciones(listaOrderItems[position])
        holder.setVisibilidadMotivoAnulacion(listaOrderItems[position])


    }


    //INICIO de CarritoItemsHolder
    class OrderItemsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setCantidad(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.cantidadOrderItemEnCloseOrder)
            txt.text = orderItem.qty.toString()
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setSimboloX(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.simboloXenCloseOrder)
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setNombreProducto(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.nombreProductoEnCloseOrder)
            txt.text= orderItem.product!!.name
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setTituloPrecioUnitario(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.tituloPrecioUnitarioEnCloseOrder)
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setSimboloCurrencyPrecioUnitario(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.simboloCurrencyEnCloseOrder)
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setImportePrecioUnitario(orderItem: OrderItem){
           /* val txt : TextView = view.findViewById(R.id.importeDePrecioUnitarioEnCloseOrder)
            txt.text = orderItem.product!!.sellPrice
            setColorSegunOrderItemState(orderItem,txt)*/
        }
        fun setSimboloCurrencyDeOrderItem(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.simboloCurrencyOrderItemEnCloseOrder)
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setImporteOrderItem(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.importeDeOrderItemEnCloseOrder)
            txt.text = orderItem.subTotal
            setColorSegunOrderItemState(orderItem,txt)
        }
        fun setColoresDeTituloEstadoAmbos(orderItem: OrderItem){
            val txt1 : TextView = view.findViewById(R.id.tituloEstadoOpc1EnCloseOrder)
            val txt2 : TextView = view.findViewById(R.id.tituloEstadoOpc2EnCloseOrder)
            setColorSegunOrderItemState(orderItem,txt1)
            setColorSegunOrderItemState(orderItem,txt2)
        }
        fun setVisibilidadDeOpcionEstadoSegunStateOrderItem(orderItem: OrderItem){
            val containerServido : LinearLayout = view.findViewById(R.id.opcionServidoEnCloseOrder)
            val containerDiferente : LinearLayout = view.findViewById(R.id.opcionDiferentesAServido)
            if(orderItem.orderItemState == OrderItemState.SERVIDO){
                containerServido.visibility = View.VISIBLE
                containerDiferente.visibility = View.GONE
                val txt : TextView = view.findViewById(R.id.estadoActualServidoCloseOrder)
                setNombreYColorParaEstadoDeOrderItem(orderItem,txt)
            }else{
                containerServido.visibility = View.GONE
                containerDiferente.visibility = View.VISIBLE
                val txt : TextView = view.findViewById(R.id.estadoActualDiferenteCloseOrder)
                setNombreYColorParaEstadoDeOrderItem(orderItem,txt)
            }
        }

        fun setTiempoMinutosHastaServido(orderItem: OrderItem){
            val txt : TextView = view.findViewById(R.id.tiempoMinutosDeServidoCloseOrder)
            val fechaHoraInicio =orderItem.openingTime
            val fechaHoraServido = orderItem.closingTime
            if(orderItem.orderItemState == OrderItemState.SERVIDO){
                if(fechaHoraInicio == null || fechaHoraServido == null){
                    throw Exception("fechaInicio o fecha servido son NULL")
                }
                txt.text = TimeCalculator.calcularDiferenciaDeMinutos(fechaHoraInicio, fechaHoraServido)
            }
        }

        fun setVisibilidadAnotaciones(orderItem: OrderItem){
            val containerAnotaciones : LinearLayout = view.findViewById(R.id.containerAnotacionesEnCloseOrder)
            if(orderItem.comentarios.isBlank()){
                containerAnotaciones.visibility = View.GONE
            }else{
                containerAnotaciones.visibility = View.VISIBLE
                val txtTituloAnotaciones : TextView = view.findViewById(R.id.tituloAnotacionesEnCloseOrder)
                setColorSegunOrderItemState(orderItem,txtTituloAnotaciones)
                val txtComentarios : TextView = view.findViewById(R.id.comentariosDeOrderItemEnCloseOrder)
                txtComentarios.text = orderItem.comentarios
                setColorSegunOrderItemState(orderItem,txtComentarios)
            }
        }
        fun setVisibilidadMotivoAnulacion(orderItem: OrderItem){
            val containerAnulaciones : LinearLayout = view.findViewById(R.id.containerAnulacionEnCloseOrder)
            if(orderItem.motivoAnulacion.isBlank()){
                containerAnulaciones.visibility = View.GONE
            }else{
                containerAnulaciones.visibility = View.VISIBLE
                val txtTituloMotivoAnulacion : TextView = view.findViewById(R.id.tituloMotivoanulacionEnCloseOrder)
                txtTituloMotivoAnulacion.setTextColor(Color.GRAY)
                val txtMotivo : TextView = view.findViewById(R.id.motivoDeAnulacionDeOrderItemEnCloseOrder)
                txtMotivo.text = orderItem.motivoAnulacion
                txtMotivo.setTextColor(Color.GRAY)
            }
        }




        private fun setNombreYColorParaEstadoDeOrderItem(orderItem: OrderItem, txt: TextView){
            when (orderItem.orderItemState) {
                OrderItemState.FALTA_ACEPTAR -> {
                    txt.text = "FALTA ACEPTAR"
                    txt.setTextColor(Color.argb(255,255,136,49))
                }
                OrderItemState.ESPERANDO_ACEPTACION -> {
                    txt.text = "ESPERANDO ACEPTACION"
                    txt.setTextColor(Color.argb(255,0,150,255))
                }
                OrderItemState.PREPARANDO -> {
                    txt.text = "PREPARANDO"
                    txt.setTextColor(Color.argb(255,0,214,29))
                }
                OrderItemState.SERVIDO -> {
                    txt.text = "SERVIDO"
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
                    txt.text = orderItem.orderItemState.name
                }
            }
        }

        private fun setColorSegunOrderItemState(orderItem: OrderItem, textView: TextView) {
            if(orderItem.orderItemState == OrderItemState.ANULADO){
                textView.setTextColor(Color.LTGRAY)
            }
        }

    }//FIN de CarritoItemsHolder



}