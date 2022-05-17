package com.restofast.salon.entities.formaters

import android.util.Log
import java.security.MessageDigest

object StringFormater {

    fun hashPassword(password: String): String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
        return digest
    }

}