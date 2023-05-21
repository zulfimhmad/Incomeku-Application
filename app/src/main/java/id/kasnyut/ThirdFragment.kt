package id.kasnyut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.kasnyut.databinding.FragmentThirdBinding
import java.lang.reflect.Array.getInt

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var total = binding.textView2;
        var totalIncome = 0;

        val dbHelper = DatabaseHelper(requireContext())
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            ThirdFragment.COLUMN_ID,
            ThirdFragment.COLUMN_NAME,
            ThirdFragment.COLUMN_TYPE,
            ThirdFragment.COLUMN_AMOUNT
        )
        val cursor = db.query(ThirdFragment.TABLE_NAME, projection, null, null, null, null, null)

        val items = mutableListOf<Item>()
        with(cursor) {
            while (moveToNext()) {
                val type = getInt(getColumnIndexOrThrow(ThirdFragment.COLUMN_TYPE))
                val amount = getInt(getColumnIndexOrThrow(ThirdFragment.COLUMN_AMOUNT))
              if (type == 1) {
                  totalIncome += amount
              } else {
                  totalIncome -= amount
              }
            }
        }

        total.setText("Rp " + totalIncome);
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}