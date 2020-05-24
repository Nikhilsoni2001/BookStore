package com.nikhil.bookstore.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nikhil.bookstore.Database.BookDatabase
import com.nikhil.bookstore.Database.BookEntity
import com.nikhil.bookstore.R
import com.nikhil.bookstore.unit.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtAuthor: TextView
    lateinit var txtPrice: TextView
    lateinit var txtRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var toolBar: Toolbar

    var bookId: String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        txtBookName = findViewById(R.id.txtBookName)
        txtAuthor = findViewById(R.id.txtAuthor)
        txtPrice = findViewById(R.id.txtPrice)
        txtRating = findViewById(R.id.txtRating)
        imgBookImage = findViewById(R.id.imgBookLogo)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddTOFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {

                        val success = it.getBoolean("success")

                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObject.getString("image")
                            txtBookName.text = bookJsonObject.getString("name")
                            txtAuthor.text = bookJsonObject.getString("author")
                            txtRating.text = bookJsonObject.getString("rating")
                            txtPrice.text = bookJsonObject.getString("price")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookDesc.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtAuthor.text.toString(),
                                txtPrice.text.toString(),
                                txtRating.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddToFav.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorFavourite
                                )
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor =
                                    ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }

                            btnAddToFav.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book is Added!!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text = "Remove from Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorFavourite
                                        )
                                        btnAddToFav.setBackgroundColor(favColor)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occurred!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book removed from favourites!!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text = "Add to Favourites"
                                        val noFavColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorPrimary
                                        )
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Unexpected error occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Unexpected error occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley error $it", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "e373780c62a3ec"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found!")

            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        /*Mode1 = Check DB if the book is fav or not
        * Mode2 = Add Book in Fav
        * Mode3 = Remove book from Fav*/

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    //Check DB if the book is fav or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }

                2 -> {
                    //Add Book in Fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }

                3 -> {
                    //Remove Book from the Fav
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}

