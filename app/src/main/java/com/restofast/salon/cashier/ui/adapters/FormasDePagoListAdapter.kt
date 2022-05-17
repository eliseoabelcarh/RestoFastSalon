package com.restofast.salon.cashier.ui.adapters

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.payment.FormaDePago
import java.math.BigDecimal
import java.math.RoundingMode

class FormasDePagoListAdapter(
    private var listaPaymentMethodItems: MutableList<FormaDePago>,
    private val itemClickListener: OnButtonClickListener
) : RecyclerView.Adapter<FormasDePagoListAdapter.PaymentMethodsHolder>() {

    interface OnButtonClickListener {
        fun onButtonRemoveClick(formaDePago: FormaDePago)
        fun onChangeImporte(lista: MutableList<FormaDePago>)
        fun setearVuelto(
            listaFormasDePago: MutableList<FormaDePago>,
            position: Int
        ): MutableLiveData<FormaDePago>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return (PaymentMethodsHolder(view))
    }

    override fun onBindViewHolder(holder: PaymentMethodsHolder, position: Int) {
        holder.setProductName(listaPaymentMethodItems[position].tipo)



        holder.getInputCash().editText?.doOnTextChanged { text, start, before, count ->
            var importeClientePagaCon: String
            try {
                importeClientePagaCon = text.toString()
                val probando = BigDecimal(importeClientePagaCon).setScale(2, RoundingMode.HALF_DOWN)
                importeClientePagaCon = probando.toPlainString()
            } catch (e: Exception) {
                importeClientePagaCon = "0.00"
            }
            Log.d("TAG--", "PAGGAA CON_ $importeClientePagaCon")
            listaPaymentMethodItems[position].clientePagaCon = importeClientePagaCon

            itemClickListener.setearVuelto(listaPaymentMethodItems, position)
                .observeForever { formaDePago ->
                    if (formaDePago != null) {
                        holder.setVuelto(formaDePago)
                    }
                }
            itemClickListener.onChangeImporte(listaPaymentMethodItems)
        }

        holder.setVuelto(listaPaymentMethodItems[position])
        holder.setInputCash(listaPaymentMethodItems[position].clientePagaCon)

        holder.getButtonRemove().setOnClickListener {
            itemClickListener.onButtonRemoveClick(listaPaymentMethodItems[position])
        }

    }

    override fun getItemCount(): Int {
        return listaPaymentMethodItems.size
    }

    fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }

    //INICIO de ProductsHolder
    class PaymentMethodsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setProductName(tipoFormaDePago: TipoFormaDePago) {
            val txt: TextView = view.findViewById(R.id.paymentMethodName)
            when (tipoFormaDePago) {
                TipoFormaDePago.EFECTIVO -> txt.text = "EFECTIVO"
                TipoFormaDePago.VENDEMAS -> txt.text = "TARJETA"
                else -> throw  Exception(" setProductName Error - no existe tipoFormaDePago")
            }
        }

        fun getButtonRemove(): Button {
            return view.findViewById(R.id.btnRemoveItemFormaPago)
        }

        fun setInputCash(monto: String) {
            if (monto.isNotBlank() && monto != "0.00") {
                val txtInputLayout: TextInputLayout = view.findViewById(R.id.inputCash)
                val editable = Editable.Factory.getInstance().newEditable(monto)
                txtInputLayout.editText?.text = editable
            }
        }

        fun getInputCash(): TextInputLayout {
            return view.findViewById(R.id.inputCash)
        }

        fun setVuelto(formaDePago: FormaDePago) {
            val vuelto = formaDePago.vuelto

            val containerVuelto: LinearLayout = view.findViewById(R.id.containerDeVuelto)
            if (vuelto.isNotBlank() && vuelto != "0.00") {
                containerVuelto.visibility = View.VISIBLE
                val txt: TextView = view.findViewById(R.id.importeVueltoPaymentMethod)
                txt.text = vuelto
            } else {
                containerVuelto.visibility = View.GONE
            }
        }


    }//FIN de ProductsHolder


}