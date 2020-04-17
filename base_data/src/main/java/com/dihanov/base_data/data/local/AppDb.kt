package com.dihanov.base_data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dihanov.base_data.data.local.dao.CatDao
import com.dihanov.base_data.data.local.dao.DogDao
import com.dihanov.base_data.data.local.entity.Cat
import com.dihanov.base_data.data.local.entity.Dog

@Database(entities = arrayOf(Dog::class, Cat::class), version = 7, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun dogDao(): DogDao
    abstract fun catDao(): CatDao
}