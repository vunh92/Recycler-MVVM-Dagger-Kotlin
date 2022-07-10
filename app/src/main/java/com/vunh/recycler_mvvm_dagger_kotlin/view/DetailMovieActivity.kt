package com.vunh.recycler_mvvm_dagger_kotlin.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.vunh.recycler_mvvm_dagger_kotlin.BaseApp
//import com.vunh.recycler_mvvm_dagger_kotlin.BaseApp
import com.vunh.recycler_mvvm_dagger_kotlin.databinding.ActivityDetailMovieBinding
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.detail_movie.DetailMovieViewModel
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.detail_movie.DetailMovieViewModelFactory
import javax.inject.Inject

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var detailMovieBinding: ActivityDetailMovieBinding
    lateinit var viewModel: DetailMovieViewModel
    @Inject
    lateinit var viewModelFactory: DetailMovieViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        detailMovieBinding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(detailMovieBinding.root)
//        viewModelFactory = DetailMovieViewModelFactory(BaseApp.baseApp!!.recyclerRepositoryImpl)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailMovieViewModel::class.java)

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
        actionBar?.title = "Detail"

        val bundle: Bundle? = intent.getBundleExtra("bundleDetailMovie")
        bundle?.let {
            bundle.apply {
                val movie = getSerializable("movie") as Movie?
                if(movie != null) {
                    viewModel.getMovie(movie.movieId)
                }
            }
        }
    }

    private fun initializeViewModel(){
        viewModel.showResult.observe(this, Observer {
            if(it != null) {
                Snackbar.make(detailMovieBinding.root, "Success", Snackbar.LENGTH_LONG).show()
                Glide.with(this).load(it.imageUrl).into(detailMovieBinding.detailMovieThumbnail);
                detailMovieBinding.detailCategoryTv.setText(it.category)
                detailMovieBinding.detailNameTv.setText(it.name)
                detailMovieBinding.detailDescTv.setText(it.desc)
            }
        })
        viewModel.showLoading.observe(this, Observer {
            if (it)
                detailMovieBinding.detailLoadingSpinner.visibility = View.VISIBLE
            else
                detailMovieBinding.detailLoadingSpinner.visibility = View.GONE
        })
        viewModel.showError.observe(this, Observer {
            Snackbar.make(detailMovieBinding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }
}