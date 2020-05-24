package com.nikhil.bookstore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.bookstore.R
import com.nikhil.bookstore.activity.DescriptionActivity
import com.nikhil.bookstore.models.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtRating.text = book.bookRating
        holder.txtPrice.text = book.bookPrice
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover)
            .into(holder.imgBookLogo)

        holder.llContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }
    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtAuthor)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val imgBookLogo: ImageView = view.findViewById(R.id.imgBookLogo)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }
}