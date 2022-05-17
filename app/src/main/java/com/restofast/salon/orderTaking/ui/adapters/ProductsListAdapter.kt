package com.restofast.salon.orderTaking.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.restofast.salon.R
import com.restofast.salon.entities.products.Product

class ProductsListAdapter(
    private var listaProductos: MutableList<Product>,
    private val itemClickListener: OnButtonAddProductClickListener
) : RecyclerView.Adapter<ProductsListAdapter.ProductsHolder>() {

    interface OnButtonAddProductClickListener {
        fun onButtonAddClick(product: Product)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return (ProductsHolder(view))
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.setProductName(listaProductos[position].name)
        holder.setProductPrice(listaProductos[position].price)
        holder.getButtonAddProduct().setOnClickListener {
            itemClickListener.onButtonAddClick(listaProductos[position])
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }


    //INICIO de ProductsHolder
    class ProductsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setProductName(name: String) {
            val txt: TextView = view.findViewById(R.id.productName)
            txt.text = name
        }

        fun setProductPrice(price: String) {
            val txt: TextView = view.findViewById(R.id.priceProduct)
            txt.text = price
        }

        fun getButtonAddProduct(): Button {
            return view.findViewById(R.id.btnAddProduct)
        }
    }//FIN de ProductsHolder


}