package com.dihanov.dogger.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dihanov.dogger.data.local.db.dao.DogDao
import com.dihanov.dogger.data.local.db.entity.Dog

@Database(entities = arrayOf(Dog::class), version = 6, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun dogDao(): DogDao
}