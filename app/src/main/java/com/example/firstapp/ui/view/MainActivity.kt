package com.example.firstapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.R
import com.example.firstapp.R.layout
import com.example.firstapp.data.api.ApiHelper
import com.example.firstapp.data.api.RetrofitBuilder
import com.example.firstapp.ui.adapter.GifAdapter
import com.example.firstapp.ui.adapter.PaginationScrollListener
import com.example.firstapp.ui.base.ViewModelFactory
import com.example.firstapp.ui.viewmodel.MainViewModel
import com.example.firstapp.utils.Status


class MainActivity : AppCompatActivity() {
    private val gifAdapter = GifAdapter()
    private lateinit var viewModel: MainViewModel
    private lateinit var apiKey: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiKey = getString(R.string.api_key)
        setContentView(layout.activity_main)
        setupViewModel()//
        setupUI()//
        setupObservers()
        textlistener()
    }


    private fun textlistener() {
        val edittext = findViewById<EditText>(R.id.et)

        edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val query = s.toString()
                viewModel.getGifs(apiKey, query)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //not used
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //not used
            }
        })

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView.adapter = gifAdapter
        recyclerView?.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as GridLayoutManager) {
            override fun isLoading(): Boolean {
                return viewModel.gifs.value?.status == Status.LOADING
            }

            override fun loadMoreItems() {
                val item = findViewById<EditText>(R.id.et)
                viewModel.getGifs(apiKey, item.text.toString(), true)
            }
        })
    }

    private fun setupObservers() {
        val imagexml = findViewById<ImageView>(R.id.imageView)

        viewModel.gifs.observe(this, Observer { gifResponse ->
            gifResponse?.let { resource ->
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)

                when (resource.status) {
                    Status.SUCCESS -> {
                        imagexml.isVisible = false
                        progressBar.isVisible = false

                        resource.data?.let { gifs ->
                            val gifList = gifs.data.map {
                                it.images.downsized.url
                            }
                            if (gifList.isEmpty()) imagexml.isVisible = true
                            retrieveList(gifList)
                        }
                    }
                    Status.ERROR -> {
                        progressBar.isVisible = false
                        imagexml.isVisible = false
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.isVisible = true
                        imagexml.isVisible = false
                    }
                }
            }
        })
    }

    private fun retrieveList(gifs: List<String>) {
        gifAdapter.apply {
            addItems(gifs)
        }
    }


}
//android edittext listener
//