package com.restofast.salon.authentication.di


import com.restofast.salon.authentication.usecases.*
import com.restofast.salon.authentication.usecases.CloseSessionUseCase
import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.sharedData.data.RepoRoomUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {


    @Provides
    fun provideCreateUserRoomModelUseCase() : CreateUserRoomModelUseCase =
        CreateUserRoomModelUseCase()


    @Provides
    fun provideCreateModeloEmpleadoUseCase() : CreateModeloEmpleadoUseCase =
        CreateModeloEmpleadoUseCase()

    @Provides
    fun provideCreateUserWithEmailAndPasswordUseCase(repoDBUsers: RepoDBUsers) : CreateUserWithEmailAndPasswordUseCase =
        CreateUserWithEmailAndPasswordUseCase(repoDBUsers)

    @Provides
    fun provideUserIsAuthenticatedUseCase(repoDBUsers: RepoDBUsers) : UserIsAuthenticatedUseCase =
        UserIsAuthenticatedUseCase(repoDBUsers)

    @Provides
    fun provideSignOutAppUseCase(repoDBUsers: RepoDBUsers, repoRoomUser: RepoRoomUser) : SignOutAppUseCase =
        SignOutAppUseCase(repoDBUsers,repoRoomUser)

    @Provides
    fun provideCloseSessionUseCase(signOutAppUseCase: SignOutAppUseCase) : CloseSessionUseCase =
        CloseSessionUseCase(signOutAppUseCase)


    @Provides
    fun provideLoginUserUseCase(repoDBUsers: RepoDBUsers, repoRoomUser: RepoRoomUser): LoginUserUseCase =
        LoginUserUseCase(repoDBUsers,repoRoomUser)


    @Provides
    fun provideCreateNewUserInDatabaseUseCase(repoDBUsers: RepoDBUsers) : CreateNewUserInDatabaseUseCase =
        CreateNewUserInDatabaseUseCase(repoDBUsers)


    @Provides
    fun provideResetPasswordUseCase(repoDBUsers: RepoDBUsers) : ResetPasswordUseCase =
        ResetPasswordUseCase(repoDBUsers)


    @Provides
    fun provideGetUserFromRoomUseCase(repoRoomUser: RepoRoomUser) : GetUserFromRoomUseCase =
        GetUserFromRoomUseCase(repoRoomUser)





    @Provides
    fun providesGetEmpleadoUseCase(repoDBUsers: RepoDBUsers) : GetEmpleadoUseCase =
        GetEmpleadoUseCase(repoDBUsers)


    @Provides
    fun providesGetUserIDUseCase(repoDBUsers: RepoDBUsers) : GetUserIDUseCase =
        GetUserIDUseCase(repoDBUsers)


    @Provides
    fun providesGetIDSegunRestaurantYSucursalDeEmpleadoUseCase(repoDBUsers: RepoDBUsers) : GetIDSegunRestaurantYSucursalDeEmpleadoUseCase =
        GetIDSegunRestaurantYSucursalDeEmpleadoUseCase(repoDBUsers)

    @Provides
    fun providesGetUserModelByIDUseCase(repoDBUsers: RepoDBUsers): GetUserModelByIDUseCase =
        GetUserModelByIDUseCase(repoDBUsers)

}