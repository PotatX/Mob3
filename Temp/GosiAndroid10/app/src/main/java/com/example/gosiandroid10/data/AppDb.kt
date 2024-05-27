package com.example.gosiandroid10.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.Update
import androidx.room.RoomDatabase
import com.example.gosiandroid10.model.Car

@Database(entities = [Car::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun CarDao(): CarDao

    companion object {
        private var instance: AppDb? = null
        fun reset(context: Context) {
            (context.getDatabasePath("database")).delete()
            instance = null
        }

        fun getDatabase(context: Context): AppDb {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(context, AppDb::class.java, "database")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDb
        }
    }
}

@Dao
interface CarDao {
    @Query("SELECT * FROM Car")
    fun getAll(): List<Car>

    @Query("SELECT * FROM Car WHERE id == (:id)")
    fun get(id: Int): Car

    @Insert
    fun insert(item: Car)

    @Insert
    fun insertAll(items: List<Car>)

    @Update
    fun update(item: Car)

    @Query("DELETE FROM Car WHERE id == (:id)")
    fun delete(id: Int)

    @Query("DELETE FROM Car")
    fun deleteAll()
}