package id.kasnyut

import android.app.AlertDialog
import android.app.DownloadManager.COLUMN_ID
import android.media.tv.TvContract.Channels.COLUMN_TYPE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import id.kasnyut.DatabaseHelper;

class ItemAdapter(private var items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameTextView.text = currentItem.name
        var prefix = "+";
        if (currentItem.type == 0) {
            prefix = "-";
        }
        holder.amountTextView.text = prefix +  currentItem.amount.toString()
        val colorRes = when (currentItem.type) {
            0 -> R.color.dark_red
            else -> R.color.dark_green
        }
        if (Build.VERSION.SDK_INT > 23) {
            holder.amountTextView.setTextColor(
                holder.amountTextView.getContext().getColor(colorRes)
            )
        }
        // Add a long click listener to the ViewHolder
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            // Show a confirmation dialog
            AlertDialog.Builder(context)
                .setTitle("Hapus Data")
                .setMessage("Apakah kamu yakin akan menghapus " + currentItem.name + "?")
                .setPositiveButton("Hapus") { _, _ ->
                    // Delete the item from the database
                    val db = DatabaseHelper(context).writableDatabase
                    db.delete("items", "_id=?", arrayOf(currentItem.id.toString()))
                    db.close()

                    // Remove the item from the list and notify the adapter
                    val newList = items.toMutableList()
                    newList.removeAt(position)
                    items = newList.toList()
                    notifyItemRemoved(position)
                    Toast.makeText(context,"Data berhasil dihapus",Toast.LENGTH_SHORT)
                }
                .setNegativeButton("Batal", null)
                .show()

            true
        }
    }


    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }
    override fun getItemCount() = items.size

}
