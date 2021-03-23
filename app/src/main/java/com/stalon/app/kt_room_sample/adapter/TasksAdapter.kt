package com.stalon.app.kt_room_sample.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stalon.app.R
import com.stalon.app.kt_room_sample.adapter.TasksAdapter.TasksViewHolder
import com.stalon.app.kt_room_sample.room_db.Task
import com.stalon.app.kt_room_sample.view.UpdateTaskActivity

class TasksAdapter(private val mCtx: Context, private val taskList: List<Task>) : RecyclerView.Adapter<TasksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_tasks, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val t = taskList[position]
        holder.textViewTask.text = t.task
        holder.textViewDesc.text = t.desc
        holder.textViewFinishBy.text = t.finishBy
        if (t.isFinished) holder.textViewStatus.text = "Completed" else holder.textViewStatus.text = "Not Completed"
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var textViewStatus: TextView
        var textViewTask: TextView
        var textViewDesc: TextView
        var textViewFinishBy: TextView
        override fun onClick(view: View) {
            val task = taskList[adapterPosition]
            val intent = Intent(mCtx, UpdateTaskActivity::class.java)
            intent.putExtra("task", task)
            mCtx.startActivity(intent)
        }

        init {
            textViewStatus = itemView.findViewById(R.id.textViewStatus)
            textViewTask = itemView.findViewById(R.id.textViewTask)
            textViewDesc = itemView.findViewById(R.id.textViewDesc)
            textViewFinishBy = itemView.findViewById(R.id.textViewFinishBy)
            itemView.setOnClickListener(this)
        }
    }

}