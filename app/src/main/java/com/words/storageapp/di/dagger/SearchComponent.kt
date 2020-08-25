package com.words.storageapp.di.dagger

import com.words.storageapp.ui.search.SearchResultActivity
import dagger.Subcomponent

@SearchScope
@Subcomponent
interface SearchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchComponent
    }

    fun inject(searchActivity: SearchResultActivity)
}