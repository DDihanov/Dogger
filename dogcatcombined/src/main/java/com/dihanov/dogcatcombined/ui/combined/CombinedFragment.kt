package com.dihanov.dogcatcombined.ui.combined

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dihanov.base_domain.Status
import com.dihanov.base_ui.BaseFragment
import com.dihanov.base_ui.ClickableAdapter
import com.dihanov.dogcatcombined.R

import com.dihanov.dogcatcombined.ui.combined.adapter.CombinedAdapter
import com.dihanov.dogcatcombined.ui.combined.uimodel.CombinedDogCat
import kotlinx.android.synthetic.main.combined_fragment.*
import kotlinx.android.synthetic.main.combined_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CombinedFragment : BaseFragment() {
    private val viewModel: CombinedViewModel by viewModel()
    private var combinedAdapter: CombinedAdapter? = null

    override fun subscribeObservers() {
        viewModel.combinedDogsCats.observe(viewLifecycleOwner, Observer {
            val data = it.getContentIfNotHandled()
            data?.let {
                val status = it.status
                when (status) {
                    Status.LOADING -> {
                        showLoader()
                    }
                    Status.ERROR -> {
                        hideLoader()
                    }
                    Status.SUCCESS -> {
                        hideLoader()
                        combinedAdapter?.clear()
                        combinedAdapter?.add(it.data!!)
                    }
                }
            }
        })
    }

    private fun hideLoader() {
        combined_loading.visibility = View.GONE
    }

    private fun showLoader() {
        combined_loading.visibility = View.VISIBLE
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.combined_fragment, container, false)

        setAdapter()

        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        view.combined_recycler.layoutManager = staggeredGridLayoutManager
        view.combined_recycler.adapter = combinedAdapter
        // animator to null otherwise items keep shifting around
        view.combined_recycler.itemAnimator = null

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCombinedDogsAndCats(
            CombinedDogCat(
                3,
                5
            )
        )
    }

    private fun setAdapter() {
        if (combinedAdapter == null) {
            CombinedAdapter().apply {
                this.setOnRecyclerViewSelected(object :
                    ClickableAdapter.RecyclerViewItemSelectedListener<String> {
                    override fun onItemSelected(item: String) {
                    }
                })

                combinedAdapter = this
            }
        }
    }
}