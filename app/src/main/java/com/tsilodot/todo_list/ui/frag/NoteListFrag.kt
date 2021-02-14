package com.tsilodot.todo_list.ui.frag

import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.adapter.NoteAdapter
import com.tsilodot.todo_list.databinding.FragmentNoteListBinding
import com.tsilodot.todo_list.db.note.NoteViewModel
import com.tsilodot.todo_list.listener.NoteItemListener
import com.tsilodot.todo_list.model.NoteVo
import com.tsilodot.todo_list.util.Const.MODEL_BUNDLE
import com.tsilodot.todo_list.util.ShowMessage
import com.tsilodot.todo_list.util.ShowMessage.Companion.showLog
import com.tsilodot.todo_list.util.ShowMessage.Companion.showMsg

class NoteListFrag : Fragment() {

    private lateinit var navController: NavController
    private val binding: FragmentNoteListBinding by lazy { FragmentNoteListBinding.inflate(layoutInflater) }

    private var noteAdapter = NoteAdapter(false)

    private val noteViewModel: NoteViewModel by lazy { ViewModelProvider(this, NoteViewModel.NoteFactory(requireActivity().application)).get(NoteViewModel::class.java) }

    private var list = listOf<NoteVo>()
    private val selectList = arrayListOf<NoteVo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        binding.rvNoteList.adapter = noteAdapter

        setObserver()

        setOnClickListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_note -> {
                navController.navigate(R.id.action_noteListFrag_to_newNoteFrag)
                return true
            }

            R.id.edit_note -> {
                noteAdapter = NoteAdapter(true)
                binding.rvNoteList.adapter = noteAdapter
                noteAdapter.submitList(list)

                binding.lineNoteButtonContainer.isVisible = true

                val constraintSet = ConstraintSet()
                constraintSet.clear(R.id.line_note_button_container)
                constraintSet.connect(R.id.line_note_button_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
                constraintSet.connect(R.id.line_note_button_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.line_note_button_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                constraintSet.constrainWidth(R.id.line_note_button_container, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.line_note_button_container, ConstraintSet.WRAP_CONTENT)

                constraintSet.clear(R.id.rv_note_list)
                constraintSet.connect(R.id.rv_note_list, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.rv_note_list, ConstraintSet.BOTTOM, R.id.line_note_button_container, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.rv_note_list, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.rv_note_list, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                constraintSet.constrainWidth(R.id.rv_note_list, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.rv_note_list, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.applyTo(binding.constNoteListParentContainer)

                setAdapter()

                return true
            }
        }
        return false
    }

    private fun setOnClickListener(){
        binding.btnNoteCancel.setOnClickListener {
            noteAdapter = NoteAdapter(false)
            binding.rvNoteList.adapter = noteAdapter
            noteAdapter.submitList(list)

            val constraintSet = ConstraintSet()
            constraintSet.clear(R.id.rv_note_list)
            constraintSet.connect(R.id.rv_note_list, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.rv_note_list, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(R.id.rv_note_list, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
            constraintSet.connect(R.id.rv_note_list, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            constraintSet.constrainWidth(R.id.rv_note_list, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainHeight(R.id.rv_note_list, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.applyTo(binding.constNoteListParentContainer)

            binding.lineNoteButtonContainer.isVisible = false

            setAdapter()
        }

        binding.btnNoteDelete.setOnClickListener {
            for(item in selectList){
                noteViewModel.delete(item)
            }
            noteViewModel.getAllNotes(100)
        }

        setAdapter()
    }

    private fun setObserver(){
        noteViewModel.getAllNotes(100).observe(viewLifecycleOwner, Observer{
            if(it!=null) {
                list = it
                noteAdapter.submitList(list)
            }
        })
    }

    private fun setAdapter(){
        noteAdapter.setOnListener(object : NoteItemListener {
            override fun onClick(model: Any, position: Int) {
                val noteVo = model as NoteVo
                val arg = Bundle()
                arg.putParcelable(MODEL_BUNDLE, noteVo)
                navController.navigate(R.id.action_noteListFrag_to_newNoteFrag, arg)
            }

            override fun onSelect(model: Any, option: String) {
                val noteVo = model as NoteVo
                when (option) {
                    "add" -> selectList.add(noteVo)
                    "remove" -> selectList.remove(noteVo)
                }
                val deleteText = "${getString(R.string.delete)}(${selectList.size})"
                binding.btnNoteDelete.text = deleteText
            }

        })
    }

}