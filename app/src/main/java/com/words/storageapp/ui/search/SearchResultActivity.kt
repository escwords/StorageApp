package com.words.storageapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.words.storageapp.MyApplication
import com.words.storageapp.R
import com.words.storageapp.util.InjectorUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchResultActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: SearchRepository

    //we instantiate searchViewModel using injectorUtil pattern
    val searchViewModel: SearchViewModel by viewModels {
        InjectorUtil.provideSearchViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val injector = (application as MyApplication).daggerComponent.searchComponent()
        injector.create().inject(this)

        setContentView(R.layout.activity_search_skill)

        val actionBar = findViewById<Toolbar>(R.id.searchAppBar)
        val navController = findNavController(R.id.searchNavHost)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.skilledFragment ||
                destination.id == R.id.photoDetail
            ) {
                actionBar.visibility = View.GONE
            } else {
                actionBar.visibility = View.VISIBLE
            }
        }
        //actionBar.setupWithNavController(navController)

        setSupportActionBar(actionBar)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data
            lifecycleScope.launch {
                query?.let { string ->
                    // resultTitle.text = it
                    searchViewModel.queryDb(string)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        //get the search view and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            //assumes that the current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            background = getDrawable(R.drawable.rectangle_shape)
            queryHint = context.getString(R.string.searchQuery)
            isSubmitButtonEnabled = true
        }
        return true
    }
}
