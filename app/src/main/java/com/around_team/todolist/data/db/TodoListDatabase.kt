package com.around_team.todolist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.around_team.todolist.data.db.converters.Converters
import com.around_team.todolist.data.db.entities.ReminderEntity
import com.around_team.todolist.data.db.entities.TodoItemEntity

/**
 * Database class for the Todo List application.
 * This class defines the database configuration and serves as the main access point
 * for the underlying connection to your app's persisted relational data.
 *
 * @property entities The list of entities associated with the database.
 * @property version The version number of the database schema.
 * @property exportSchema Indicates whether to export the schema to a file.
 */
@Database(
    entities = [TodoItemEntity::class, ReminderEntity::class],
    version = 3,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class TodoListDatabase : RoomDatabase() {

    /**
     * Provides the Data Access Object (DAO) for interacting with the database.
     *
     * @return The DAO for the todo list database.
     */
    abstract fun dao(): Dao

    /**
     * Provides the Reminder DAO for interacting with reminders.
     *
     * @return The ReminderDao for the reminders table.
     */
    abstract fun reminderDao(): ReminderDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE todo_list ADD COLUMN color TEXT")
                db.execSQL("ALTER TABLE todo_list ADD COLUMN files TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS reminders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        task_id TEXT,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        trigger_time INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}