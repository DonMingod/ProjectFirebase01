package com.cibertec.loginfirebase


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.loginfirebase.adapter.MovimientoAdapter
import com.cibertec.loginfirebase.model.Movimiento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MovimientoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovimientoAdapter
    private val listaMovimientos = mutableListOf<Movimiento>()

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movimiento, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MovimientoAdapter(listaMovimientos)
        recyclerView.adapter = adapter

        cargarMovimientosDesdeFirestore()

        return view
    }

    private fun cargarMovimientosDesdeFirestore() {
        val usuario = auth.currentUser ?: return

        db.collection("movimientos")
            .whereEqualTo("uid", usuario.uid)
            .get()
            .addOnSuccessListener { resultados ->
                val listaTemp = mutableListOf<Movimiento>()
                for (documento in resultados) {
                    val movimiento = documento.toObject(Movimiento::class.java).copy(documentId = documento.id)
                    listaTemp.add(movimiento)
                }
                adapter.actualizarLista(listaTemp)
            }
            .addOnFailureListener {

            }
    }
}