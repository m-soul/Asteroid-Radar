package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.RecyclerViewAdapter
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val db = AsteroidDataBase.getInstance(application)
        val factory = ViewModelFactory(application, db)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val adapter = RecyclerViewAdapter(RecyclerViewAdapter.AsteroidListener {
            viewModel.onAsteroidClicked(it)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onNavigateDone()
            }


        })
        viewModel.asteroidsChanged.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                viewModel.asteroids.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.submitList(it)
                    }
                })
                viewModel.asteroidChangedDone()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.show_rent_menu -> viewModel.filterTodayAsteroids()
            R.id.show_buy_menu -> viewModel.filterSavedAsteroids()
            R.id.show_all_menu -> viewModel.filterWeekAsteroid()
        }
        return true
    }
}
