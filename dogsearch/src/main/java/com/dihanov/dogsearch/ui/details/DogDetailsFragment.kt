package com.dihanov.dogsearch.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dihanov.base_ui.BaseFragment
import com.dihanov.dogsearch.R
import kotlinx.android.synthetic.main.details_fragment.view.*

class DogDetailsFragment : BaseFragment() {
    override fun subscribeObservers() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.details_fragment, container, false)

        Glide.with(requireContext()).load(requireArguments().getString("dogImgUrl")).into(view.detailsImage)

        return view
    }
}