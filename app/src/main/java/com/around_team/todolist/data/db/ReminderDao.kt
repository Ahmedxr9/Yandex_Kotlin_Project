package com.around_team.todolist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.around_team.todolist.data.db.entities.ReminderEntity

/**
 * Data Access Object (DAO) for accessing Reminders in the local database.
 */
@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Query("SELECT * FROM reminders ORDER BY trigger_time ASC")
    suspend fun getAllReminders(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE task_id = :taskId")
    suspend fun getRemindersByTaskId(taskId: String): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE trigger_time >= :fromTime AND trigger_time <= :toTime")
    suspend fun getRemindersByTimeRange(fromTime: Long, toTime: Long): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): ReminderEntity?

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    @Query("DELETE FROM reminders WHERE task_id = :taskId")
    suspend fun deleteRemindersByTaskId(taskId: String)
}

