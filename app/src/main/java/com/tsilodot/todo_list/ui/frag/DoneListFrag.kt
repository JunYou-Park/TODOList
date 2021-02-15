package com.tsilodot.todo_list.ui.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.adapter.NoteAdapter
import com.tsilodot.todo_list.databinding.FragmentDoneListBinding
import com.tsilodot.todo_list.databinding.FragmentNoteListBinding
import com.tsilodot.todo_list.db.note.NoteViewModel
import com.tsilodot.todo_list.listener.NoteItemListener
import com.tsilodot.todo_list.model.NoteVo
import com.tsilodot.todo_list.util.Const
import com.tsilodot.todo_list.util.ResolutionHelper.Companion.getDimension

class DoneListFrag : Fragment() {

    private lateinit var navController: NavController
    private val binding: FragmentDoneListBinding by lazy { FragmentDoneListBinding.inflate(layoutInflater) }

    private var noteAdapter = NoteAdapter(false)

    private val noteViewModel: NoteViewModel by lazy { ViewModelProvider(this, NoteViewModel.NoteFactory(requireActivity().application)).get(
        NoteViewModel::class.java) }

    private var list = listOf<NoteVo>()
    private val selectList = arrayListOf<NoteVo>()

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                noteViewModel.delete(list[position])
            }
        }

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

        binding.rvDoneList.adapter = noteAdapter

        setObserver()

        setOnClickListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done_list -> {

                return true
            }

            R.id.edit_note -> {
                noteAdapter = NoteAdapter(true)
                binding.rvDoneList.adapter = noteAdapter
                noteAdapter.submitList(list)

                binding.constDoneListButtonContainer.isVisible = true

                val constraintSet = ConstraintSet()
                constraintSet.clear(R.id.const_done_list_button_container)
                constraintSet.connect(R.id.const_done_list_button_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
                constraintSet.connect(R.id.const_done_list_button_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.const_done_list_button_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                constraintSet.constrainWidth(R.id.const_done_list_button_container, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.const_done_list_button_container, ConstraintSet.WRAP_CONTENT)

                constraintSet.clear(R.id.rv_done_list)
                constraintSet.connect(R.id.rv_done_list, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.rv_done_list, ConstraintSet.BOTTOM, R.id.const_done_list_button_container, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.rv_done_list, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.rv_done_list, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                constraintSet.constrainWidth(R.id.rv_done_list, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.rv_done_list, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.applyTo(binding.constDoneListParentContainer)

                setAdapter()

                return true
            }
        }
        return false
    }

    private fun setOnClickListener() {

        binding.btnDoneListCancel.setOnClickListener {
            noteAdapter = NoteAdapter(false)
            binding.rvDoneList.adapter = noteAdapter
            noteAdapter.submitList(list)

            val constraintSet = ConstraintSet()
            constraintSet.clear(R.id.rv_done_list)
            constraintSet.connect(R.id.rv_done_list, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.rv_done_list, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(R.id.rv_done_list, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
            constraintSet.connect(R.id.rv_done_list, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            constraintSet.constrainWidth(R.id.rv_done_list, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainHeight(R.id.rv_done_list, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.applyTo(binding.constDoneListParentContainer)

            binding.constDoneListButtonContainer.isVisible = false

            setAdapter()
        }

        binding.btnDoneListComplete.setOnClickListener {
            for (item in selectList) {
                item.done = false
                noteViewModel.update(item)
            }
            noteViewModel.getAllNotes(100, true)

            selectList.clear()
            val completeText = "${getString(R.string.complete)}(${selectList.size})"
            binding.btnDoneListComplete.text = completeText
        }

        setAdapter()
    }

    private fun setObserver() {
        noteViewModel.getAllNotes(100, true).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                list = it
                noteAdapter.submitList(list)
            }
        })
    }

    private fun setAdapter() {
        noteAdapter.setOnListener(object : NoteItemListener {
            override fun onClick(model: Any, position: Int) {
                val noteVo = model as NoteVo
                val arg = Bundle()
                arg.putParcelable(Const.MODEL_BUNDLE, noteVo)
                navController.navigate(R.id.action_doneListFrag_to_newNoteFrag, arg)
            }

            override fun onSelect(model: Any, option: String) {
                val noteVo = model as NoteVo
                when (option) {
                    "add" -> selectList.add(noteVo)
                    "remove" -> selectList.remove(noteVo)
                }
                val completeText = "${getString(R.string.complete)}(${selectList.size})"
                binding.btnDoneListComplete.text = completeText
            }
        })

        binding.rvDoneList.apply {
            setHasFixedSize(true)
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }

    }

}