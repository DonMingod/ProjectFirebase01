package com.cibertec.loginfirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class RegistroFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var etEmail: EditText
    private lateinit var etNombreUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmarPassword: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var tvIrLogin: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registro, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        etNombreUsuario = view.findViewById(R.id.etNombreUsuario)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmarPassword = view.findViewById(R.id.etConfirmarPassword)
        btnRegistrar = view.findViewById(R.id.btnLogin)
        tvIrLogin = view.findViewById(R.id.tvIrLogin)

        btnRegistrar.setOnClickListener { registrarUsuario() }

        tvIrLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun registrarUsuario() {
        val nombreUsuario = etNombreUsuario.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmarPassword = etConfirmarPassword.text.toString().trim()

        if (nombreUsuario.isEmpty() || email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmarPassword) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nombreUsuario)
                        .build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {
                            Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                            // Aquí podrías navegar al login o a la pantalla principal
                        }
                } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}