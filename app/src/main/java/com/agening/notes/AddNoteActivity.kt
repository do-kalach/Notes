package com.agening.notes

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agening.notes.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private var dbHelper: NotesDBHelper? = null
    private var database: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dbHelper = NotesDBHelper(this)
        database = dbHelper?.writableDatabase
        binding.buttonSaveNote.setOnClickListener {
            val title: String = binding.editTextTitle.text.toString().trim()
            val description: String = binding.editTextDescription.text.toString().trim()
            val dayOfWeek: String = binding.spinnerDaysOfWeek.selectedItem.toString()
            val radioButtonId = binding.radioGroupPriority.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(radioButtonId)
            val priority = radioButton.text.toString().toInt()
            if (isFilled(title, description)) {
                val contentValues = ContentValues()
                contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, title)
                contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, description)
                contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, dayOfWeek)
                contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, priority)
                database?.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues)
            }else{
                Toast.makeText(this, R.string.warning_fill_fileds, Toast.LENGTH_SHORT).show();
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isFilled(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}