package com.example.ecolimsac.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ecolimsac.models.Residuo;
import com.example.ecolimsac.models.Usuario;

@Database(entities = {Residuo.class, Usuario.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ResiduoDao residuoDao();
    public abstract UsuarioDao usuarioDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "residuos_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
