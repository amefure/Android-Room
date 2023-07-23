package com.example.room.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
     val name: String,
     val age: Int,
     val hobby: String

//    @ColumnInfo(name = "first_name") val firstName: String?,
//    @ColumnInfo(name = "last_name") val lastName: String?
){
    companion object {
        fun getDemoData(): List<User> {
            return listOf(
                User(1, "ame", 25, "ボルタリング"),
                User(2, "mahiro", 30, "お菓子作り"),
                User(3, "sho", 22, "ヒッチハイク"),
                User(4, "Joseph", 27, "カフェ巡り"),
                User(5, "ito", 42, "小説"),
                User(6, "riku", 67, "麻雀"),
                User(7, "yoru", 25, "ダイエット"),
                User(8, "risa", 31, "映画")
            )
        }
    }
}

