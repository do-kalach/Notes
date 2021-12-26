package com.agening.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.agening.notes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var adapter: NoteAdapter? = null
    private var dbHelper: NotesDBHelper? = null
    private val notes: ArrayList<Note> = ArrayList()
    private var database:SQLiteDatabase?=null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(this@MainActivity, "Hello", Toast.LENGTH_SHORT).show()
                adapter?.notifyDataSetChanged()
            }

        })
        dbHelper = NotesDBHelper(this)
        database = dbHelper?.writableDatabase

        getData()

        adapter = NoteAdapter(notes)
        binding.recyclerViewNotes.adapter = adapter

        binding.buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewNotes)
        adapter?.setOnNoteClickListener = object : NoteAdapter.OnNoteClickListener {
            override fun onNoteClick(position: Int) {
                Toast.makeText(applicationContext, "Hello", Toast.LENGTH_SHORT).show()
            }

            override fun onLongClick(position: Int) {
                remove(position)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun remove(position: Int) {
        val id = notes[position].id
        val where: String = NotesContract.NotesEntry.ID + " = ?"
        val whereArgs = arrayOf(id.toString())
        database?.delete(NotesContract.NotesEntry.TABLE_NAME, where, whereArgs)
        getData()
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("Range")
    private fun getData() {
        notes.clear()
        val selection = NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK + " == ?"
        val selectionArgs = arrayOf("1")
        val cursor = database!!.query(
            NotesContract.NotesEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            NotesContract.NotesEntry.COLUMN_PRIORITY
        )
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.ID))

            val title =
                cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE))
            val description =
                cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DESCRIPTION))
            val dayOfWeek =
                cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK))
            val priority =
                cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_PRIORITY))
            val note = Note(id.toInt(), title, description, dayOfWeek, priority)
            notes.add(note)
        }
        cursor.close()
    }
}