package com.restofast.salon.sharedData.domain.daos

import androidx.room.*
import com.restofast.salon.sharedData.domain.entities.UserRoom

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY uid")
    suspend fun loadAll(): MutableList<UserRoom?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userRoom: UserRoom?)

    @Update
    suspend fun update(userRoom: UserRoom?)

    @Delete
    suspend fun delete(userRoom: UserRoom?)

    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun loadById(uid: String): UserRoom?

    @Query("DELETE FROM users WHERE uid IN (:uid)")
    suspend fun deleteById(uid: String)

    @Query("DELETE FROM users")
    suspend fun cleanRoom()

}