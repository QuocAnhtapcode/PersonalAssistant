package club.mobile.d21.personalassistant_ver2_kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DailyTask::class,PriorityTask::class,Note::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun priorityTaskDao(): PriorityTaskDao
    abstract fun noteDao(): NoteDao
    companion object{
        private val converters = Converters()
        @Synchronized
        fun getDailyTaskDatabase(context:Context): AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "dailyTaskDatabase.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addTypeConverter(converters)
                .build()
        }
        @Synchronized
        fun getPriorityTaskDatabase(context:Context): AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "priorityTaskDatabase.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addTypeConverter(converters)
                .build()
        }
        @Synchronized
        fun getNoteDatabase(context:Context): AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "noteDatabase.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addTypeConverter(converters)
                .build()
        }
    }
}