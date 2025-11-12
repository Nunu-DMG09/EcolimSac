package com.example.ecolimsac.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolimsac.R;
import com.example.ecolimsac.adapters.ResiduoAdapter;
import com.example.ecolimsac.models.Residuo;
import com.example.ecolimsac.viewmodel.ResiduoViewModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ReportesFragment extends Fragment {

    private ResiduoViewModel vm;
    private ResiduoAdapter adapter;
    private Button btnGenerarPDF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reportes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerResiduos);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResiduoAdapter();
        recycler.setAdapter(adapter);

        vm = new ViewModelProvider(requireActivity()).get(ResiduoViewModel.class);
        vm.residuos.observe(getViewLifecycleOwner(), adapter::submit);
        vm.cargarTodos();

        btnGenerarPDF = view.findViewById(R.id.btnGenerarPDF);
        btnGenerarPDF.setOnClickListener(v -> {
            List<Residuo> listaResiduos = vm.residuos.getValue();
            if (listaResiduos != null && !listaResiduos.isEmpty()) {
                generarPDF(listaResiduos);
            } else {
                Toast.makeText(requireContext(), "No hay datos para generar el PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generarPDF(List<Residuo> listaResiduos) {
        try {
            String pdfPath = requireContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(pdfPath, "reportes_residuos.pdf");
            FileOutputStream outputStream = new FileOutputStream(file);

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();


            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font encabezadoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);


            Paragraph title = new Paragraph("Reporte de Residuos\n\n", tituloFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);


            document.add(new Paragraph("Fecha: " +
                    new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date())));
            document.add(new Paragraph("\n"));


            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(110);
            table.setWidths(new float[]{2f, 3f, 3f, 3f, 3f, 3f, 2f, 3f, 3f, 3f, 3f, 3f});

            // Encabezados con fondo
            String[] headers = {"ID", "Código", "Nombre", "Tipo", "Categoría", "Descripción", "Peso", "Fecha", "Origen", "Valor", "Responsable", "Estado"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, encabezadoFont));
                cell.setBackgroundColor(new BaseColor(0, 51, 147));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(4);
                table.addCell(cell);
            }

            // Datos
            for (Residuo residuo : listaResiduos) {
                table.addCell(createCell(String.valueOf(residuo.id), normalFont));
                table.addCell(createCell(residuo.codigo, normalFont));
                table.addCell(createCell(residuo.nombre, normalFont));
                table.addCell(createCell(residuo.tipo, normalFont));
                table.addCell(createCell(residuo.categoria, normalFont));
                table.addCell(createCell(residuo.descripcion, normalFont));
                table.addCell(createCell(residuo.peso + " kg", normalFont));
                table.addCell(createCell(residuo.fecha, normalFont));
                table.addCell(createCell(residuo.origen, normalFont));
                table.addCell(createCell("S/" + residuo.valorAproximado, normalFont));
                table.addCell(createCell(residuo.responsable, normalFont));
                table.addCell(createCell(residuo.estado, normalFont));
            }

            document.add(table);
            document.close();

            Toast.makeText(requireContext(), "PDF generado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            compartirPDF(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }
    }


    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text != null ? text : "-", font));
        cell.setPadding(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private void compartirPDF(File file) {
        android.net.Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Compartir PDF"));
    }
}
