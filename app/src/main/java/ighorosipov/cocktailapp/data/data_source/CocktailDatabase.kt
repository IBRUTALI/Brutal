package ighorosipov.cocktailapp.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ighorosipov.cocktailapp.data.model.CocktailEntity

@Database(
    entities = [CocktailEntity::class],
    version = 1
)
abstract class CocktailDatabase: RoomDatabase() {

    abstract val cocktailDao: CocktailDao

    companion object {

        fun getDB(context: Context): CocktailDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CocktailDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

        private const val DATABASE_NAME = "cocktails_db"

    }

}