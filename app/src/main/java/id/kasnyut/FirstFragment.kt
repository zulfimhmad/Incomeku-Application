package id.kasnyut

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.kasnyut.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }
    companion object {
        private const val DATABASE_NAME = "myapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_AMOUNT = "amount"
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DatabaseHelper(requireContext())

        recyclerView = view.findViewById(R.id.rv)
        getAllItems(dbHelper);

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddItemDialog(dbHelper)
        }
    }

    private fun getAllItems(dbHelper: DatabaseHelper) {

        val db = dbHelper.readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_TYPE, COLUMN_AMOUNT)
        val cursor = db.query(TABLE_NAME, projection, null, null, null, null, null)

        val items = mutableListOf<Item>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val type = getInt(getColumnIndexOrThrow(COLUMN_TYPE))
                val amount = getInt(getColumnIndexOrThrow(COLUMN_AMOUNT))
                items.add(Item(id, name, type, amount))
            }
        }

        adapter = ItemAdapter(items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun showAddItemDialog(databaseHelper: DatabaseHelper) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_item, null)

        val editTextName = view.findViewById<EditText>(R.id.edit_text_name)
        val editTextAmount = view.findViewById<EditText>(R.id.edit_text_amount)
        val radioGroupType = view.findViewById<RadioGroup>(R.id.radio_group_type)
        val radioType1 = view.findViewById<RadioButton>(R.id.radio_type_1)
        val radioType2 = view.findViewById<RadioButton>(R.id.radio_type_2)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Tambah Data")
            .setPositiveButton("Tambah") { _, _ ->
                val name = editTextName.text.toString()
                val amount = editTextAmount.text.toString().toInt()
                val type = if (radioType1.isChecked) 1 else 0

                val item = Item(name = name, amount = amount, type = type, id = null)
                databaseHelper.insertItem(item)
               getAllItems(databaseHelper);
                Toast.makeText(context,"Data berhasil ditambahkan",Toast.LENGTH_SHORT)
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}