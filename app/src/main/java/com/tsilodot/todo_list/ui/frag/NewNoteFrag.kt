package com.tsilodot.todo_list.ui.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.databinding.FragmentNewNoteBinding
import com.tsilodot.todo_list.db.note.NoteViewModel
import com.tsilodot.todo_list.listener.CalendarDialogListener
import com.tsilodot.todo_list.model.NoteVo
import com.tsilodot.todo_list.ui.dialog.CalendarDialog
import com.tsilodot.todo_list.util.Const.MODEL_BUNDLE
import com.tsilodot.todo_list.util.ShowMessage
import com.tsilodot.todo_list.util.ShowMessage.Companion.showMsg
import org.joda.time.DateTime


class NewNoteFrag : Fragment() {

    private val binding: FragmentNewNoteBinding by lazy { FragmentNewNoteBinding.inflate(layoutInflater)}
    private val nowDate = DateTime()
    private var startSelectedDate = DateTime()
    private var endSelectedDate: DateTime? = null
    private var id: Int? = null

    private lateinit var navController: NavController

    private var noteVo: NoteVo? = null
    private val noteViewModel: NoteViewModel by lazy { ViewModelProvider(this, NoteViewModel.NoteFactory(requireActivity().application)).get(NoteViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        if(arguments!=null) noteVo = requireArguments().getParcelable(MODEL_BUNDLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initBinding()

        setOnClickListener()

    }

    private fun initBinding(){
        binding.tvNewNoteStartDate.text = startSelectedDate.toString("yyyy년 MM월 dd일 (E)")

        if(noteVo!=null){
            id = noteVo!!.id

            binding.etNewNoteTitle.setText(noteVo!!.title)
            binding.etNewNoteContent.setText(noteVo!!.content)

            val startDate = DateTime(noteVo!!.startDate)
            binding.tvNewNoteStartDate.text = startDate.toString("yyyy년 MM월 dd일 (E)")
            startSelectedDate = startDate

            if(noteVo!!.endDate!= 0L) {
                val endDate = DateTime(noteVo!!.endDate)
                binding.tvNewNoteEndDate.text = endDate.toString("yyyy년 MM월 dd일 (E)")
                endSelectedDate = endDate
            }
        }

    }

    private fun setOnClickListener(){
        binding.lineNewNoteStartDateContainer.setOnClickListener {
            createCalendarDialog("start")
        }

        binding.lineNewNoteEndDateContainer.setOnClickListener {
            createCalendarDialog("end")
        }
    }

    private fun createCalendarDialog(tag: String){
        var dialog: CalendarDialog? = null
        dialog = if(tag=="start") CalendarDialog(selectedDate = startSelectedDate, endDate = nowDate.plusYears(2))
        else CalendarDialog(selectedDate = endSelectedDate?:startSelectedDate, endDate = nowDate.plusYears(2))

        dialog.show(requireActivity().supportFragmentManager, tag)
        dialog.setDialogInterface(object : CalendarDialogListener {
            override fun onClicked(date: String, jodaDate: DateTime) {
                if(tag=="start"){
                    startSelectedDate = jodaDate
                    binding.tvNewNoteStartDate.text = date
                } else{
                    endSelectedDate = jodaDate
                    binding.tvNewNoteEndDate.text = date
                }

            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> {
                if(binding.etNewNoteTitle.text.toString() == ""){
                    showMsg(requireContext(), getString(R.string.plz_input_title))
                }
                else{
                    val startLong = startSelectedDate.millis
                    if(endSelectedDate != null){
                        val endLong = endSelectedDate!!.millis
                        if(startLong>endLong){
                            showMsg(requireContext(), getString(R.string.error_note_date))
                        }
                        else{
                            val noteVo = NoteVo(id = id, title = binding.etNewNoteTitle.text.toString(), content = binding.etNewNoteContent.text.toString(), startDate = startLong, endDate = endLong)
                            noteViewModel.insert(noteVo)
                            navController.popBackStack()
                        }
                    }
                    else{
                        val noteVo = NoteVo(id = id, title = binding.etNewNoteTitle.text.toString(), content = binding.etNewNoteContent.text.toString(), startDate = startLong)
                        noteViewModel.insert(noteVo)
                        navController.popBackStack()
                    }
                }
                return true
            }
        }
        return false
    }

}