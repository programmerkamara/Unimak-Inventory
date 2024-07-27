package com.progammer.unimakinventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var itemsList: MutableList<InventoryItem>
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var itemsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemsList = mutableListOf()
        itemsAdapter = ItemsAdapter(itemsList, this::onEditItem, this::onDeleteItem)
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = itemsAdapter
        }

        findViewById<Button>(R.id.addItemButton).setOnClickListener {
            showAddItemDialog()
        }

        itemsRef = FirebaseDatabase.getInstance().getReference("inventory")
        itemsRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemsList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(InventoryItem::class.java)
                    item?.let {
                        val itemWithId = it.copy(id = itemSnapshot.key ?: "")
                        itemsList.add(itemWithId)
                    }
                }
                itemsAdapter.notifyDataSetChanged()
                updateEmptyState()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load items: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddItemDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_item, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
        val quantityEditText = dialogView.findViewById<EditText>(R.id.quantityEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        AlertDialog.Builder(this)
            .setTitle("Add Item")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newItem = InventoryItem(
                    id = itemsRef.push().key ?: "",
                    name = nameEditText.text.toString(),
                    quantity = quantityEditText.text.toString().toInt(),
                    description = descriptionEditText.text.toString()
                )
                addItemToDatabase(newItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addItemToDatabase(item: InventoryItem) {
        itemsRef.child(item.id).setValue(item)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onEditItem(item: InventoryItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_item, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
        val quantityEditText = dialogView.findViewById<EditText>(R.id.quantityEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        nameEditText.setText(item.name)
        quantityEditText.setText(item.quantity.toString())
        descriptionEditText.setText(item.description)

        AlertDialog.Builder(this)
            .setTitle("Update Item")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedItem = item.copy(
                    name = nameEditText.text.toString(),
                    quantity = quantityEditText.text.toString().toInt(),
                    description = descriptionEditText.text.toString()
                )
                updateItemInDatabase(updatedItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onDeleteItem(item: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { _, _ ->
                deleteItemFromDatabase(item)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateItemInDatabase(item: InventoryItem) {
        itemsRef.child(item.id).setValue(item)
            .addOnSuccessListener {
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteItemFromDatabase(item: InventoryItem) {
        itemsRef.child(item.id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateEmptyState() {

    }
}
