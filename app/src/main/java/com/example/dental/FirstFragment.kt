package com.example.dental

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dental.databinding.FragmentFirstBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        var EMAIL = "tanczos.huba@gmail.com"

        var loginButton = _binding!!.loginButton
        loginButton.setPermissions(EMAIL)
        // If you are using in a fragment, call
        loginButton.setFragment(this)

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);
        val listener: FirstFragmentListener = requireActivity() as MainActivity
        // Callback registration
        loginButton.registerCallback(listener.callbackManager(), object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("TAG", "Success Login")
                // Get User's Info
            }

            override fun onCancel() {
                Toast.makeText(context!!.applicationContext, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context!!.applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(com.example.dental.R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}