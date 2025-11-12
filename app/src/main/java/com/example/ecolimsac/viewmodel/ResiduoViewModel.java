package com.example.ecolimsac.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ecolimsac.db.AppDatabase;
import com.example.ecolimsac.models.Residuo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ResiduoViewModel extends AndroidViewModel {
    public final MutableLiveData<List<Residuo>> residuos = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<Residuo>> residuosFiltrados = new MutableLiveData<>(new ArrayList<>());
    private final AppDatabase db;

    public ResiduoViewModel(@NonNull Application app) {
        super(app);
        db = AppDatabase.getInstance(app);
    }

    public void cargarTodos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Residuo> list = db.residuoDao().getAll();
            list.sort((a, b) -> b.fecha.compareTo(a.fecha));
            residuos.postValue(list);
            residuosFiltrados.postValue(list);
        });
    }

    public void filtrar(String desde, String hasta, String tipo) {
        List<Residuo> base = residuos.getValue();
        if (base == null) base = new ArrayList<>();
        final String fDesde = (desde == null) ? "" : desde;
        final String fHasta = (hasta == null) ? "9999-12-31" : hasta;
        final String fTipo = (tipo == null) ? "" : tipo.toLowerCase(Locale.ROOT);

        List<Residuo> out = new ArrayList<>();
        for (Residuo r : base) {
            boolean okFecha = r.fecha.compareTo(fDesde) >= 0 && r.fecha.compareTo(fHasta) <= 0;
            boolean okTipo = fTipo.isEmpty() || (r.tipo != null && r.tipo.toLowerCase(Locale.ROOT).contains(fTipo));
            if (okFecha && okTipo) out.add(r);
        }
        residuosFiltrados.setValue(out);
    }
}
