package com.example.recipeapplication.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.recipeapplication.MainViewModel
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.FragmentLogInBinding
import com.example.recipeapplication.utils.AUTH
import com.example.recipeapplication.utils.CHILD_USERNAME
import com.example.recipeapplication.utils.NODE_USERS
import com.example.recipeapplication.utils.REF_DATABASE_ROOT
import com.example.recipeapplication.utils.UID
import com.example.recipeapplication.utils.USER
import com.example.recipeapplication.utils.initUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val model: MainViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var awaitForEmailConfirmCoroutine: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()

        model.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                if (user.isEmailVerified) {
                    updateUI()
                } else {
                    awaitForEmailConfirmCoroutine?.cancel()
                    awaitForEmailConfirmCoroutine = lifecycleScope.launch(Dispatchers.IO) {
                        while (true) {
                            val user: FirebaseUser = AUTH.currentUser ?: return@launch
                            user.reload().await()
                            if (user.isEmailVerified) {
                                withContext(Dispatchers.Main) {
                                    model.user.value = user
                                }
                                return@launch
                            }
                            delay(5000)
                        }
                    }
                }
            } else {
                navController.navigate(R.id.action_logInFragment_to_profileFragment)
            }
        }
    }

    private fun initFields() = with(binding) {
        navController = Navigation.findNavController(requireView())
        btExit.setOnClickListener { signOut() }
        btResend.setOnClickListener { sendEmailVerification(AUTH.currentUser) }
        btFavorites.setOnClickListener { navController.navigate(R.id.action_logInFragment_to_favoritesFragment) }
        btMyRecipes.setOnClickListener { navController.navigate(R.id.action_logInFragment_to_myRecipesFragment) }
        btSettings.setOnClickListener { navController.navigate(R.id.action_logInFragment_to_settingsFragment) }
        btAboutUs.setOnClickListener { navController.navigate(R.id.action_logInFragment_to_aboutUsFragment) }
        ibtChangeName.setOnClickListener { updateFieldName(tvName.visibility) }
        ibtDone.setOnClickListener { changeUserName() }
    }

    private fun changeUserName() {
        val name = binding.etChangeName.text.toString()
        if (name.isNotEmpty()) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                .child(CHILD_USERNAME).setValue(name)
            USER.username = name
            updateFieldName(binding.tvName.visibility)
        } else {
            Toast.makeText(
                context,
                getString(R.string.toast_msg_change_name_field),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateFieldName(visibility: Int) = with(binding) {
        when (visibility) {
            View.VISIBLE -> {
                tvName.visibility = View.GONE
                ibtChangeName.visibility = View.GONE
                etChangeName.visibility = View.VISIBLE
                ibtDone.visibility = View.VISIBLE

                etChangeName.setText(tvName.text)
            }

            View.GONE -> {
                tvName.visibility = View.VISIBLE
                ibtChangeName.visibility = View.VISIBLE
                etChangeName.visibility = View.GONE
                ibtDone.visibility = View.GONE

                tvName.text = USER.username
            }
        }
    }

    private fun signOut() {
        AUTH.signOut()
        model.user.value = AUTH.currentUser
        UID = AUTH.currentUser?.uid.toString()
        initUser()
        navController.navigate(R.id.profileFragment)
    }

    private fun updateUI() = with(binding) {
        clFirst.visibility = View.GONE
        clSecond.visibility = View.VISIBLE

        tvName.text = USER.username
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_msg_sending_letter_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_msg_sending_letter_failed),
                        Toast.LENGTH_SHORT
                    )
                }
            }
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