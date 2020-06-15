package com.example.wings.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wings.R
import com.example.wings.adapter.CustomerAdapter
import com.example.wings.model.CustomerResponse
import com.example.wings.network.ApiParams
import com.example.wings.network.ApiResponseInterface
import com.example.wings.network.CommonCall
import com.example.wings.utils.Const
import com.squareup.picasso.Picasso
import com.techfirst.marksmentor.support.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_employee_main.*


class EmployeeMainActivity : AppCompatActivity(), CustomerAdapter.onClick {
    private var commonCall: CommonCall? = null
    lateinit var mInterFace: ApiResponseInterface
    internal var size: Int = 10
    internal var since: Int = 0
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    lateinit var customerAdapter: CustomerAdapter
    private var searchView: SearchView? = null
    lateinit var dataItem: MutableList<CustomerResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_main)

        setSupportActionBar(toolbar)

        dataItem = arrayListOf()
        val layoutManager = LinearLayoutManager(this)
        Recycler_customer.layoutManager = layoutManager

        customerAdapter = CustomerAdapter(dataItem, this)
        Recycler_customer.adapter = customerAdapter

        Recycler_customer?.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                Log.e("COUNT  >>", since.toString())
                progress_bar.visibility = View.VISIBLE
                commonCall!!.customer(size, since)
            }
        })

        setupNetwork()
        progress_bar.visibility = View.VISIBLE
        commonCall!!.customer(size, since)

    }


    private fun setupNetwork() {
        mInterFace = object : ApiResponseInterface {

            override fun isError(errorCode: String, ServiceCode: Int) {
                progress_bar.visibility = View.GONE
                if (dataItem.isEmpty())
                    Const.showSnackBar(this@EmployeeMainActivity, "Customer Not Found!")
            }

            @Suppress("UNCHECKED_CAST")
            override fun isSuccess(response: Any, ServiceCode: Int) {
                when (ServiceCode) {
                    ApiParams.GET_CUSTOMER -> {
                        progress_bar.visibility = View.GONE
                        val conversationListResponse = response as List<CustomerResponse>
                        dataItem = (conversationListResponse as MutableList<CustomerResponse>)
                        if (dataItem.isNotEmpty()) {
                            since = dataItem[dataItem.size - 1].id!!
                            isLoading = false
                            customerAdapter.addData(dataItem)
                        }
                    }
                }
            }
        }
        commonCall = CommonCall(this, mInterFace)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        // listening to search query text change
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // filter recycler view when query submitted
                customerAdapter.filter!!.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // filter recycler view when text is changed
                customerAdapter.filter!!.filter(query)
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onProfileClick(url: String) {
        val dialog = Dialog(this, R.style.MyDialogTheme2)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_imagepreview)
        val img_image =
            dialog.findViewById<ImageView>(R.id.Img_profile)
        val img_close =
            dialog.findViewById<ImageView>(R.id.Img_close)

        if (!TextUtils.isEmpty(url)) {
            Picasso.get()
                .load(url)
                .into(img_image)
        }

        img_close.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun refreshCounter() {
        if (customerAdapter.getSelected()!!.size != 0) {
            Txt_count.visibility = View.VISIBLE
            Txt_count.text = "Selected : " + customerAdapter.getSelected()!!.size
        } else {
            Txt_count.visibility = View.GONE
        }

    }
}
