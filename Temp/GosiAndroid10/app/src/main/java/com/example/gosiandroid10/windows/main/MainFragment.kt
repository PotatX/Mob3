package com.example.gosiandroid10.windows.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.findNavController
import com.example.gosiandroid10.R
import com.example.gosiandroid10.data.AppDb
import com.example.gosiandroid10.model.Car
import com.example.gosiandroid10.windows.reports.ReportActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainFragment : Fragment() {
    private lateinit var arrayAdapter: ArrayAdapter<Car>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getDatabase(requireContext())
        val dataList = db.CarDao().getAll()

        arrayAdapter = ArrayAdapter<Car>(requireContext(), android.R.layout.simple_list_item_1)

        view.apply {
            findViewById<Button>(R.id.buttonAdd).setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainFragment_to_editFragment,
                    Bundle().apply {
                        putInt("itemId", -1)
                    }
                )
            }

            findViewById<Button>(R.id.buttonReport)?.setOnClickListener {
                val i = Intent(activity, ReportActivity::class.java)
                startActivity(i)
            }

            findViewById<Button>(R.id.buttonSave).setOnClickListener {
                writeJson(dataList)
            }

            findViewById<Button>(R.id.buttonLoad).setOnClickListener {
                readJson(db)
                arrayAdapter.clear()
                arrayAdapter.addAll(db.CarDao().getAll())
            }

            findViewById<ListView>(R.id.dataList)?.apply {
                adapter = arrayAdapter

                setOnItemClickListener { adapterView, view, position, id ->
                    val item = dataList[position]

                    findNavController().navigate(
                        R.id.action_mainFragment_to_editFragment,
                        Bundle().apply {
                            putInt("carId", item.id!!)
                        })
                }
            }
            arrayAdapter.addAll(db.CarDao().getAll())
        }
    }

    fun writeJson(items: List<Car>) {
        val itemsStr = Json.encodeToString(value = items)

        requireContext().openFileOutput("data.json", Context.MODE_PRIVATE)
            .use {
                it.write(itemsStr.toByteArray())
            }
    }

    private fun readJson(db: AppDb) {
        val strJson = requireContext().openFileInput("data.json")
            .bufferedReader()
            .use {
                it.readText()
            }

        val items = Json.decodeFromString<List<Car>>(strJson)

        db.CarDao().deleteAll()
        db.CarDao().insertAll(items)
    }
}