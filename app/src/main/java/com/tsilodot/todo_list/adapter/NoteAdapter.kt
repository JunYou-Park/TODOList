package com.tsilodot.todo_list.adapter

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.databinding.LayoutNoteItemBinding
import com.tsilodot.todo_list.listener.NoteItemListener
import com.tsilodot.todo_list.model.NoteVo
import com.tsilodot.todo_list.util.ResolutionHelper.Companion.getDimension
import org.joda.time.DateTime

class NoteAdapter(private val isVisible: Boolean) : ListAdapter<NoteVo, NoteAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var noteItemListener: NoteItemListener

    fun setOnListener(listener: NoteItemListener){
        noteItemListener = listener
    }

    inner class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_note_item, parent, false)) {

        private val binding by lazy { LayoutNoteItemBinding.bind(itemView) }

        private val changeBounds: ChangeBounds = ChangeBounds()
        private val transitionSet = TransitionSet()

        private val deleteList = arrayListOf<NoteVo>()

        fun onBind(item: NoteVo, position: Int){
            transitionSet.addTransition(changeBounds)
            if(!isVisible){
                val constraintSet = ConstraintSet()
                constraintSet.clear(R.id.tv_note_title)
                constraintSet.constrainWidth(R.id.tv_note_title, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.tv_note_title, ConstraintSet.WRAP_CONTENT)
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, itemView.context.getDimension(24f))
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, itemView.context.getDimension(24f))
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, itemView.context.getDimension(24f))
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.END, R.id.tv_note_date, ConstraintSet.START, itemView.context.getDimension(8f))
                TransitionManager.beginDelayedTransition(binding.lineNoteParentContainer, changeBounds)
                constraintSet.applyTo(binding.lineNoteParentContainer)
            }
            else{
                val constraintSet = ConstraintSet()
                constraintSet.clear(R.id.tv_note_title)
                constraintSet.constrainWidth(R.id.tv_note_title, ConstraintSet.MATCH_CONSTRAINT)
                constraintSet.constrainHeight(R.id.tv_note_title, ConstraintSet.WRAP_CONTENT)
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, itemView.context.getDimension(24f))
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, itemView.context.getDimension(24f))
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.START, R.id.cb_note_check, ConstraintSet.END, 0)
                constraintSet.connect(R.id.tv_note_title, ConstraintSet.END, R.id.tv_note_date, ConstraintSet.START, itemView.context.getDimension(8f))
                TransitionManager.beginDelayedTransition(binding.lineNoteParentContainer, changeBounds)
                constraintSet.applyTo(binding.lineNoteParentContainer)
            }

            binding.cbNoteCheck.isVisible = isVisible

            binding.tvNoteTitle.text = item.title



            if(item.endDate==0L){
                val startDate = DateTime(item.startDate)
                binding.tvNoteDate.text = startDate.toString("yyyy.MM.dd")
            }
            else {
                val startDate = DateTime(item.startDate)
                val endDate = DateTime(item.endDate)
                val period = "${startDate.toString("yy.MM.dd")} ~ ${endDate.toString("yy.MM.dd")}"
                binding.tvNoteDate.text = period
            }

            binding.lineNoteParentContainer.setOnClickListener {
                if(isVisible) binding.cbNoteCheck.isChecked = !binding.cbNoteCheck.isChecked
                else noteItemListener.onClick(item, position)
            }

            binding.cbNoteCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) noteItemListener.onSelect(item, "add")
                else noteItemListener.onSelect(item, "remove")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        = ViewHolder(parent)

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NoteVo>(){

            override fun areItemsTheSame(oldItem: NoteVo, newItem: NoteVo) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NoteVo, newItem: NoteVo) = oldItem == newItem

        }
    }
}