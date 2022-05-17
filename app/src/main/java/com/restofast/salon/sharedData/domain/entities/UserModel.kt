package com.restofast.salon.sharedData.domain.entities

import android.util.Log

open class UserModel constructor(

    open var uid : String = "",
    open var name : String = "",
    open var lastname : String = "",
    open var dni : String = "",
    open var email : String = "",
    open var password : String = ""

) {

    fun castToUserRoom(): UserRoom {
        return UserRoom(uid,name,lastname,dni,email,password)
    }

}