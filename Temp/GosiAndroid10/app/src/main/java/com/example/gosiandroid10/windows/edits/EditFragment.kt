package com.example.gosiandroid10.windows.edits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.core.widget.doOnTextChanged
import com.example.gosiandroid10.R
import com.example.gosiandroid10.data.AppDb
import com.example.gosiandroid10.model.Car
import com.example.gosiandroid10.model.CarState

class EditFragment : Fragment() {

    private var viewModel = EditViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getDatabase(requireContext())
        val id = requireArguments().getInt("carId")

        if (id > 0) {
            val item = db.CarDao().get(id)
            setItem(item)
        }

        view.apply {
            findViewById<EditText>(R.id.editTextName)?.doOnTextChanged { text, start, before, count ->
                viewModel.name = text.toString()
            }

            findViewById<EditText>(R.id.editTextClient)?.doOnTextChanged { text, start, before, count ->
                viewModel.client = text.toString()
            }

            findViewById<Button>(R.id.buttonSave)?.setOnClickListener {
                val client = if (viewModel.state == CarState.Used.name) viewModel.client else ""

                if (id > 0) {
                    val newItem = Car(viewModel.name, client, viewModel.state, id)
                    db.CarDao().update(newItem)
                } else {
                    val newItem = Car(viewModel.name, client, viewModel.state)
                    db.CarDao().insert(newItem)
                }
                findNavController().popBackStack()
            }

            findViewById<Button>(R.id.buttonDelete)?.setOnClickListener {
                if (id > 0) {
                    db.CarDao().delete(id)
                    findNavController().popBackStack()
                }
            }

            findViewById<CheckBox>(R.id.checkBoxNew)?.setOnClickListener {
                val flag = viewModel.state == CarState.New.name
                if (flag) {
                    viewModel.state = CarState.None.name
                } else {
                    viewModel.state = CarState.New.name
                }
                setChecked(CarState.valueOf(viewModel.state))
            }

            findViewById<CheckBox>(R.id.checkBoxUsed)?.setOnClickListener {
                val flag = viewModel.state == CarState.Used.name
                if (flag) {
                    viewModel.state = CarState.None.name
                } else {
                    viewModel.state = CarState.Used.name
                }
                setChecked(CarState.valueOf(viewModel.state))
            }

            findViewById<CheckBox>(R.id.checkBoxDeleted)?.setOnClickListener {
                val flag = viewModel.state == CarState.Deleted.name
                if (flag) {
                    viewModel.state = CarState.None.name
                } else {
                    viewModel.state = CarState.Deleted.name
                }
                setChecked(CarState.valueOf(viewModel.state))
            }
        }
    }

    private fun setItem(car: Car) {
        view?.apply {
            findViewById<EditText>(R.id.editTextName)?.setText(car.name)
            findViewById<EditText>(R.id.editTextClient)?.setText(car.client)

            setChecked(CarState.valueOf(car.car_state))
        }

        viewModel.name = car.name
        viewModel.client = car.client
    }

    private fun setChecked(state: CarState) {
        view?.apply {
            findViewById<CheckBox>(R.id.checkBoxNew)?.isChecked = state == CarState.New
            findViewById<CheckBox>(R.id.checkBoxUsed)?.isChecked = state == CarState.Used
            findViewById<CheckBox>(R.id.checkBoxDeleted)?.isChecked = state == CarState.Deleted
        }

        viewModel.state = state.name
    }
}