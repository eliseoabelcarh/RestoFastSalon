package com.restofast.salon.sharedData.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.restofast.salon.authentication.usecases.AddUserToRoomUseCase
import com.restofast.salon.authentication.usecases.CleanRoomUseCase
import com.restofast.salon.sharedData.data.*
import com.restofast.salon.sharedData.domain.daos.UserDao
import com.restofast.salon.sharedData.domain.room.DBRoomUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {

    @Singleton
    @Provides
    fun provideRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        DBRoomUser::class.java,
        "myDBRoomUser"
    ).allowMainThreadQueries().build()


    @Singleton
    @Provides
    fun provideUserDao(dbRoomUser: DBRoomUser): UserDao = dbRoomUser.userDao()


    @Provides
    fun provideRepoRoomUser(userDao: UserDao): RepoRoomUser = RepoRoomUser(userDao)

    @Provides
    fun provideCleanRoomUseCase(repoRoomUser: RepoRoomUser): CleanRoomUseCase =
        CleanRoomUseCase(repoRoomUser)

    @Provides
    fun provideAddUserToRoomUseCase(repoRoomUser: RepoRoomUser): AddUserToRoomUseCase =
        AddUserToRoomUseCase(repoRoomUser)

    /*@Singleton
    @Provides
    fun provideConfigApp(@ApplicationContext context: Context): ConfigApp = ConfigApp(context)*/


    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    fun provideRepoDBUsers(auth: FirebaseAuth, db: FirebaseFirestore): RepoDBUsers =
        RepoDBUsers(auth, db)


    @Provides
    fun provideRepoDBOrders(db: FirebaseFirestore): RepoDBOrders =
        RepoDBOrders(db)

    @Provides
    fun provideRepoDBRestaurantes(db: FirebaseFirestore): RepoDBRestaurantes =
        RepoDBRestaurantes(db)

    @Provides
    fun provideRepoDBMovimientos(
        db: FirebaseFirestore,
        repoDBRestaurantes: RepoDBRestaurantes
    ): RepoDBMovimientos =
        RepoDBMovimientos(db, repoDBRestaurantes)


    @Provides
    fun provideRepoDBTables(
        db: FirebaseFirestore,
        repoDBUsers: RepoDBUsers,
        repoDBOrders: RepoDBOrders,
        repoDBMovimientos: RepoDBMovimientos,
        repoDBRestaurantes: RepoDBRestaurantes,
        repoDBProducts: RepoDBProducts
    ): RepoDBTables =
        RepoDBTables(db, repoDBUsers, repoDBOrders, repoDBMovimientos, repoDBRestaurantes, repoDBProducts)

    @Provides
    fun provideRepoDBProducts(db: FirebaseFirestore): RepoDBProducts = RepoDBProducts(db)

    @Provides
    fun provideRepoDBOrdersToGo(db: FirebaseFirestore): RepoDBOrdersToGo =
        RepoDBOrdersToGo(db)

}