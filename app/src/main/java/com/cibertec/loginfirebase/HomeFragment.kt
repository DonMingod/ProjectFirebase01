package com.cibertec.loginfirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var tvWelcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        tvWelcome= view.findViewById(R.id.tvWelcome)

        // Mostrar email del usuario logueado
        val currentUser = auth.currentUser

        if (currentUser != null) {
            tvWelcome.text = "Bienvenido, ${currentUser.email}"
        } else {
            tvWelcome.text = "Bienvenido"
        }

        return view
    }
}