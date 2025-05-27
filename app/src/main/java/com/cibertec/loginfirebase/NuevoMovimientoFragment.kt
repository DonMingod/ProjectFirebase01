package com.cibertec.loginfirebase

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NuevoMovimientoFragment : Fragment() {

    private lateinit var etMonto: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var rgTipo: RadioGroup
    private lateinit var rbIngreso: RadioButton
    private lateinit var rbGasto: RadioButton
    private lateinit var btnGuardarMovimiento: Button
    private lateinit var btnSeleccionarCategoria: Button
    private lateinit var categoriaSeleccionada: TextView
    private lateinit var tvSaldo: TextView

    private var categoria = "Ninguna"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_nuevo_movimiento, container, false)

        // id
        etMonto = view.findViewById(R.id.etMonto)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        rgTipo = view.findViewById(R.id.rgTipo)
        rbIngreso = view.findViewById(R.id.rbIngreso)
        rbGasto = view.findViewById(R.id.rbGasto)
        btnGuardarMovimiento = view.findViewById(R.id.btnGuardarMovimiento)
        btnSeleccionarCategoria = view.findViewById(R.id.btnSeleccionarCategoria)
        tvSaldo = view.findViewById(R.id.tvSaldoActual)


        categoriaSeleccionada = TextView(requireContext()).apply {
            text = "Categoría: Ninguna"
            textSize = 16f
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            setPadding(0, 16, 0, 0)
        }
        val layout = view.findViewById<ConstraintLayout>(R.id.constraintLayoutRoot)
        layout.addView(categoriaSeleccionada)

        btnSeleccionarCategoria.setOnClickListener {
            mostrarBottomSheetCategorias()
        }

        btnGuardarMovimiento.setOnClickListener {
            guardarMovimiento()
        }

        obtenerSaldo()

        return view
    }

    private fun mostrarBottomSheetCategorias() {
        val dialog = BottomSheetDialog(requireContext())

        val gridLayout = GridLayout(requireContext()).apply {
            columnCount = 3
            setPadding(32, 32, 32, 32)
        }

        val categorias = listOf(
            "Compras" to android.R.drawable.ic_menu_compass,
            "Comida" to android.R.drawable.ic_menu_crop,
            "Transporte" to android.R.drawable.ic_menu_directions,
            "Hogar" to android.R.drawable.ic_menu_myplaces,
            "Trabajo" to android.R.drawable.ic_menu_manage,
            "Ocio" to android.R.drawable.ic_menu_gallery
        )

        categorias.forEach { (nombre, icono) ->
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)

                val imageView = ImageView(requireContext()).apply {
                    setImageResource(icono)
                    layoutParams = ViewGroup.LayoutParams(100, 100)
                }

                val textView = TextView(requireContext()).apply {
                    text = nombre
                    gravity = Gravity.CENTER
                }

                addView(imageView)
                addView(textView)

                setOnClickListener {
                    Toast.makeText(requireContext(), "Seleccionado: $nombre", Toast.LENGTH_SHORT).show()
                    categoria = nombre
                    categoriaSeleccionada.text = "Categoría: $nombre"
                    dialog.dismiss()
                }
            }

            gridLayout.addView(itemLayout)
        }

        dialog.setContentView(gridLayout)
        dialog.show()
    }

    private fun guardarMovimiento() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val monto = etMonto.text.toString().toDoubleOrNull()
        val descripcion = etDescripcion.text.toString().trim()
        val tipo = when {
            rbIngreso.isChecked -> "ingreso"
            rbGasto.isChecked -> "gasto"
            else -> ""
        }

        if (monto == null || tipo.isEmpty() || categoria == "Ninguna" || descripcion.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val movimiento = hashMapOf(
            "uid" to uid,
            "monto" to monto,
            "descripcion" to descripcion,
            "tipo" to tipo,
            "categoria" to categoria,
            "fecha" to Date()
        )

        db.collection("movimientos")
            .add(movimiento)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Movimiento guardado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                obtenerSaldo()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limpiarCampos() {
        etMonto.text.clear()
        etDescripcion.text.clear()
        rgTipo.clearCheck()
        categoria = "Ninguna"
        categoriaSeleccionada.text = "Categoría: Ninguna"
    }

    private fun obtenerSaldo() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("movimientos")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                var saldo = 0.0
                for (doc in result) {
                    val monto = doc.getDouble("monto") ?: 0.0
                    val tipo = doc.getString("tipo")
                    if (tipo == "ingreso") saldo += monto else saldo -= monto
                }
                tvSaldo.text = "Saldo actual: S/ %.2f".format(saldo)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al obtener saldo", Toast.LENGTH_SHORT).show()
            }
    }
}
