package com.example.recipeapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.recipeapplication.MainViewModel
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val model: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = LogInFragment()
    }
}