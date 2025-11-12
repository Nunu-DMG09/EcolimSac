package com.example.ecolimsac.network;

import com.example.ecolimsac.models.Residuo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<Void> login(@Body Object body); // simplificado (usa tu DTO real)

    @GET("catalogos/residuos")
    Call<List<Residuo>> getCatalogoResiduos();

    @POST("recolecciones")
    Call<Void> enviarRecoleccion(@Body Residuo r);

    @POST("recolecciones/sync")
    Call<Void> syncLote(@Body List<Residuo> lote);
}
