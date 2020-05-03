package com.dihanov.catsearch.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dihanov.base_ui.BaseFragment
import com.dihanov.catsearch.R
import kotlinx.android.synthetic.main.details_fragment.view.*

class CatDetailsFragment : BaseFragment() {
    override fun subscribeObservers() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.details_fragment, container, false)

        Glide.with(requireContext()).load(requireArguments().getString("catImgUrl")).into(view.detailsImage)

        return view
    }
}