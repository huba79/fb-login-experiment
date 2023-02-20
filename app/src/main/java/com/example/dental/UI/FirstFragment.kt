package com.example.dental.UI

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dental.R
import com.example.dental.databinding.FragmentFirstBinding
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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
        try {
            val packageName = requireContext().packageName
            val info = requireContext().packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash:", "Package Not found!")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", "Hash algorythm not found!")
        }

        val EMAIL = "email"

        val loginButton = _binding!!.loginButton
        loginButton.permissions= listOf(EMAIL)
        // If you are using in a fragment, call
        loginButton.setFragment(this)

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);
        val listener: FirstFragmentListener = requireActivity() as MainActivity
        // Callback registration
        loginButton.registerCallback(listener.callbackManager(), object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FBLoginResult", "Success Login")

                Log.i("FBLoginResult", "authenticationToken: ${result.authenticationToken?.token.toString()}")
                Log.i("FBLoginResult", "accessToken: ${result.accessToken.token.toString()}")
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
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}