package com.example.project2.ui.main.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.databinding.ActivityAddToDoBinding
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_add_to_do.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.SimpleDateFormat
import java.util.*

class AddToDoActivity : AppCompatActivity(), ToDoRepository.OnRepositoryListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var viewModel: ViewModelToDo
    private lateinit var binding: ActivityAddToDoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        //ViewModel
        viewModel = ViewModelProvider(this).get(ViewModelToDo::class.java)
        //Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_do)
        binding.viewModel = viewModel
        binding.titleInput = edit_text_title
        binding.item = this

        viewModel.setRepositoryListener(this)

        //toolbar
        text_view_toolbar_title.text = "New Task"
        setSupportActionBar(toolbar_main)

        viewModel.setObservableDateAndTimeField()

    }

    fun showDatePickerDialog() {
        var calender = Calendar.getInstance()
        var year = calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var day = calender.get(Calendar.DAY_OF_MONTH)
        var dialog = DatePickerDialog(this, android.R.style.Theme_Material_Dialog, this, year, month, day)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    fun showTimePickerDialog() {

        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR)
        var minute = calendar.get(Calendar.MINUTE)
        var dialog = TimePickerDialog(this, android.R.style.Theme_Material_Dialog,this, hour, minute, true)
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todo_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_close -> {finish()}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUploadSuccess() {
        toast("Upload Success")
        finish()
    }

    override fun onDeleteSuccess() {
    }

    override fun onUploadFailure() {
        toast("Upload failed")
        finish()
    }

    override fun onUpdateSuccess() {
    }

    override fun onDateSet(d: DatePicker?, year: Int, month: Int, day: Int) {
        var mm = (month + 1)
        var calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        var formatter = SimpleDateFormat("EEE, d MMM yyyy")
        text_view_date.text = formatter.format(calendar.time)
        text_view_time.animate().alpha(1.0f).translationY(text_view_date.height.toFloat()).also{
            text_view_time.visibility = View.VISIBLE
            it.duration = 500
            it.start()
        }
    }

    override fun onTimeSet(t: TimePicker?, hour: Int, min: Int) {
        var calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY,hour)
        calender.set(Calendar.MINUTE,min)
        val formatter = SimpleDateFormat("h:mm a")
        text_view_time.text = formatter.format((calender.time))

    }
}