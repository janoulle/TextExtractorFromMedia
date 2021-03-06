package com.janeullah.textextractorfrommedia.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.adapter.ImageViewAdapter
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.module.GlideApp
import com.janeullah.textextractorfrommedia.viewholder.ImageViewHolder
import kotlinx.android.synthetic.main.activity_display_tweet_images.*

class DisplayTweetImages : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var tweetId: String
    private lateinit var listOfMediaEntities: ArrayList<ImageInformation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_tweet_images)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewManager = LinearLayoutManager(this)

        tweetId = intent.getStringExtra(IntentNames.TWEET_ID) ?: ""
        listOfMediaEntities = intent.getParcelableArrayListExtra(IntentNames.TWEET_MEDIA_LIST)

        viewAdapter = ImageViewAdapter(this, listOfMediaEntities)

        recyclerView = image_recycler_view.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

            //https://medium.com/@szholdiyarov/how-to-add-divider-to-list-and-recycler-views-858344450401
            addItemDecoration(DividerItemDecoration(this@DisplayTweetImages, DividerItemDecoration.VERTICAL) .apply {
                setDrawable(getDrawable(R.drawable.divider))
            })

            setRecyclerListener {
                RecyclerView.RecyclerListener { holder ->
                    val imageViewHolder = holder as ImageViewHolder
                    GlideApp.with(this@DisplayTweetImages).clear(imageViewHolder.imageView)
                }
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            android.R.id.home -> {

                // Using navigateUpFromSameTask() is suitable only when your app is the owner of the current task
                // (that is, the user began this task from your app). If that's not true and your activity was started in a task
                // that belongs to a different app, then navigating Up should create a new task that belongs to your app,
                // which requires that you create a new back stack.

                // Respond to the action bar's Up/Home button
                val upIntent: Intent? = NavUtils.getParentActivityIntent(this)
                upIntent?.putExtra(IntentNames.TWEET_ID, tweetId)

                when {
                    upIntent == null -> throw IllegalStateException("No Parent Activity Intent")
                    NavUtils.shouldUpRecreateTask(this, upIntent) -> {
                        // This activity is NOT part of this app's task, so create a new task
                        // when navigating up, with a synthesized back stack.
                        TaskStackBuilder.create(this)
                                // Add all of this activity's parents to the back stack
                                .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent
                                .startActivities()
                    }
                    else -> {
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(this, upIntent)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
