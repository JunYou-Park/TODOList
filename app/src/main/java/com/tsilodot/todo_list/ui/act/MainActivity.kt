package com.tsilodot.todo_list.ui.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.databinding.ActivityMainBinding
import com.tsilodot.todo_list.util.ShowMessage.Companion.showMsg

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private var destinationId = R.id.noteListFrag
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        title = getString(R.string.to_do_list)
        binding.tbNoteList.title = title

        navController.addOnDestinationChangedListener { _, destination, _ ->
            destinationId = destination.id
            when(destinationId){
                R.id.noteListFrag -> title = getString(R.string.to_do_list)
                R.id.newNoteFrag -> title = getString(R.string.create_new)
            }
            setToolbar()
        }


    }

    private fun setToolbar(){
        this.invalidateOptionsMenu()
        setSupportActionBar(binding.tbNoteList)
        binding.tbNoteList.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when(destinationId){
            R.id.noteListFrag -> menuInflater.inflate(R.menu.note_list_menu, menu)
            R.id.newNoteFrag -> menuInflater.inflate(R.menu.new_note_menu, menu)
        }
        return true
    }

}