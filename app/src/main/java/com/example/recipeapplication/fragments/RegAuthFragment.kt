package com.example.recipeapplication.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.recipeapplication.MainViewModel
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.FragmentRegAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val TAG = "TAG"
class RegAuthFragment : Fragment() {
    private lateinit var binding: FragmentRegAuthBinding
    private lateinit var auth: FirebaseAuth
    private val model: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireView())

        model.btClickedProfileFragment.observe(viewLifecycleOwner) {
            bt.text = it

            when(it) {
                getString(R.string.log_in) ->
                    bt.setOnClickListener {
                        if (validityCheck(etEmail, etPassword))
                            logIn(etEmail.text.toString(), etPassword.text.toString())
                    }

                getString(R.string.sign_up) ->
                    bt.setOnClickListener {
                        if (validityCheck(etEmail, etPassword))
                            createAccount(etEmail.text.toString(), etPassword.text.toString())
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    internal fun EditText.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
    }

    private fun validityCheck(email: EditText, password: EditText): Boolean {
        if (!email.isEmailValid() || password.text.isEmpty()) {
            email.backgroundTintList = ColorStateList
                .valueOf(ContextCompat.getColor(requireContext(), R.color.errorColor))
            password.backgroundTintList = ColorStateList
                .valueOf(ContextCompat.getColor(requireContext(), R.color.errorColor))
            Toast.makeText(
                requireContext(),
                "Invalid email or password.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun createAccount(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        requireContext(),
                        "Account created successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    model.user.value = user
                    navController.navigate(R.id.action_RegAuthFragment_to_logInFragment)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    model.user.value = user
                    navController.navigate(R.id.action_RegAuthFragment_to_logInFragment)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", it.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    companion object {
        @JvmStatic
        fun newInstance() = RegAuthFragment()
    }
}