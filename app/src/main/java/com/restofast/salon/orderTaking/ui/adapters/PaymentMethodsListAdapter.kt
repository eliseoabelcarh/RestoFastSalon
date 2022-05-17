package com.restofast.salon.orderTaking.ui.adapters

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.payment.FormaDePago

class PaymentMethodsListAdapter(
    private var listaPaymentMethodItems: MutableList<FormaDePago>,
    private val itemClickListener: OnButtonRemoveClickListener
) : RecyclerView.Adapter<PaymentMethodsListAdapter.PaymentMethodsHolder>() {

    interface OnButtonRemoveClickListener {
        fun onButtonRemoveClick(formaDePago: FormaDePago)
        fun onChangeImporte(lista: MutableList<FormaDePago>)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return (PaymentMethodsHolder(view))
    }

    override fun onBindViewHolder(holder: PaymentMethodsHolder, position: Int) {
        holder.setProductName(listaPaymentMethodItems[position].tipo)

        holder.getInputCash().editText?.doOnTextChanged { text, start, before, count ->
            listaPaymentMethodItems[position].importeCobrado = text.toString()
            Log.d("TAG--", "COBRADO:  ${listaPaymentMethodItems[position].importeCobrado}")
            itemClickListener.onChangeImporte(listaPaymentMethodItems)
        }
        holder.setInputCash(listaPaymentMethodItems[position].importeCobrado)
        holder.getButtonRemove().setOnClickListener {
            itemClickListener.onButtonRemoveClick(listaPaymentMethodItems[position])
        }

    }

    override fun getItemCount(): Int {
        return listaPaymentMethodItems.size
    }


    //INICIO de ProductsHolder
    class PaymentMethodsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setProductName(tipoFormaDePago: TipoFormaDePago) {
            val txt: TextView = view.findViewById(R.id.paymentMethodName)
            when (tipoFormaDePago) {
                TipoFormaDePago.EFECTIVO -> txt.text = tipoFormaDePago.name
                TipoFormaDePago.VENDEMAS -> txt.text = "TARJETA"
                else -> throw  Exception(" setProductName Error - no existe tipoFormaDePago")
            }
        }
        fun getButtonRemove(): Button {
            return view.findViewById(R.id.btnRemoveItemFormaPago)
        }
        fun setInputCash(monto:String){
            val txtInputLayout: TextInputLayout = view.findViewById(R.id.inputCash)
            val editable = Editable.Factory.getInstance().newEditable(monto)
            txtInputLayout.editText?.text = editable
        }
        fun getInputCash(): TextInputLayout {
            return view.findViewById(R.id.inputCash)
        }
    }//FIN de ProductsHolder


}