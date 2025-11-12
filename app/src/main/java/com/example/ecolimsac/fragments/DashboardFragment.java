package com.example.ecolimsac.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecolimsac.R;
import com.example.ecolimsac.models.Residuo;
import com.example.ecolimsac.viewmodel.ResiduoViewModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class DashboardFragment extends Fragment {

    private ResiduoViewModel vm;
    private TextView tvTotalReg, tvTotalKg, tvUltimo, tvMasValioso, tvPorcentajeReciclados, tvUbicacionFrecuente;
    private TextView tvCategorias, tvMasPesado, tvValorTotal, tvUltimaUbicacion;
    private TextView tvTopCategorias, tvResiduoFrecuente, tvPromedioPeso, tvValorPromedio;
    private Button btnGenerarPDF;

    private List<Residuo> listaResiduos = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // IDs existentes
        tvTotalReg = v.findViewById(R.id.tvTotalReg);
        tvTotalKg = v.findViewById(R.id.tvTotalKg);
        tvUltimo = v.findViewById(R.id.tvUltimo);
        tvMasValioso = v.findViewById(R.id.tvMasValioso);
        tvPorcentajeReciclados = v.findViewById(R.id.tvPorcentajeReciclados);
        tvUbicacionFrecuente = v.findViewById(R.id.tvUbicacionFrecuente);

        // Nuevos TextViews (debes agregarlos en XML)
        tvCategorias = v.findViewById(R.id.tvCategorias);
        tvMasPesado = v.findViewById(R.id.tvMasPesado);
        tvValorTotal = v.findViewById(R.id.tvValorTotal);
        tvUltimaUbicacion = v.findViewById(R.id.tvUltimaUbicacion);
        tvTopCategorias = v.findViewById(R.id.tvTopCategorias);
        tvResiduoFrecuente = v.findViewById(R.id.tvResiduoFrecuente);
        tvPromedioPeso = v.findViewById(R.id.tvPromedioPeso);
        tvValorPromedio = v.findViewById(R.id.tvValorPromedio);

        btnGenerarPDF = v.findViewById(R.id.btnGenerarPDF);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(ResiduoViewModel.class);

        vm.residuos.observe(getViewLifecycleOwner(), list -> {
            listaResiduos = list;
            int total = list.size();
            double kg = 0;
            String ultimo = "-";

            Residuo masValioso = null;
            Residuo masPesado = null;
            int reciclados = 0;
            double valorTotal = 0;

            Map<String, Integer> ubicaciones = new HashMap<>();
            Map<String, Integer> categoriasContadas = new HashMap<>();
            Map<String, Integer> residuosContados = new HashMap<>();
            Set<String> categoriasUnicas = new HashSet<>();

            if (!list.isEmpty()) {
                list.sort((r1, r2) -> r2.fecha.compareTo(r1.fecha)); // Orden descendente
                ultimo = list.get(0).fecha;
            }

            for (Residuo r : list) {
                kg += r.peso;
                valorTotal += r.valorAproximado;

                if (masValioso == null || r.valorAproximado > masValioso.valorAproximado) {
                    masValioso = r;
                }

                if (masPesado == null || r.peso > masPesado.peso) {
                    masPesado = r;
                }

                if (r.tipo != null && r.tipo.toLowerCase(Locale.ROOT).contains("reciclable")) {
                    reciclados++;
                }

                if (r.categoria != null && !r.categoria.isEmpty()) {
                    categoriasUnicas.add(r.categoria);
                    categoriasContadas.put(r.categoria, categoriasContadas.getOrDefault(r.categoria, 0) + 1);
                }

                if (r.nombre != null && !r.nombre.isEmpty()) {
                    residuosContados.put(r.nombre, residuosContados.getOrDefault(r.nombre, 0) + 1);
                }

                if (r.origen != null && !r.origen.isEmpty()) {
                    ubicaciones.put(r.origen, ubicaciones.getOrDefault(r.origen, 0) + 1);
                }
            }

            String ultimaUbicacion = list.isEmpty() || list.get(0).ubicacion == null ? "-" : list.get(0).ubicacion;


            String ubicacionFrecuente = getMaxKey(ubicaciones);


            String topCategorias = getTopItems(categoriasContadas, 3);


            String residuoFrecuente = getMaxKey(residuosContados);


            double porcentaje = total > 0 ? (reciclados * 100.0 / total) : 0;


            double promedioPeso = total > 0 ? (kg / total) : 0;


            double promedioValor = total > 0 ? (valorTotal / total) : 0;

            tvTotalReg.setText(String.valueOf(total));
            tvTotalKg.setText(String.format(Locale.getDefault(), "%.2f kg", kg));
            tvUltimo.setText(ultimo);
            tvMasValioso.setText(masValioso != null ? masValioso.tipo + " (S/ " + masValioso.valorAproximado + ")" : "-");
            tvPorcentajeReciclados.setText(total > 0 ? String.format(Locale.getDefault(), "%.1f%%", porcentaje) : "Sin datos");
            tvUbicacionFrecuente.setText(ubicacionFrecuente);
            tvCategorias.setText(total > 0 ? categoriasUnicas.size() + " categorías" : "Sin datos");
            tvMasPesado.setText(masPesado != null ? masPesado.nombre + " (" + masPesado.peso + " kg)" : "-");
            tvValorTotal.setText("S/ " + valorTotal);
            tvUltimaUbicacion.setText(ultimaUbicacion);
            tvTopCategorias.setText(topCategorias);
            tvResiduoFrecuente.setText(residuoFrecuente != null ? residuoFrecuente : "-");
            tvPromedioPeso.setText(String.format(Locale.getDefault(), "%.2f kg", promedioPeso));
            tvValorPromedio.setText(String.format(Locale.getDefault(), "S/ %.2f", promedioValor));
        });

        vm.cargarTodos();

        btnGenerarPDF.setOnClickListener(v -> generarPDF());
    }

    private String getMaxKey(Map<String, Integer> map) {
        String maxKey = "-";
        int maxVal = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (e.getValue() > maxVal) {
                maxVal = e.getValue();
                maxKey = e.getKey();
            }
        }
        return maxKey;
    }

    private String getTopItems(Map<String, Integer> map, int top) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(top, list.size()); i++) {
            sb.append(list.get(i).getKey()).append(" (").append(list.get(i).getValue()).append(")\n");
        }
        return sb.toString().isEmpty() ? "-" : sb.toString();
    }

    private void generarPDF() {
        try {

            String pdfPath = requireContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(pdfPath, "dashboard_residuos.pdf");
            FileOutputStream outputStream = new FileOutputStream(file);

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            Paragraph title = new Paragraph("Reporte Dashboard\n\n", tituloFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date());
            document.add(new Paragraph("Fecha de generación: " + fecha, normalFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Resumen general:", subtituloFont));
            document.add(new Paragraph("Total registros: " + tvTotalReg.getText(), normalFont));
            document.add(new Paragraph("Total kg: " + tvTotalKg.getText(), normalFont));
            document.add(new Paragraph("Último registro: " + tvUltimo.getText(), normalFont));
            document.add(new Paragraph("Más valioso: " + tvMasValioso.getText(), normalFont));
            document.add(new Paragraph("% Reciclados: " + tvPorcentajeReciclados.getText(), normalFont));
            document.add(new Paragraph("Ubicación frecuente: " + tvUbicacionFrecuente.getText(), normalFont));
            document.add(new Paragraph("Categorías únicas: " + tvCategorias.getText(), normalFont));
            document.add(new Paragraph("Más pesado: " + tvMasPesado.getText(), normalFont));
            document.add(new Paragraph("Valor total: " + tvValorTotal.getText(), normalFont));
            document.add(new Paragraph("Última ubicación: " + tvUltimaUbicacion.getText(), normalFont));
            document.add(new Paragraph("Top categorías:\n" + tvTopCategorias.getText(), normalFont));
            document.add(new Paragraph("Residuo más frecuente: " + tvResiduoFrecuente.getText(), normalFont));
            document.add(new Paragraph("Promedio peso: " + tvPromedioPeso.getText(), normalFont));
            document.add(new Paragraph("Valor promedio: " + tvValorPromedio.getText(), normalFont));

            document.close();

            Toast.makeText(requireContext(), "PDF generado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            compartirPDF(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartirPDF(File file) {
        Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Compartir PDF"));
    }

}
