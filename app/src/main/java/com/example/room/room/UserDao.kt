package com.example.room.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Insert
     fun insert(user: User)

    @Query("SELECT * FROM user_table")
    fun getAll(): Flowable<List<User>>

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getLiveDataUser(id: Int): LiveData<User>

    @Query("DELETE FROM user_table")
    fun deleteAll()

}