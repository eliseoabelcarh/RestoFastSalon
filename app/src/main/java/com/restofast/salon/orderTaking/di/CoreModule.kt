package com.restofast.salon.orderTaking.di

import com.restofast.salon.orderTaking.usecases.*
import com.restofast.salon.sharedData.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {


    @Provides
    fun providesGetListaDeTablesFromDatabaseUseCase(repoDBTables: RepoDBTables): GetListaDeTablesFromDatabaseUseCase =
        GetListaDeTablesFromDatabaseUseCase(repoDBTables)

    @Provides
    fun providesCreateTablesDistributionForFirstTimeEnDatabaseUseCase(
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers
    ): CreateTablesDistributionForFirstTimeEnDatabaseUseCase =
        CreateTablesDistributionForFirstTimeEnDatabaseUseCase(repoDBTables, repoDBUsers)


    @Provides
    fun providesGetTablesDistributionOneTimeUseCase(repoDBTables: RepoDBTables): GetTablesDistributionOneTimeUseCase =
        GetTablesDistributionOneTimeUseCase(repoDBTables)


    @Provides
    fun providesCreateProductoEnDatabaseUseCase(
        repoDBProducts: RepoDBProducts
    ): CreateProductoEnDatabaseUseCase =
        CreateProductoEnDatabaseUseCase(repoDBProducts)

    @Provides
    fun providesGetPlatosDatabaseByOwnerIDUseCase(repoDBProducts: RepoDBProducts): GetPlatosDatabaseByOwnerIDUseCase =
        GetPlatosDatabaseByOwnerIDUseCase(repoDBProducts)

    @Provides
    fun providesGetBebidasDatabaseByOwnerIDUseCase(repoDBProducts: RepoDBProducts): GetBebidasDatabaseByOwnerIDUseCase =
        GetBebidasDatabaseByOwnerIDUseCase(repoDBProducts)

    @Provides
    fun providesAddProductToTableUseCase(
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers
    ): AddProductToTableUseCase =
        AddProductToTableUseCase(repoDBTables, repoDBUsers)


    @Provides
    fun providesGetTableDBUseCase(repoDBTables: RepoDBTables): GetTableDBUseCase =
        GetTableDBUseCase(repoDBTables)

    @Provides
    fun providesRemoveProductFromOrderUseCase(repoDBTables: RepoDBTables): RemoveProductFromOrderUseCase =
        RemoveProductFromOrderUseCase(repoDBTables)

    @Provides
    fun providesRemoveCompleteOrderItemFromOrderUseCase(repoDBTables: RepoDBTables): RemoveCompleteOrderItemFromOrderUseCase =
        RemoveCompleteOrderItemFromOrderUseCase(repoDBTables)


    @Provides
    fun providesEnviarPedidosACocinaUseCase(repoDBTables: RepoDBTables): EnviarPedidosACocinaUseCase =
        EnviarPedidosACocinaUseCase(repoDBTables)

    /* @Provides
     fun providesCrearFormaDePagoUseCase(repoDBTables: RepoDBTables): CrearFormaDePagoUseCase =
         CrearFormaDePagoUseCase(repoDBTables)
 */

    @Provides
    fun providesAddCommentKitchenToOrderItemUseCase(repoDBTables: RepoDBTables): AddCommentKitchenToOrderItemUseCase =
        AddCommentKitchenToOrderItemUseCase(repoDBTables)


    @Provides
    fun providesCreateOrderToGoUseCase(
        repoDBUsers: RepoDBUsers,
        repoDBOrders: RepoDBOrders
    ): CreateOrderToGoUseCase =
        CreateOrderToGoUseCase(repoDBUsers, repoDBOrders)

    @Provides
    fun providesGetActiveOrdersToGoRealTimeUseCase(
        repoDBUsers: RepoDBUsers,
        repoDBOrders: RepoDBOrders
    ):
            GetActiveOrdersToGoRealTimeUseCase =
        GetActiveOrdersToGoRealTimeUseCase(repoDBUsers, repoDBOrders)

    @Provides
    fun providesGetEnvasesDatabaseByOwnerIDUseCase(repoDBProducts: RepoDBProducts): GetEnvasesDatabaseByOwnerIDUseCase =
        GetEnvasesDatabaseByOwnerIDUseCase(repoDBProducts)

    @Provides
    fun providesGetProductosCartaUseCase(
        repoDBUsers: RepoDBUsers,
        repoDBProducts: RepoDBProducts
    ): GetProductosCartaUseCase =
        GetProductosCartaUseCase(repoDBUsers, repoDBProducts)


    ////// NUEVOSSSS
    @Provides
    fun providesNewPedidoMesaIDUseCase(
        repoDBOrders: RepoDBOrders,
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers
    ): CreateNewPedidoMesaIDUseCase =
        CreateNewPedidoMesaIDUseCase(repoDBOrders, repoDBTables, repoDBUsers)


    @Provides
    fun providesGetCategoriasDeProductoDeSucursalUseCase(
        repoDBRestaurantes: RepoDBRestaurantes,
        repoDBUsers: RepoDBUsers
    ): GetCategoriasDeProductoDeSucursalUseCase =
        GetCategoriasDeProductoDeSucursalUseCase(repoDBRestaurantes, repoDBUsers)

    @Provides
    fun providesGetListaProductosDatabaseUseCase(
        repoDBProducts: RepoDBProducts,
        repoDBUsers: RepoDBUsers
    ): GetListaProductosDatabaseUseCase =
        GetListaProductosDatabaseUseCase(repoDBProducts, repoDBUsers)

    @Provides
    fun providesCrearItemDePedidoUseCase(
        repoDBUsers: RepoDBUsers,
        repoDBOrders: RepoDBOrders
    ): CrearItemDePedidoUseCase =
        CrearItemDePedidoUseCase(repoDBUsers, repoDBOrders)


    @Provides
    fun providesSaveItemsEnPedidoDatabaseUseCase(
        repoDBOrders: RepoDBOrders,
        repoDBUsers: RepoDBUsers,
        repoDBRestaurantes: RepoDBRestaurantes
    ): SaveItemsEnPedidoDatabaseUseCase =
        SaveItemsEnPedidoDatabaseUseCase(repoDBOrders, repoDBUsers, repoDBRestaurantes)


    @Provides
    fun providesGetPedidoDesdeDatabaseUseCase(repoDBOrders: RepoDBOrders): GetPedidoDesdeDatabaseUseCase =
        GetPedidoDesdeDatabaseUseCase(repoDBOrders)


    @Provides
    fun providesNotificarCocinaParaAceptarPedidosUseCase(
        repoDBOrders: RepoDBOrders,
        repoDBRestaurantes: RepoDBRestaurantes,
        repoDBUsers: RepoDBUsers
    ): NotificarCocinaParaAceptarPedidosUseCase =
        NotificarCocinaParaAceptarPedidosUseCase(repoDBOrders, repoDBRestaurantes, repoDBUsers)


    @Provides
    fun providesEliminarPedidoDeDatabaseUseCase ( repoDBOrders: RepoDBOrders): EliminarPedidoDeDatabaseUseCase=
        EliminarPedidoDeDatabaseUseCase(repoDBOrders)


}
