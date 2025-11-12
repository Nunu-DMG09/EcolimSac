package com.example.ecolimsac.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecolimsac.R;
import com.example.ecolimsac.db.AppDatabase;
import com.example.ecolimsac.models.Residuo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroFragment extends Fragment {

    private Spinner spTipo, spCategoria;
    private EditText etNombre, etPeso, etDescripcion, etOrigen, etValor, etResponsable, etUbicacion;
    private Button btnGuardar, btnEscanear;
    private TextView tvCodigo;
    private String codigoQR = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);


        spTipo = view.findViewById(R.id.spTipo);
        spCategoria = view.findViewById(R.id.spCategoria);


        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(
                getContext(),
                R.array.tipos_residuos,
                android.R.layout.simple_spinner_item
        );
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categorias_residuos,
                android.R.layout.simple_spinner_item
        );
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategoria);


        etNombre = view.findViewById(R.id.etNombre);
        etPeso = view.findViewById(R.id.etPeso);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etOrigen = view.findViewById(R.id.etOrigen);
        etValor = view.findViewById(R.id.etValor);
        etResponsable = view.findViewById(R.id.etResponsable);
        etUbicacion = view.findViewById(R.id.etUbicacion);

        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnEscanear = view.findViewById(R.id.btnEscanear);
        tvCodigo = view.findViewById(R.id.tvCodigo);

        btnGuardar.setOnClickListener(v -> guardarResiduo());
        btnEscanear.setOnClickListener(v -> iniciarEscaneo());



        return view;
    }

    private void iniciarEscaneo() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanee el código del residuo");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            codigoQR = result.getContents();
            tvCodigo.setText("Código: " + codigoQR);
        } else {
            Toast.makeText(getContext(), "No se detectó código", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarResiduo() {

        String tipo = spTipo.getSelectedItem().toString();
        String nombre = etNombre.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem().toString();
        String descripcion = etDescripcion.getText().toString().trim();
        String origen = etOrigen.getText().toString().trim();
        String valorStr = etValor.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();

        if (tipo.isEmpty() || pesoStr.isEmpty()) {
            Toast.makeText(getContext(), "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor = 0;
        if (!valorStr.isEmpty()) {
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Valor inválido, se guardará como 0", Toast.LENGTH_SHORT).show();
            }
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        String responsable = getActivity()
                .getSharedPreferences("ECOLIM_PREFS", Context.MODE_PRIVATE)
                .getString("usuario", "Desconocido");


        Residuo residuo = new Residuo(
                nombre,
                tipo,
                categoria,
                peso,
                fecha,
                codigoQR.isEmpty() ? "-" : codigoQR,
                descripcion,
                origen,
                valor,
                "Procesado",
                responsable,
                ubicacion
        );


        btnGuardar.setEnabled(false);

        new Thread(() -> {
            AppDatabase.getInstance(getContext()).residuoDao().insert(residuo);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    btnGuardar.setEnabled(true);
                });
            }
        }).start();
    }

    private void limpiarCampos() {
        spTipo.setSelection(0);
        etNombre.setText("");
        etPeso.setText("");
        spCategoria.setSelection(0);
        etDescripcion.setText("");
        etOrigen.setText("");
        etValor.setText("");
        etResponsable.setText("");
        etUbicacion.setText("");
        tvCodigo.setText("Código: -");
        codigoQR = "";
    }
}
