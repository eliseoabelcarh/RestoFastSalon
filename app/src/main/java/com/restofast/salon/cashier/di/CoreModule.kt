package com.restofast.salon.cashier.di

import com.restofast.salon.cashier.usecases.*
import com.restofast.salon.sharedData.data.RepoDBMovimientos
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {


    @Provides
    fun provideLoadOrderUseCase(repoDBTables: RepoDBTables): LoadOrderUseCase =
        LoadOrderUseCase(repoDBTables)

    @Provides
    fun provideCreateNewFormaDePagoUseCase(
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers,
        repoDBRestaurantes: RepoDBRestaurantes
    ): CreateNewFormaDePagoUseCase =
        CreateNewFormaDePagoUseCase(repoDBTables, repoDBUsers, repoDBRestaurantes)

    @Provides
    fun provideSetPagoAOrderUseCase(
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers
    ): SetPagoAOrderUseCase =
        SetPagoAOrderUseCase(repoDBTables, repoDBUsers)

    @Provides
    fun provideCloseOrderInDBUseCase(
        repoDBTables: RepoDBTables,
        repoDBUsers: RepoDBUsers,
        repoDBRestaurantes: RepoDBRestaurantes,
        repoDBMovimientos: RepoDBMovimientos
    ): CloseOrderInDBUseCase =
        CloseOrderInDBUseCase(repoDBTables, repoDBUsers, repoDBRestaurantes, repoDBMovimientos)


    @Provides
    fun provideGetUltimoNroBoletaSugeridoUseCase(
        repoDBRestaurantes: RepoDBRestaurantes,
        repoDBUsers: RepoDBUsers
    ): GetUltimoNroBoletaSugeridoUseCase =
        GetUltimoNroBoletaSugeridoUseCase(repoDBRestaurantes, repoDBUsers)

}