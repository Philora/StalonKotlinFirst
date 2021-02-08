package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initializing the array lists and the adapter
        var itemlist = arrayListOf<String>()
        var add = Button(this)
        var editText = EditText(this)
        var clear = Button(this)
        var delete = Button(this)
        var adapter =ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice
                , itemlist)
        // Adding the items to the list when the add button is pressed
        add.setOnClickListener {

            itemlist.add(editText.text.toString())
            listView.adapter =  adapter
            adapter.notifyDataSetChanged()
            // This is because every time when you add the item the input space or the eidt text space will be cleared
            editText.text.clear()
        }
        // Clearing all the items in the list when the clear button is pressed
        clear.setOnClickListener {

            itemlist.clear()
            adapter.notifyDataSetChanged()
        }
        // Adding the toast message to the list when an item on the list is pressed
        listView.setOnItemClickListener { adapterView, view, i, l ->
            android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()
        }
        // Selecting and Deleting the items from the list when the delete button is pressed
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }
    }
}