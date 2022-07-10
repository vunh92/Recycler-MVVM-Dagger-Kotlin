package com.vunh.recycler_mvvm_dagger_kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vunh.recycler_mvvm_dagger_kotlin.BaseApp
//import com.vunh.recycler_mvvm_dagger_kotlin.BaseApp.Companion.baseApp
import com.vunh.recycler_mvvm_dagger_kotlin.databinding.ActivityCreateMovieBinding
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.create_movie.CreateMovieViewModel
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.create_movie.CreateMovieViewModelFactory
import javax.inject.Inject

class CreateMovieActivity : AppCompatActivity() {
    private lateinit var createMovieBinding: ActivityCreateMovieBinding
    lateinit var viewModel: CreateMovieViewModel
    @Inject
    lateinit var viewModelFactory: CreateMovieViewModelFactory
    var isUpdate = false
    var baseMovie = Movie(
        "",
        "High Rated",
        "https://howtodoandroid.com/images/wonder.jpg",
        "Wonder",
        "Wonder is a 2017 American drama film directed by Stephen Chbosky and written by Jack Thorne , Steve Conrad and Stephen Chbosky based on the 2012 novel of the same name by R.J. Palacio"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        createMovieBinding = ActivityCreateMovieBinding.inflate(layoutInflater)
        setContentView(createMovieBinding.root)
//        viewModelFactory = CreateMovieViewModelFactory(baseApp!!.recyclerRepositoryImpl)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateMovieViewModel::class.java)

        initializeView()
        initializeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeView(){
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        val bundle: Bundle? = intent.getBundleExtra("bundleCreateMovie")
        bundle?.let {
            bundle.apply {
                actionBar?.title = getString("title")
                val movie = getSerializable("movie") as Movie?
                if(movie != null) {
                    baseMovie = movie
                    isUpdate = true
                }
                createMovieBinding.createCategoryEdt.setText(baseMovie.category)
                createMovieBinding.createImageUrlEdt.setText(baseMovie.imageUrl)
                createMovieBinding.createNameEdt.setText(baseMovie.name)
                createMovieBinding.createDescEdt.setText(baseMovie.desc)
            }
        }
        createMovieBinding.createAcceptBtn.setOnClickListener {
            if(viewModel.showLoading.value == true) return@setOnClickListener
            baseMovie.category = createMovieBinding.createCategoryEdt.text.toString().trim()
            baseMovie.imageUrl = createMovieBinding.createImageUrlEdt.text.toString().trim()
            baseMovie.name = createMovieBinding.createNameEdt.text.toString().trim()
            baseMovie.desc = createMovieBinding.createDescEdt.text.toString().trim()
            if (isUpdate) {
                viewModel.updateMovie(baseMovie)
            }else {
                viewModel.insertMovie(baseMovie)
            }
        }
    }

    private fun initializeViewModel(){
        viewModel.showResult.observe(this, Observer {
            if(it) Snackbar.make(createMovieBinding.root, "Success", Snackbar.LENGTH_LONG).show()
            viewModel.finishActivity(this)
        })
        viewModel.showLoading.observe(this, Observer {
            if (it)
                createMovieBinding.createLoadingSpinner.visibility = View.VISIBLE
            else
                createMovieBinding.createLoadingSpinner.visibility = View.GONE
        })
        viewModel.showError.observe(this, Observer {
            Snackbar.make(createMovieBinding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }
}