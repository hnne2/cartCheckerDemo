package com.push700.io

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [cartsInRoom::class], version = 1)
abstract class PackageAppDataBase : RoomDatabase() {

    abstract fun cartsDao(): CartsDao

    companion object {
        @Volatile
        private var INSTANCE: PackageAppDataBase? = null

        fun getDatabase(context: Context): PackageAppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PackageAppDataBase::class.java,
                    "user_database"
                ).build().also { INSTANCE = it }
                instance
            }
        }
    }
}


@Dao
interface CartsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(cartsInRoom: cartsInRoom)
    @Query("SELECT * FROM cartsInRoom")
    suspend  fun getAllPushes(): List<cartsInRoom>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromList(pushItemInRoomList: List<cartsInRoom>)
    @Query("DELETE FROM cartsInRoom")
    suspend fun deleteAllPushes()
}
@Entity(tableName = "cartsInRoom")
class cartsInRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "typeCart")
    val typeCart: String?,

    @ColumnInfo(name = "bank")
    val bank: String?,

    @ColumnInfo(name = "SchemeCart")
    val SchemeCart: String?,

    @ColumnInfo(name = "city")
    val city: String?,

    @ColumnInfo(name = "country")
    val country: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name ="url" )
    val url: String?,
    @ColumnInfo(name ="number" )
    val number: String?,

)