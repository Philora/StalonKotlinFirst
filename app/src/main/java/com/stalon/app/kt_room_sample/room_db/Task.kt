package com.stalon.app.kt_room_sample.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Task : Serializable {
    /*
     * Getters and Setters
     * */
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "task")
    var task: String? = null

    @ColumnInfo(name = "desc")
    var desc: String? = null

    @ColumnInfo(name = "finish_by")
    var finishBy: String? = null

    @ColumnInfo(name = "finished")
    var isFinished = false

}