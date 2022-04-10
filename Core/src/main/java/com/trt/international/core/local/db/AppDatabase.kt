package com.trt.international.core.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trt.international.core.local.db.dao.UserFavoriteDao
import com.trt.international.core.local.db.entity.UserFavoriteEntity

@Database(
    entities = [UserFavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userFavDao(): UserFavoriteDao

}