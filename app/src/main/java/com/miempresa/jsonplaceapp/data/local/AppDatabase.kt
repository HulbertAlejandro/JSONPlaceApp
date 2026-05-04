package com.miempresa.jsonplaceapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miempresa.jsonplaceapp.data.local.dao.*
import com.miempresa.jsonplaceapp.data.local.entity.*

@Database(
    entities = [
        PostEntity::class,
        UserEntity::class,
        CommentEntity::class,
        PhotoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun photoDao(): PhotoDao
}