package com.cibertec.loginfirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var btnNuevoMovimiento: Button
    private lateinit var btnVerMovimiento: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val currentUser = auth.currentUser
        toolbar.title = "Bienvenido, ${currentUser?.email ?: "Usuario"}"

        btnNuevoMovimiento = view.findViewById(R.id.btnNuevoMovimiento)
        btnVerMovimiento = view.findViewById(R.id.btnVerMovimientos)

        btnNuevoMovimiento.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NuevoMovimientoFragment())
                .addToBackStack(null)
                .commit()
        }

        btnVerMovimiento.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MovimientoFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}