package com.progammer.unimakinventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    private val itemsList: List<InventoryItem>,
    private val onEditClick: (InventoryItem) -> Unit,
    private val onDeleteClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)
        private val itemDescriptionTextView: TextView = itemView.findViewById(R.id.itemDescriptionTextView)
        private val editButton: Button = itemView.findViewById(R.id.editButton) // Add this in your layout
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton) // Add this in your layout

        fun bind(item: InventoryItem) {
            itemNameTextView.text = "ItemName: ${item.name}"
            itemQuantityTextView.text = "Quantity: ${item.quantity}"
            itemDescriptionTextView.text = "Description: ${item.description}"

            editButton.setOnClickListener {
                onEditClick(item)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}
