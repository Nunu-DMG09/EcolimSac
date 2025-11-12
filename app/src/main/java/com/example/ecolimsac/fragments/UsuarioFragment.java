package com.example.ecolimsac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecolimsac.R;

public class UsuarioFragment extends Fragment {

    private TextView tvNombreUsuario, tvEmailUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvEmailUsuario = view.findViewById(R.id.tvEmailUsuario);

        // Obtener datos de SharedPreferences
        Context context = getContext();
        String nombre = context.getSharedPreferences("ECOLIM_PREFS", Context.MODE_PRIVATE)
                .getString("usuario", "Usuario");
        String email = context.getSharedPreferences("ECOLIM_PREFS", Context.MODE_PRIVATE)
                .getString("email", "correo@desconocido.com");

        tvNombreUsuario.setText("Nombre: " + nombre);
        tvEmailUsuario.setText("Email: " + email);

        return view;
    }
}
