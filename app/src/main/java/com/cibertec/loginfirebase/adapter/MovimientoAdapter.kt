package com.cibertec.loginfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.loginfirebase.R
import com.cibertec.loginfirebase.model.Movimiento

class MovimientoAdapter(
    private var listaMovimientos: List<Movimiento>
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    inner class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvMonto: TextView = itemView.findViewById(R.id.tvMonto)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = listaMovimientos[position]
        holder.tvCategoria.text = movimiento.categoria
        holder.tvDescripcion.text = movimiento.descripcion
        holder.tvMonto.text = movimiento.monto.toString()
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        val fechaFormateada = movimiento.fecha?.let { formato.format(it) } ?: "Sin fecha"

        holder.tvFecha.text = fechaFormateada
    }

    override fun getItemCount(): Int = listaMovimientos.size

    fun actualizarLista(nuevaLista: List<Movimiento>) {
        listaMovimientos = nuevaLista
        notifyDataSetChanged()
    }
}
