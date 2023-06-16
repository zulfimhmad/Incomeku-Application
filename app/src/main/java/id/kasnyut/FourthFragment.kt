package id.kasnyut

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.kasnyut.databinding.FragmentFourthBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FourthFragment : Fragment() {
    private var _binding: FragmentFourthBinding? = null
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView : RecyclerView
    private lateinit var noInternetTextView : TextView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        noInternetTextView = view.findViewById<TextView>(R.id.noInternetTextView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        newsAdapter = NewsAdapter(view.context, emptyList()) { link ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
        recyclerView.adapter = newsAdapter
        progressDialog = ProgressDialog(binding.root.context)
        progressDialog.setMessage("Memuat Daftar Berita..")
        progressDialog.setTitle("Incomeku")
        progressDialog.setCancelable(false)

        val koneksi_gak_pake_php = checkInternetConnection(view)
        if (koneksi_gak_pake_php) {
            showLoadingDialog()
            // Do your operations here
            GlobalScope.launch(Dispatchers.IO) {
                val json = fetchDataFromApi()
                val newsList = parseJson(json)


                // Update the RecyclerView on the main thread
                launch(Dispatchers.Main) {
                    newsAdapter = NewsAdapter(view.context, newsList) { link ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                    recyclerView.adapter = newsAdapter
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                    hideLoadingDialog()
                }
            }
        } else {
            showNoInternetDialog(view)
        }
    // Make API call on a background thread



    }

    private fun parseJson(json: String): List<NewsItem> {
        val newsList = mutableListOf<NewsItem>()

        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val image = jsonObject.getString("image")
                val title = jsonObject.getString("title")
                val link = jsonObject.getString("link")

                val newsItem = NewsItem(image, title, link)
                newsList.add(newsItem)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newsList
    }

    private fun showLoadingDialog() {
        Handler(Looper.getMainLooper()).post {
            progressDialog.show()
        }

    }

    private fun hideLoadingDialog() {
        Handler(Looper.getMainLooper()).post {
            progressDialog.dismiss()
        }

    }

    private fun fetchDataFromApi(): String {
        val apiUrl = "https://cileungsi.uniantara.id/zulfi/data.json" // Replace with your API URL
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        val inputStream = connection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        return response.toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkInternetConnection(view:View): Boolean {
        if (context == null) return false
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    private fun showNoInternetDialog(view: View) {
      noInternetTextView.visibility = View.VISIBLE;
        recyclerView.visibility = View.GONE;
    }
}