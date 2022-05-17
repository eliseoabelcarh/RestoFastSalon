package com.restofast.salon.authentication.usecases


import com.restofast.salon.entities.formaters.StringFormater
import com.restofast.salon.sharedData.data.RepoDBUsers

class CreateUserWithEmailAndPasswordUseCase (var repoDBUsers: RepoDBUsers){


    suspend operator fun invoke(email: String, password: String) : String {
        return repoDBUsers.createUserWithEmailAndPassword(email, StringFormater.hashPassword(password))
    }


}