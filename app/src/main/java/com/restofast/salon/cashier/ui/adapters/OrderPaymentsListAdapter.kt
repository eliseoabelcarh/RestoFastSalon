package com.restofast.salon.cashier.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.payment.FormaDePago

class OrderPaymentsListAdapter (
    private var listaFormasDePagoItems: MutableList<FormaDePago>
) : RecyclerView.Adapter<OrderPaymentsListAdapter.FormasDePagoItemsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormasDePagoItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forma_de_pago_e_importe, parent, false)
        return (FormasDePagoItemsHolder(view))
    }

    override fun getItemCount(): Int {
        return listaFormasDePagoItems.size
    }

    override fun onBindViewHolder(holder: FormasDePagoItemsHolder, position: Int) {

        holder.setTipoNombre(listaFormasDePagoItems[position])
        holder.setImporte(listaFormasDePagoItems[position])

    }


    //INICIO de CarritoItemsHolder
    class FormasDePagoItemsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setTipoNombre(formaDePago: FormaDePago){
            val txt : TextView = view.findViewById(R.id.paymentMethodNameEnFormaDePagoEImporte)
            when(formaDePago.tipo){
                TipoFormaDePago.EFECTIVO -> txt.text = "EFECTIVO"
                TipoFormaDePago.VENDEMAS -> txt.text = "TARJETA"
                else -> txt.text = formaDePago.tipo.name
            }
        }
        fun setImporte(formaDePago: FormaDePago){
            val txt : TextView = view.findViewById(R.id.importeEnFormaDePagoEImporte)
            txt.text = formaDePago.importeCobrado
        }
    }//FIN de CarritoItemsHolder


}