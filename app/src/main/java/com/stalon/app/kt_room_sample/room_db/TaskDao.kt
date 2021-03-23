package com.stalon.app.kt_room_sample.room_db

import androidx.room.*

@Dao
interface TaskDao {
    @get:Query("SELECT * FROM task")
    val all: List<Task>

    @Insert
    fun insert(task: Task?)

    @Delete
    fun delete(task: Task?)

    @Update
    fun update(task: Task?)
}