package com.stalon.app.kt_room_sample.view

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stalon.app.R
import com.stalon.app.kt_room_sample.room_db.DatabaseClient
import com.stalon.app.kt_room_sample.room_db.Task
import kotlinx.android.synthetic.main.activity_update_task.*

class UpdateTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)
        val task = intent.getSerializableExtra("task") as Task?
        loadTask(task)
        findViewById<View>(R.id.button_update).setOnClickListener {
            Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_LONG).show()
            updateTask(task)
        }
        findViewById<View>(R.id.button_delete).setOnClickListener {
            val builder = AlertDialog.Builder(this@UpdateTaskActivity)
            builder.setTitle("Are you sure?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialogInterface, i -> deleteTask(task) }
            builder.setNegativeButton("No") { dialogInterface, i -> }
            val ad = builder.create()
            ad.show()
        }
    }

    private fun loadTask(task: Task?) {
        editTextTask.setText(task?.task)
        editTextDesc.setText(task?.desc)
        editTextFinishBy.setText(task?.finishBy)
        checkBoxFinished!!.isChecked = task!!.isFinished
    }

    private fun updateTask(task: Task?) {
        val sTask = editTextTask!!.text.toString().trim { it <= ' ' }
        val sDesc = editTextDesc!!.text.toString().trim { it <= ' ' }
        val sFinishBy = editTextFinishBy!!.text.toString().trim { it <= ' ' }
        if (sTask.isEmpty()) {
            editTextTask!!.error = "Task required"
            editTextTask!!.requestFocus()
            return
        }
        if (sDesc.isEmpty()) {
            editTextDesc!!.error = "Desc required"
            editTextDesc!!.requestFocus()
            return
        }
        if (sFinishBy.isEmpty()) {
            editTextFinishBy!!.error = "Finish by required"
            editTextFinishBy!!.requestFocus()
            return
        }
        class UpdateTask : AsyncTask<Void?, Void?, Void?>() {

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                Toast.makeText(applicationContext, "Updated", Toast.LENGTH_LONG).show()
                finish()
                startActivity(Intent(this@UpdateTaskActivity, LoadDataActivity::class.java))
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                task?.task.equals(sTask)
                task?.desc.equals(sDesc)
                task?.finishBy.equals(sFinishBy)
                task?.isFinished?.compareTo(checkBoxFinished.isChecked());
                DatabaseClient.getInstance(applicationContext)?.appDatabase
                        ?.taskDao()
                        ?.update(task)
                return null
            }
        }

        val ut = UpdateTask()
        ut.execute()
    }

    private fun deleteTask(task: Task?) {
        class DeleteTask : AsyncTask<Void?, Void?, Void?>() {

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
                finish()
                startActivity(Intent(this@UpdateTaskActivity, LoadDataActivity::class.java))
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                DatabaseClient.getInstance(applicationContext)?.appDatabase
                        ?.taskDao()
                        ?.delete(task)
                return null
            }
        }

        val dt = DeleteTask()
        dt.execute()
    }
}