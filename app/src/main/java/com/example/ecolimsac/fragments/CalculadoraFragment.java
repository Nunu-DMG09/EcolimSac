package com.example.ecolimsac.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ecolimsac.R;

public class CalculadoraFragment extends Fragment {

    private EditText etCantidad;
    private Spinner spUnidad;
    private Button btnConvertir;
    private TextView tvResultado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        etCantidad = view.findViewById(R.id.etCantidad);
        spUnidad = view.findViewById(R.id.spUnidad);
        btnConvertir = view.findViewById(R.id.btnConvertir);
        tvResultado = view.findViewById(R.id.tvResultado);


        String[] unidades = {"Litros", "Mililitros", "Gramos", "Onzas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, unidades);
        spUnidad.setAdapter(adapter);


        btnConvertir.setOnClickListener(v -> convertir());

        return view;
    }

    private void convertir() {
        String cantidadStr = etCantidad.getText().toString();

        if (cantidadStr.isEmpty()) {
            tvResultado.setText("Ingrese un valor");
            return;
        }

        double cantidad = Double.parseDouble(cantidadStr);
        String unidad = spUnidad.getSelectedItem().toString();
        double kg = 0;

        switch (unidad) {
            case "Litros":
                kg = cantidad;
                break;
            case "Mililitros":
                kg = cantidad / 1000;
                break;
            case "Gramos":
                kg = cantidad / 1000;
                break;
            case "Onzas":
                kg = cantidad * 0.0283495;
                break;
        }

        // Redondear a 2 decimales
        kg = Math.round(kg * 100.0) / 100.0;

        tvResultado.setText(kg + " kg");
    }
}

