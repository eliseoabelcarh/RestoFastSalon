package com.restofast.salon.sharedData.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.restofast.salon.sharedData.domain.daos.UserDao
import com.restofast.salon.sharedData.domain.entities.UserRoom


@Database(entities = [UserRoom::class], version = 1, exportSchema = false)
abstract class DBRoomUser : RoomDatabase(){

    abstract  fun userDao(): UserDao

    companion object {

        var INSTANCE: DBRoomUser? = null

        fun getRoom(context: Context?): DBRoomUser? {
            if (INSTANCE == null) {
                synchronized(DBRoomUser::class) {

                    if (context != null) {

                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DBRoomUser::class.java,
                            "myDBRoomUser"
                        ).allowMainThreadQueries().build()
                    }
                }
            }

            return INSTANCE
        }

        fun destroyRoom(){
            INSTANCE = null
        }
    }

}