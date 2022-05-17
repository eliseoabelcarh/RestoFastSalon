package com.restofast.salon.orderTaking.ui.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.restofast.salon.databinding.DistribucionMesas1Binding
import com.restofast.salon.entities.enums.TableState
import com.restofast.salon.entities.formaters.TimeFormater
import com.restofast.salon.entities.order.Table

class TableDistributionAdapter constructor(

    private val restaurantIDDeEmpleado : String,
    private val sucursalIDDeEmpleado : String,
    private val context : Context,
    private val itemClickListener : OnTableClickListener
) {

    interface OnTableClickListener {
        fun onTableCardClick(table: Table)
    }

    fun getViewBindingSinEmparejar(): ViewBinding {
        when("$restaurantIDDeEmpleado-$sucursalIDDeEmpleado"){
            "CHIFA_FUNNY_CHEN-COMAS1" -> {
                val layoutInflater: LayoutInflater = LayoutInflater.from(context)
                val dist = DistribucionMesas1Binding.inflate(layoutInflater)
                val one: Int = 1111
                val two: Int = 2222
                val tree: Int = 3333
                val four: Int = 4444
                val five: Int = 5555
                val six: Int = 6666
                val seven: Int = 7777
                val eight: Int = 8888
                val nine: Int = 9999
                dist.card1.id = one
                dist.card2.id = two
                dist.card3.id = tree
                dist.card4.id = four
                dist.card5.id = five
                dist.card6.id = six
                dist.card7.id = seven
                dist.card8.id = eight
                dist.card9.id = nine
                return dist
            }
            else -> throw Exception("Errro no hay ")
        }
    }

    fun getViewBindingEmparejada(listaDeTables: MutableList<Table>) : ViewBinding {
        val codigoBuscado = "$restaurantIDDeEmpleado-$sucursalIDDeEmpleado"
        when (codigoBuscado) {
            "CHIFA_FUNNY_CHEN-COMAS1" -> {
                val dist = getViewBindingSinEmparejar()
                emparejar(dist as DistribucionMesas1Binding, listaDeTables)
                return dist
            }
            else -> {
                throw Exception("TablesDistributionView onCreate: no se encuentra Vista Sucursal para este Empleado")
            }
        }
    }


    private fun emparejar(binding: ViewBinding, listaDeTables: MutableList<Table>)  {
        if(binding is DistribucionMesas1Binding){
            val listaDeCardViews : MutableList<CardView> = ArrayList()
            val container = binding.containerSoloDeMesas
            val listaDeViews = getChildsFromLinearLayout(container)
            listaDeCardViews.addAll(getRecursive(listaDeViews))
            for (table in listaDeTables){
                for (card in listaDeCardViews){
                    if(table.cardViewID == card.id.toString()){
                        emparejarTableACardView(table,card)
                    }
                }
            }
        }
        else{
            throw Exception("ERROR EN actualizarValoresDeTablesDeDatabaseACardViews, no se encuentra tipo de binding")
        }
    }

    private fun emparejarTableACardView(table: Table, card: CardView) {
        val nroMesaTexto : TextView? = getTextFromCardView(card)
        //val imagenDeCard : ImageView? = getImageFromCardView(card)
        //val openingTime : TextView? = getOpeningTimeFromCard(card)

        if(nroMesaTexto != null/* && imagenDeCard != null && openingTime != null*/){
            //imagenDeCard.isVisible = table.showAlertIconMozo
            if(table.state == TableState.OPEN){
                card.setBackgroundColor(Color.GREEN)
            }
            else {
                card.setBackgroundColor(Color.LTGRAY)
            }
        }
        card.setOnClickListener {
            itemClickListener.onTableCardClick(table)
        }
    }

    private fun getImageFromCardView(card: CardView): ImageView? {
        var imageView : ImageView? = null
        val linear = card.getChildAt(0) as LinearLayout
        val listaDeViews: MutableList<View> = getChildsFromLinearLayout(linear)
        for (v in listaDeViews) {
            if (v is ImageView) {
                imageView = v
            }
        }
        return imageView
    }

    private fun getTextFromCardView(card: CardView): TextView? {
        var textView : TextView? = null
        val linear = card.getChildAt(0) as LinearLayout
        val listaDeViews: MutableList<View> = getChildsFromLinearLayout(linear)
        for (v in listaDeViews) {
            if (v is TextView) {
                textView = v
            }
        }
        return textView
    }

    private fun getOpeningTimeFromCard(card: CardView) : TextView?{
        var textView : TextView? = null
        val linear = card.getChildAt(1) as LinearLayout
        val listaDeViews: MutableList<View> = getChildsFromLinearLayout(linear)
        for (v in listaDeViews) {
            if (v is TextView) {
                textView = v
            }
        }
        return textView
    }

    private fun getChildsFromLinearLayout(view: LinearLayout): MutableList<View> {
        val lista : MutableList<View> = ArrayList()
        var cantChilds = view.childCount
        while (cantChilds > 0 ){
            val child : View = view.getChildAt(cantChilds-1)
            lista.add(child)
            cantChilds--
        }
        return lista
    }

    private fun getChildsFromConstraintLayout(view: ConstraintLayout): MutableList<View> {
        val lista : MutableList<View> = ArrayList()
        var cantChilds = view.childCount
        while (cantChilds > 0 ){
            val child : View = view.getChildAt(cantChilds-1)
            lista.add(child)
            cantChilds--
        }
        return lista
    }

    private fun getRecursive(listaDeViews : MutableList<View>): MutableList<CardView>{
        val listaDeCards : MutableList<CardView> = ArrayList()
        for ( child in listaDeViews ){
            if(child is CardView){
                listaDeCards.add(child)
            }
            else if(child is LinearLayout){
                val listaDeViews : MutableList<View> = getChildsFromLinearLayout(child)
                val cards = getRecursive(listaDeViews)
                listaDeCards.addAll(cards)
            }
            else if(child is ConstraintLayout){
                val listaDeViews : MutableList<View> = getChildsFromConstraintLayout(child)
                val cards = getRecursive(listaDeViews)
                listaDeCards.addAll(cards)
            }
            else{
                throw Exception("Error en getRecursive de TableDistAdapter: tipo de View inexistente")
            }
        }
        return listaDeCards
    }


}