package com.progammer.unimakinventory

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val quantityEditText = findViewById<EditText>(R.id.quantityEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toInt()
            val description = descriptionEditText.text.toString()

            val database = FirebaseDatabase.getInstance().reference.child("inventory")
            val id = database.push().key
            if (id != null) {
                val item = InventoryItem(id, name, quantity, description)
                database.child(id).setValue(item).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                        // Handle error
                    }
                }
            }
        }
    }
}
