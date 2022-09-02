package com.udacity.asteroidradar.detail


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MainViewModelFactory

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewmodel

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val application= requireNotNull(this.activity).application
        val dataSource= AsteroidDatabase.getInstance(application).asteroidDatabaseDAO
        val viewModelFactory= DetailViewModelFactory(dataSource,application)
        viewModel=  ViewModelProvider(
            this,viewModelFactory).get(DetailViewmodel::class.java)

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
        val isSaved = DetailFragmentArgs.fromBundle(requireArguments()).isSaved
if (!isSaved)
    viewModel.isAstoridSaved(asteroid)
        binding.asteroid = asteroid
        when(isSaved){
            true->{
                binding.save.visibility=View.GONE
                binding.delete.visibility=View.VISIBLE
            }
            false->{

                binding.save.visibility=View.VISIBLE
                binding.delete.visibility=View.GONE
            }
        }
        viewModel.isAstroidSaved.observe(viewLifecycleOwner, Observer {
            it.let {
                when(it){
                    true->{
                        binding.save.visibility=View.GONE
                        binding.delete.visibility=View.VISIBLE
                    }
                    false->{

                        binding.save.visibility=View.VISIBLE
                        binding.delete.visibility=View.GONE
                    }
                }
            }
        })
        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        binding.save.setOnClickListener{
            viewModel.saveAstorroid(asteroid)
            binding.save.visibility=View.GONE
            binding.delete.visibility=View.VISIBLE

        }
        binding.delete.setOnClickListener{
            viewModel.deleteAstorroid(asteroid)
            binding.save.visibility=View.VISIBLE
            binding.delete.visibility=View.GONE

        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
