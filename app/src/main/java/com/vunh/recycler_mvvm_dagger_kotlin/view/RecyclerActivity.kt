package com.vunh.recycler_mvvm_dagger_kotlin.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vunh.recycler_mvvm_dagger_kotlin.BaseApp
import com.vunh.recycler_mvvm_dagger_kotlin.adapter.RecyclerAdapter
import com.vunh.recycler_mvvm_dagger_kotlin.databinding.ActivityRecyclerBinding
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.recycler_view.RecyclerViewModel
import com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.recycler_view.RecyclerViewModelFactory
import javax.inject.Inject

class RecyclerActivity : AppCompatActivity() {
    private lateinit var recyclerBinding: ActivityRecyclerBinding
    lateinit var viewModel: RecyclerViewModel
    @Inject
    lateinit var viewModelFactory: RecyclerViewModelFactory
    lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        recyclerBinding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(recyclerBinding.root)
//        viewModelFactory = RecyclerViewModelFactory(baseApp!!.recyclerRepositoryImpl)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecyclerViewModel::class.java)

        setupRecyclerView()
        initializeView()
        initializeViewModel()
    }

    private fun initializeView(){
        recyclerBinding.recyclerRefreshBtn.setOnClickListener(View.OnClickListener {
            if (viewModel.showLoading.value == false) {
                viewModel.clearMovie()
                viewModel.getMovieList()
            }
        })
        recyclerBinding.recyclerClearBtn.setOnClickListener {
            viewModel.clearMovie()
        }
        recyclerAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            val intent = intentDetailMovieActivity(this)
            intent.putExtra("bundleDetailMovie", bundle)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        recyclerAdapter.deleteItemListener {
            viewModel.deleteMovie(it)
        }
        recyclerAdapter.updateItemListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
                putString("title", "Update")
            }
            val intent = intentCreateMovieActivity(this)
            intent.putExtra("bundleCreateMovie", bundle)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        recyclerBinding.recyclerInsertBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", "Insert")
            }
            val intent = intentCreateMovieActivity(this)
            intent.putExtra("bundleCreateMovie", bundle)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun initializeViewModel(){
        viewModel.showResult.observe(this, Observer {
            if(it) Snackbar.make(recyclerBinding.root, "Success", Snackbar.LENGTH_LONG).show()
        })
        viewModel.showLoading.observe(this, Observer {
            if (it)
                recyclerBinding.loadingSpinner.visibility = View.VISIBLE
            else
                recyclerBinding.loadingSpinner.visibility = View.GONE
        })
        viewModel.showError.observe(this, Observer {
            Snackbar.make(recyclerBinding.root, it, Snackbar.LENGTH_LONG).show()
        })
        viewModel.movieList.observe(this, Observer<List<Movie>> {
            it?.apply {
                recyclerAdapter.movieList = it
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerAdapter = RecyclerAdapter()
        recyclerBinding.recyclerRv.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@RecyclerActivity)
        }
    }

    companion object {
        fun intentCreateMovieActivity(context: Context): Intent {
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return Intent(context, CreateMovieActivity::class.java)
        }
        fun intentDetailMovieActivity(context: Context): Intent {
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return Intent(context, DetailMovieActivity::class.java)
        }
    }
}