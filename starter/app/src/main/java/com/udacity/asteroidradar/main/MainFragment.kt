package com.udacity.asteroidradar.main

import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.*
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.ResourceManagerInternal.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.RecycleAdapter
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import okhttp3.internal.platform.Platform.get
import java.lang.System.load
import java.util.logging.Logger

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
//    by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }
private lateinit var binding:FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val application= requireNotNull(this.activity).application
        val viewModelFactory=MainViewModelFactory(application)
        viewModel=  ViewModelProvider(
            this,viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        val adapter =RecycleAdapter(AsteroidListener {
            viewModel.onAstroidClicked(it)
        })
        viewModel.navigateToAstroidDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it,viewModel.astroidDataFilter.value==DataFilter.SAVED))
                viewModel.onAstroidDetailsNavigated()
            }
        })

        binding.asteroidRecycler.adapter=adapter
        setHasOptionsMenu(true)


        viewModel.astroidDataFilter.observe(viewLifecycleOwner, Observer {
          when(it){

                    DataFilter.TODAY->viewModel.asteroidsToday.observe(viewLifecycleOwner, Observer {

                            binding.statusLoadingWheel.visibility=View.GONE

                        adapter.addList(it)

                    })
                    DataFilter.WEEK->viewModel.asteroidsWeek.observe(viewLifecycleOwner, Observer {

                            binding.statusLoadingWheel.visibility=View.GONE

                        adapter.addList(it)

                    })
              DataFilter.SAVED->viewModel.asteroidsSaved.observe(viewLifecycleOwner, Observer {
                  binding.statusLoadingWheel.visibility=View.GONE

                  adapter.addList(it)

                    })


            }
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.textView.text=it.title
                binding.activityMainImageOfTheDay.contentDescription="image of the day : "+it.title
                Picasso.with(context)
                    .load(it.url)
                    .into(binding.activityMainImageOfTheDay)

            }

        })
        viewModel.astroidStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                Status.LOADING->{
                    if(adapter.currentList.isEmpty()&&viewModel.astroidDataFilter.value!=DataFilter.SAVED){
                        binding.statusLoadingWheel.visibility=View.VISIBLE
                    }else{
                        binding.statusLoadingWheel.visibility=View.GONE
                    }
                }
                Status.DONE-> binding.statusLoadingWheel.visibility=View.GONE
                Status.ERROR->{
                    if(adapter.currentList.isEmpty()&&viewModel.astroidDataFilter.value!=DataFilter.SAVED){
                        binding.statusLoadingWheel.visibility=View.VISIBLE
                    }else{
                        binding.statusLoadingWheel.visibility=View.GONE
                    }
                viewModel.refresh()
                }
                    else->binding.statusLoadingWheel.visibility=View.GONE
            }
        })
        viewModel.PictureStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                Status.LOADING->binding.pictureStatusWheel.visibility=View.VISIBLE
                Status.DONE->binding.pictureStatusWheel.visibility=View.GONE
                Status.ERROR->viewModel.getPictureOfDay()
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.statusLoadingWheel.visibility=View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title.toString()){
            getString(R.string.next_week_asteroids)->viewModel.setDataFilter(DataFilter.WEEK)
            getString(R.string.today_asteroids)->viewModel.setDataFilter(DataFilter.TODAY)
            getString(R.string.saved_asteroids)->viewModel.setDataFilter(DataFilter.SAVED)
        }

        return true
    }
}

