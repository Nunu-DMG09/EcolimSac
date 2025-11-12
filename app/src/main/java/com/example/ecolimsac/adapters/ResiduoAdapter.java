package com.example.ecolimsac.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolimsac.R;
import com.example.ecolimsac.models.Residuo;

import java.util.ArrayList;
import java.util.List;

public class ResiduoAdapter extends RecyclerView.Adapter<ResiduoAdapter.VH> {
    private final List<Residuo> data = new ArrayList<>();

    public void submit(List<Residuo> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residuo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Residuo r = data.get(pos);
        h.tvTipo.setText("Tipo: " + (r.tipo != null ? r.tipo : "-"));
        h.tvNombre.setText("Nombre: " + (r.nombre));
        h.tvCategoria.setText("Categoría: " + (r.categoria != null ? r.categoria : "-"));
        h.tvDescripcion.setText("Descripción: " + (r.descripcion != null ? r.descripcion : "-"));
        h.tvPeso.setText(String.format("Peso: %.2f kg", r.peso));
        h.tvFecha.setText("Fecha: " + (r.fecha != null ? r.fecha : "-"));
        h.tvCodigo.setText("Código: " + (r.codigo != null ? r.codigo : "-"));
        h.tvOrigen.setText("Origen: " + (r.origen != null ? r.origen : "-"));
        h.tvValor.setText("Valor: S/" + r.valorAproximado);
        h.tvResponsable.setText("Responsable: " + (r.responsable != null ? r.responsable : "-"));
        h.tvUbicacion.setText("Ubicación: " + (r.ubicacion != null ? r.ubicacion : "-"));
        h.tvEstado.setText("Estado: " + (r.estado));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTipo, tvNombre, tvCategoria, tvDescripcion, tvPeso, tvFecha, tvCodigo, tvOrigen, tvValor, tvResponsable, tvUbicacion, tvEstado;

        VH(@NonNull View v) {
            super(v);
            tvTipo = v.findViewById(R.id.tvTipo);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvCategoria = v.findViewById(R.id.tvCategoria);
            tvDescripcion = v.findViewById(R.id.tvDescripcion);
            tvPeso = v.findViewById(R.id.tvPeso);
            tvFecha = v.findViewById(R.id.tvFecha);
            tvCodigo = v.findViewById(R.id.tvCodigo);
            tvOrigen = v.findViewById(R.id.tvOrigen);
            tvValor = v.findViewById(R.id.tvValor);
            tvResponsable = v.findViewById(R.id.tvResponsable);
            tvUbicacion = v.findViewById(R.id.tvUbicacion);
            tvEstado = v.findViewById(R.id.tvEstado);
        }
    }
}
