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


class LoginFragment : Fragment() {

    //ciclo de vida inicializado yaaaaaa

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //este es el actioon bar
        (activity as AppCompatActivity).supportActionBar?.hide()

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //base de datos
        auth = FirebaseAuth.getInstance()

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        //navegacion
        val tvIrRegistro = view.findViewById<TextView>(R.id.tvIrRegistro)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Navegar a HomeFragment después del login exitoso
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, HomeFragment())
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        tvIrRegistro.setOnClickListener {
            // Navegar a RegistroFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistroFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}