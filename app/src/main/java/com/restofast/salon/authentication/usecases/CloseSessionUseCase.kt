package com.restofast.salon.authentication.usecases

import com.restofast.salon.authentication.usecases.SignOutAppUseCase


class CloseSessionUseCase (var signOutAppUseCase: SignOutAppUseCase) {

    suspend operator fun invoke() = signOutAppUseCase.invoke()

}