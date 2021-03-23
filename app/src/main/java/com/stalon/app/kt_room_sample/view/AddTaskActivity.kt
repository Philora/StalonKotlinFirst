package com.stalon.app.kt_room_sample.view

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stalon.app.R
import com.stalon.app.kt_room_sample.room_db.DatabaseClient
import com.stalon.app.kt_room_sample.room_db.Task

class AddTaskActivity : AppCompatActivity() {
    private var editTextTask: EditText? = null
    private var editTextDesc: EditText? = null
    private var editTextFinishBy: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        editTextTask = findViewById(R.id.edt_Task)
        editTextDesc = findViewById(R.id.edt_Desc)
        editTextFinishBy = findViewById(R.id.edt_FinishBy)
        findViewById<View>(R.id.btn_Save).setOnClickListener { saveTask() }
    }

    private fun saveTask() {
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
        class SaveTask : AsyncTask<Void?, Void?, Void?>() {

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                finish()
                startActivity(Intent(applicationContext, LoadDataActivity::class.java))
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
            }

            override fun doInBackground(vararg p0: Void?): Void? {

                //creating a task
                val task = Task()
                task.task = sTask
                task.desc = sDesc
                task.finishBy = sFinishBy
                task.isFinished = false

                //adding to database
                DatabaseClient.getInstance(applicationContext)?.appDatabase
                        ?.taskDao()
                        ?.insert(task)
                return null
            }
        }

        val st = SaveTask()
        st.execute()
    }
}