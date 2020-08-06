package com.example.project2.ui.main.fragment

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.project2.R
import com.example.project2.databinding.FragmentBottomSheetBinding
import com.example.project2.ui.main.activities.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BottomSheetFragment : BottomSheetDialogFragment(){

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBottomSheetBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater,container, false)
        var view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.item = activity as HomeActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}