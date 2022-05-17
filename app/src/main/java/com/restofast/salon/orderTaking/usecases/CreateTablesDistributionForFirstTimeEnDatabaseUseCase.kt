package com.restofast.salon.orderTaking.usecases

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.viewbinding.ViewBinding
import com.restofast.salon.databinding.DistribucionMesas1Binding
import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class CreateTablesDistributionForFirstTimeEnDatabaseUseCase(
    var repoDBTables: RepoDBTables,
    var repoDBUsers: RepoDBUsers
) {

    suspend operator fun invoke(binding: ViewBinding) = coroutineScope {

        //esto es para ChifaFunnyChenSucursalUno
        if (binding is DistribucionMesas1Binding) {
            val listaDeMesas: MutableList<Table> = ArrayList()
            val listaDeCardViews: MutableList<CardView> = ArrayList()
            val container = binding.containerSoloDeMesas
            val listaDeViews = getChildsFromLinearLayout(container)
            listaDeCardViews.addAll(getRecursive(listaDeViews))
            for (card in listaDeCardViews) {
                val id = repoDBTables.getRandomID()
                val nroMesa = getTextFromCardView(card)?.text.toString()
                val cardViewID = card.id.toString()
                val restauranteID = repoDBUsers.getRestaurantIDDeCurrentEmpleado()
                val sucursalID = repoDBUsers.getSucursalIDDeCurrentEmpleado()
                val table = Table(
                    id,
                    nroMesa,
                    cardViewID,
                    restauranteID,
                    sucursalID
                )
                listaDeMesas.add(table)
            }

            repoDBTables.createListaDeMesasEnDB(listaDeMesas)


        } else {
            throw Exception("Error: CreateTablesDistributionForFirstTimeEnDatabaseUseCase: NO EXISTE TIPO")
        }

    }


    private fun getChildsFromLinearLayout(view: LinearLayout): MutableList<View> {
        val lista: MutableList<View> = ArrayList()
        var cantChilds = view.childCount
        while (cantChilds > 0) {
            val child: View = view.getChildAt(cantChilds - 1)
            lista.add(child)
            cantChilds--
        }
        return lista
    }

    private fun getChildsFromConstraintLayout(view: ConstraintLayout): MutableList<View> {
        val lista: MutableList<View> = ArrayList()
        var cantChilds = view.childCount
        while (cantChilds > 0) {
            val child: View = view.getChildAt(cantChilds - 1)
            lista.add(child)
            cantChilds--
        }
        return lista
    }

    private fun getRecursive(listaDeViews: MutableList<View>): MutableList<CardView> {
        val listaDeCards: MutableList<CardView> = ArrayList()
        for (child in listaDeViews) {
            if (child is CardView) {
                listaDeCards.add(child)
            } else if (child is LinearLayout) {
                val listaDeViews: MutableList<View> = getChildsFromLinearLayout(child)
                val cards = getRecursive(listaDeViews)
                listaDeCards.addAll(cards)
            } else if (child is ConstraintLayout) {
                Log.d("TAG--", "es contsraint layout")
                val listaDeViews: MutableList<View> = getChildsFromConstraintLayout(child)
                Log.d("TAG--", "listsize ${listaDeViews.size}")
                val cards = getRecursive(listaDeViews)
                listaDeCards.addAll(cards)
            } else if (child is Guideline) {
                //NO HAGO NADA
            } else {
                throw Exception("error en createTableDistributuionFirstTime: tipo de View inexistente")
            }


        }
        return listaDeCards
    }

    private fun getImageFromCardView(card: CardView): ImageView? {
        var imageView: ImageView? = null
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
        var textView: TextView? = null
        val linear = card.getChildAt(0) as LinearLayout
        val listaDeViews: MutableList<View> = getChildsFromLinearLayout(linear)
        for (v in listaDeViews) {
            if (v is TextView) {
                textView = v
            }
        }
        return textView
    }


}