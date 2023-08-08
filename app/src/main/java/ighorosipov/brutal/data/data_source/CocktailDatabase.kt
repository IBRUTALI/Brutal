package ighorosipov.brutal.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ighorosipov.brutal.domain.model.Cocktail

@Database(
    entities = [Cocktail::class],
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

        const val DATABASE_NAME = "cocktails_db"

    }

}