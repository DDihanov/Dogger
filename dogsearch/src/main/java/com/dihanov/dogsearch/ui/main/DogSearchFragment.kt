package com.dihanov.dogsearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dihanov.SearchView
import com.dihanov.base_domain.Status
import com.dihanov.base_ui.BaseFragment
import com.dihanov.base_ui.ClickableAdapter
import com.dihanov.dogsearch.R
import com.dihanov.dogsearch.ui.main.adapter.SearchAdapter
import com.dihanov.dogsearch.ui.uimodel.GetDog
import com.dihanov.util.KeyboardManager
import kotlinx.android.synthetic.main.dog_search_fragment.*
import kotlinx.android.synthetic.main.dog_search_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.inject

class DogSearchFragment : BaseFragment(), SearchView.SearchQuerySubmitListener {
    companion object {
        const val LIMIT = 10
    }
    private val viewModel: DogSearchViewModel by viewModel()
    private val keyboardUtils: KeyboardManager by inject()

    private var adapter: SearchAdapter? = null

    override fun subscribeObservers() {
        viewModel.dogImages.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                val status = it.status
                when (status) {
                    Status.ERROR -> {
                        hideLoader()
                    }
                    Status.LOADING -> {
                        showLoader()
                    }
                    Status.SUCCESS -> {
                        hideLoader()
                        hideKeyboard()
                        adapter?.clear()

                        val list = it.data!!
                        adapter?.add(list)
                    }
                }
            }
        })
    }

    private fun hideKeyboard() {
        keyboardUtils.hideKeyboard(view)
    }

    private fun hideLoader() {
        search_loading.visibility = View.GONE
    }

    private fun showLoader() {
        search_loading.visibility = View.VISIBLE
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dog_search_fragment, container, false)

        setAdapter()

        view.search_view.setSearchQuerySubmitListener(this)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        view.search_recycler.layoutManager = staggeredGridLayoutManager
        view.search_recycler.adapter = adapter
        // animator to null otherwise items keep shifting around
        view.search_recycler.itemAnimator = null

        return view
    }

    private fun setAdapter() {
        if (adapter == null) {
            SearchAdapter().apply {
                this.setOnRecyclerViewSelected(object :
                    ClickableAdapter.RecyclerViewItemSelectedListener<String> {
                    override fun onItemSelected(item: String) {
                        findNavController().navigate(
                            R.id.dogDetailsFragment,
                            Bundle().apply { putString("dogImgUrl", item) })
                    }
                })

                adapter = this
            }
        }
    }

    override fun onQuerySubmitted(query: String) {
        if (query.isEmpty()) {
            adapter?.clear()
            return
        }
        viewModel.searchForDogs(GetDog(query, LIMIT))
    }
}
