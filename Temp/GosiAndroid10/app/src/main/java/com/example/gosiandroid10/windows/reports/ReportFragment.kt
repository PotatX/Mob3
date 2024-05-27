package com.example.gosiandroid10.windows.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.gosiandroid10.R
import com.example.gosiandroid10.data.AppDb
import com.example.gosiandroid10.model.Car

class ReportFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getDatabase(requireContext())

        val data = db.CarDao().getAll()
        val arrayAdapter = ArrayAdapter<Car>(requireContext(), android.R.layout.simple_list_item_1)
        arrayAdapter.addAll(data)

        view.findViewById<ListView>(R.id.dataList)?.adapter = arrayAdapter
    }
}