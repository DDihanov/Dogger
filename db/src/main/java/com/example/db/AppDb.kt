package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dihanov.catsearch.data.local.dao.CatDao
import com.dihanov.catsearch.data.local.entity.Cat
import com.dihanov.dogsearch.data.local.dao.DogDao
import com.dihanov.dogsearch.data.local.entity.Dog

@Database(entities = arrayOf(Dog::class, Cat::class), version = 7, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun dogDao(): DogDao
    abstract fun catDao(): CatDao
}