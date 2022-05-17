package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.ItemDePedido
import com.restofast.salon.entities.products.Product
import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBUsers
import java.util.*

class CrearItemDePedidoUseCase (var repoDBUsers: RepoDBUsers, var repoDBOrders: RepoDBOrders) {

     operator fun invoke(product:Product) : ItemDePedido {
        return ItemDePedido(
            id = repoDBOrders.getRandomID(),
            cantidadDeProductos = 1,
            productoID = product.id,
            categoriaProductoID = product.categoriaProductoID,
            precioUnitarioDeProducto = product.price ,
            subTotal = product.price,
            openingTime = Date(),
            empleadoIDQueCreaItemDePedido = repoDBUsers.getCurrentUserUIDNORMAL(),

        )
    }
}