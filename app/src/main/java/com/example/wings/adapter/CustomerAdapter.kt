package com.example.wings.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wings.R
import com.example.wings.model.CustomerResponse
import com.example.wings.utils.CircleTransform
import com.squareup.picasso.Picasso


/**
 * Created by VIS_4 on 3/16/2018.
 */

class CustomerAdapter(
    private val dataList: MutableList<CustomerResponse>,
    private val listener: onClick
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>(), Filterable {

    private var customerListFiltered: MutableList<CustomerResponse> = arrayListOf()

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var cardView: CardView = itemView.findViewById(R.id.Cardview)
        var imgProfile: ImageView = itemView.findViewById(R.id.Img_profile)
        var imgChecked: ImageView = itemView.findViewById(R.id.Img_checked)
        var txtName: TextView = itemView.findViewById(R.id.Txt_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return ViewHolder(view)
    }

    @Suppress("UNCHECKED_CAST", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = customerListFiltered[position].login
        if (!TextUtils.isEmpty(customerListFiltered[position].avatarUrl)) {
            Picasso.get().load(customerListFiltered[position].avatarUrl)
                .transform(CircleTransform()).into(holder.imgProfile)
        }

        holder.imgProfile.setOnClickListener {
            listener.onProfileClick(customerListFiltered[position].avatarUrl!!)
        }

        holder.cardView.setOnClickListener {
            customerListFiltered[position].isChecked = customerListFiltered[position].isChecked != true
            holder.imgChecked.visibility =
                if (customerListFiltered[position].isChecked!!) View.VISIBLE else View.INVISIBLE

            listener.refreshCounter()
        }

    }

    override fun getItemCount(): Int {
        return customerListFiltered.size
    }

    fun getData(): List<CustomerResponse>? {
        return if (!dataList.isNullOrEmpty())
            dataList
        else
            null
    }


    fun addData(listItems: MutableList<CustomerResponse>) {
        dataList.addAll(listItems)
        customerListFiltered.addAll(listItems)
        val size = customerListFiltered.size
        val sizeNew = customerListFiltered.size
        notifyItemRangeChanged(size, sizeNew)
    }


    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter? {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    customerListFiltered = dataList
                } else {
                    val filteredList: MutableList<CustomerResponse> = ArrayList()
                    for (row in dataList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.login!!.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    customerListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = customerListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                customerListFiltered = filterResults.values as MutableList<CustomerResponse>
                notifyDataSetChanged()
            }
        }
    }

    interface onClick {
        fun onProfileClick(url: String)
        fun refreshCounter()
    }

    fun getSelected(): ArrayList<CustomerResponse>? {
        val selected: ArrayList<CustomerResponse> = ArrayList()
        for (i in 0 until customerListFiltered.size) {
            if (customerListFiltered.get(i).isChecked!!) {
                selected.add(customerListFiltered[i])
            }
        }
        return selected
    }

}


