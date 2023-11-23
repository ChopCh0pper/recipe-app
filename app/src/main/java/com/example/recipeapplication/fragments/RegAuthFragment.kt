package com.example.recipeapplication.fragments

import android.content.res.ColorStateList
import android.os.Bundle
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
import com.example.recipeapplication.utils.AUTH
import com.example.recipeapplication.utils.CHILD_ID
import com.example.recipeapplication.utils.CHILD_USERNAME
import com.example.recipeapplication.utils.NODE_USERS
import com.example.recipeapplication.utils.REF_DATABASE_ROOT
import com.example.recipeapplication.utils.CURRENT_UID
import com.example.recipeapplication.utils.initUser
import com.google.firebase.auth.FirebaseUser

class RegAuthFragment : Fragment() {
    private lateinit var binding: FragmentRegAuthBinding
    private val model: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireView())

        model.btClickedProfileFragment.observe(viewLifecycleOwner) {
            bt.text = it

            when (it) {
                getString(R.string.log_in) -> {
                    bt.setOnClickListener {
                        if (etEmail.isEmailValid() && etPassword.text.isNotEmpty()) {
                            checkEmailExists(etEmail.text.toString()) { existence ->
                                if (existence) {
                                    logIn(etEmail.text.toString(), etPassword.text.toString())
                                } else {
                                    existenceOfMailMessage(existence)
                                }
                            }
                        } else {
                            invalidityMessage()
                        }
                    }

                    tvForgotPass.visibility = View.VISIBLE
                    tvForgotPass.setOnClickListener {
                        if (etEmail.isEmailValid()) {
                            checkEmailExists(etEmail.text.toString()) { existence ->
                                if (existence) {
                                    resetPass(etEmail.text.toString())
                                } else {
                                    existenceOfMailMessage(existence)
                                }
                            }
                        }
                    }
                }

                getString(R.string.sign_up) ->
                    bt.setOnClickListener {
                        if (etEmail.isEmailValid() && etPassword.text.isNotEmpty()) {
                            checkEmailExists(etEmail.text.toString()) { existence ->
                                if (!existence) {
                                    createAccount(
                                        etEmail.text.toString(),
                                        etPassword.text.toString()
                                    )
                                } else {
                                    existenceOfMailMessage(existence)
                                }
                            }
                        } else {
                            invalidityMessage()
                        }
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

    private fun EditText.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
    }

    private fun invalidityMessage() = with(binding) {
        etEmail.backgroundTintList = ColorStateList
            .valueOf(ContextCompat.getColor(requireContext(), R.color.errorColor))
        etPassword.backgroundTintList = ColorStateList
            .valueOf(ContextCompat.getColor(requireContext(), R.color.errorColor))
        Toast.makeText(
            requireContext(),
            getString(R.string.toast_msg_validityCheck),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkEmailExists(email: String, callback: (Boolean) -> Unit) {
        AUTH.fetchSignInMethodsForEmail(email).addOnSuccessListener {
            val signInMethods = it.signInMethods!!
            val result = signInMethods.isNotEmpty()
            callback(result)
        }
    }


    private fun existenceOfMailMessage(existence: Boolean) {
        binding.etEmail.backgroundTintList = ColorStateList
            .valueOf(ContextCompat.getColor(requireContext(), R.color.errorColor))
        when (existence) {
            true -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_msg_existence_of_email_true),
                    Toast.LENGTH_SHORT
                ).show()
            }

            false -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_msg_existence_of_email_false),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetPass(email: String) {
        AUTH.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    getString(R.string.toast_msg_sending_letter_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.toast_msg_change_name_field),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_msg_signup_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = AUTH.currentUser
                    model.user.value = user
                    CURRENT_UID = AUTH.currentUser?.uid.toString()
                    val dataMap = mutableMapOf<String, Any>()
                    dataMap[CHILD_ID] = CURRENT_UID
                    dataMap[CHILD_USERNAME] = CURRENT_UID
                    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                        .updateChildren(dataMap)
                    initUser()
                    sendEmailVerification(user!!)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_msg_signup_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    navController.navigate(R.id.action_RegAuthFragment_to_logInFragment)
                }
            }
    }

    private fun logIn(email: String, password: String) {
        AUTH.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = AUTH.currentUser
                    CURRENT_UID = AUTH.currentUser?.uid.toString()
                    initUser()
                    model.user.value = user
                    navController.navigate(R.id.action_RegAuthFragment_to_logInFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_msg_login_failed),
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