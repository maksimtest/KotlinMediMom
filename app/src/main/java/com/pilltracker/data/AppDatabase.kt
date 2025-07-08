package com.pilltracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pilltracker.dao.CategoryDao
import com.pilltracker.dao.ChildDao
import com.pilltracker.dao.FactDao
import com.pilltracker.dao.MedicineDao
import com.pilltracker.dao.SicknessDao
import com.pilltracker.dao.UnitDao
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.entity.UnitEntity

@Database(entities = [
    CategoryEntity::class, MedicineEntity::class, UnitEntity::class,
    ChildEntity::class, FactEntity::class, SicknessEntity::class],
    version = 30,
    exportSchema = true)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun medicineDao(): MedicineDao
    abstract fun unitDao(): UnitDao
    abstract fun childDao(): ChildDao
    abstract fun factDao(): FactDao
    abstract fun sicknessDao(): SicknessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                                            context.applicationContext,
                                            AppDatabase::class.java,
                                            "mediMom_db"
                                        ).fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }

    }
}
