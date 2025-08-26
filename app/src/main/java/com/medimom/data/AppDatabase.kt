package com.medimom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medimom.dao.CategoryDao
import com.medimom.dao.ChildDao
import com.medimom.dao.FactDao
import com.medimom.dao.MedicineDao
import com.medimom.dao.SicknessDao
import com.medimom.dao.UnitDao
import com.medimom.entity.CategoryEntity
import com.medimom.entity.ChildEntity
import com.medimom.entity.FactEntity
import com.medimom.entity.MedicineEntity
import com.medimom.entity.SicknessEntity
import com.medimom.entity.UnitEntity

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
