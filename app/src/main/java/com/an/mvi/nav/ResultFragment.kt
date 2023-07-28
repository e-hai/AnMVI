package com.an.mvi.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.an.mvi.R

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            clickBack()
        }
        view.findViewById<Button>(R.id.bt_to_a).setOnClickListener {
            clickToAFragment()
        }
    }

    private fun clickToAFragment() {
        val directions = ResultFragmentDirections.actionResultFragmentToMainFragment(true)
        findNavController().navigate(directions)
    }

    private fun clickBack() {
        findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
    }
}