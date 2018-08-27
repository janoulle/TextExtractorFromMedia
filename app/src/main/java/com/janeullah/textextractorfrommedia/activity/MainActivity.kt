package com.janeullah.textextractorfrommedia.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    var typeSelected = "document"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.textRecognizerTypes, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        textRecognizerTypeChosen.adapter = adapter

        tweetIdField.setText(intent.getStringExtra(Constants.TWEET_ID) ?: "")

        // listener for spinner
        textRecognizerTypeChosen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeSelected = parent?.getItemAtPosition(position)?.toString() ?: "document"
            }
        }

        //listener for clicks on the submit button
        submitTweetId.setOnClickListener {
            val tweetId = tweetIdField.text.toString()
            if (!isValidTweetId(tweetId)) {
                Toast.makeText(this@MainActivity, "Please enter a valid tweet id!", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this@MainActivity, AnalyzeTweetActivity::class.java).apply {
                    putExtra(Constants.TWEET_ID, tweetIdField.text.toString())
                    putExtra(Constants.PARSE_MODE, typeSelected)
                }
                startActivity(intent)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValidTweetId(tweetId: String?): Boolean {
        try {
            tweetId?.toLong()
        } catch (e: NumberFormatException) {
            return false
        }
        return !TextUtils.isEmpty(tweetId)
    }

}
