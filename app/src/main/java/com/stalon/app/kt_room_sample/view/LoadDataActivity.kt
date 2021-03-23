package com.stalon.app.kt_room_sample.view

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.stalon.app.R
import com.stalon.app.kt_room_sample.room_db.DatabaseClient
import com.stalon.app.kt_room_sample.room_db.Task
import com.stalon.app.kt_room_sample.adapter.TasksAdapter
import kotlinx.android.synthetic.main.activity_main.*

class LoadDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview_tasks.setLayoutManager(LinearLayoutManager(this))
        floating_button_add.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoadDataActivity, AddTaskActivity::class.java)
            startActivity(intent)
        })
        tasks
    }

    private val tasks: Unit
        private get() {
            class GetTasks : AsyncTask<Void?, Void?, List<Task>>() {

                override fun onPostExecute(tasks: List<Task>) {
                    super.onPostExecute(tasks)
                    val adapter = TasksAdapter(this@LoadDataActivity, tasks)
                    recyclerview_tasks!!.adapter = adapter
                }

                override fun doInBackground(vararg p0: Void?): List<Task> {
                    return DatabaseClient.getInstance(applicationContext)
                            ?.appDatabase
                            ?.taskDao()
                            ?.all!!
                }
            }

            val gt = GetTasks()
            gt.execute()
        }
}