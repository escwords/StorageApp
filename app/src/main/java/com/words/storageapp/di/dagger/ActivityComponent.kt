package com.words.storageapp.di.dagger
//
//import android.app.Activity
//import android.content.Context
//import com.words.storageapp.ui.main.MainActivity
//import com.words.storageapp.ui.search.SearchResultActivity
//import dagger.BindsInstance
//import dagger.Component
//
//@Component(modules = [SearchComponentModule::class, DataStorageModule::class])
//interface ActivityComponent {
//
//    @Component.Builder
//    interface Builder{
//        fun build(@BindsInstance context: Context): ActivityComponent
//        @BindsInstance fun activity(activity: Activity): Builder
//    }
//    fun inject(activity: MainActivity)
//    fun inject(activity: SearchResultActivity)
//}
//
//fun inject(activity: Activity){
//    DaggerActivityComponent.Builder
//        .activity(activity)
//        .build(activity.applicationContext)
//        .inject(activity)
//}
