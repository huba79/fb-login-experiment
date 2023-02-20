package com.example.dental.ui

import android.accounts.Account
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dental.R
import com.example.dental.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {
    private val RC_SIGN_IN =0
    private var _binding: FragmentLoginBinding? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val isGoogleLoginEnabled = true
    private lateinit var accountNormalized:Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        logDevKeyHash()

        if(isGoogleLoginEnabled){
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            val googleLoginButton = binding.googleLoginButtonId

            googleLoginButton.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
        return binding.root
    }

    private fun logDevKeyHash() {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
        //to convert to android account
        if(googleAccount!=null) {
            val androidAccount = googleAccount.account
            //TODO : create mainfragmentlistener that can accept account data for the main fragment to use  instead of updateUI(androidAccount)
            //then navigate to the main fragment with the account info
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
    override fun onStart() {
        super.onStart()
        val googleAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (googleAccount !=null){
            accountNormalized = googleAccount.account!!
            //TODO : create mainfragmentlistener that can accept account data for the main fragment to use  instead of updateUI(androidAccount)
            //then navigate to the main fragment with the account info
        }
    }

    //private fun updateUI(account: Account?) { //TODO implement general account processing logic }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}