package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OrderEntity::class], version = 1, exportSchema = false)
abstract class RasoiDatabase : RoomDatabase() {
  abstract fun orderDao(): OrderDao

  companion object {
    @Volatile
    private var INSTANCE: RasoiDatabase? = null

    fun getDatabase(context: Context): RasoiDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          RasoiDatabase::class.java,
          "rasoi_db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
