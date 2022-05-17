package com.restofast.salon.orderTaking.usecases



import com.restofast.salon.entities.products.Product
import com.restofast.salon.sharedData.data.RepoDBProducts


class CreateProductoEnDatabaseUseCase(
    var repoDBProducts: RepoDBProducts,

) {

    suspend operator fun invoke(product: Product) {
        repoDBProducts.createProductoEnDatabase(product)
    }

}