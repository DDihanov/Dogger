package com.dihanov.base_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {
    abstract fun subscribeObservers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        subscribeObservers()
        return inflateView(inflater, container, savedInstanceState)
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
}