package com.progammer.unimakinventory

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class EditItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val itemId = intent.getStringExtra("ITEM_ID")
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val quantityEditText = findViewById<EditText>(R.id.quantityEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        val database = FirebaseDatabase.getInstance().reference.child("inventory").child(itemId!!)

        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toInt()
            val description = descriptionEditText.text.toString()



            deleteButton.setOnClickListener {
                database.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                        // Handle error
                    }
                }
            }

        }}}
