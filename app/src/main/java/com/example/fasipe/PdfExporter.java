package com.example.fasipe;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfExporter {

    private static String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static boolean exportToPdf(ArrayList<String> recordNome, ArrayList<String> recordCpf,
                                      ArrayList<String> recordTipoOcorrencia, ArrayList<String> recordTipoOcorrenciaDescricao, ArrayList<String> recordEstagiario,
                                      ArrayList<String> recordCoordenador) {

        Document document = new Document();

        try {
            // Local para salvar o arquivo PDF
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FilteredData.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PdfWriter.getInstance(document, fileOutputStream);

            // Abrir o documento
            document.open();

            // Fonte para o cabeçalho
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

            // Cabeçalho
            Paragraph header = new Paragraph("Filtered Data", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Espaçamento após o cabeçalho
            document.add(new Paragraph("\n"));

            // Tabela de dados
            PdfPTable table = new PdfPTable(6); // 6 colunas
            table.setWidthPercentage(100); // Largura da tabela como porcentagem da largura da página
            table.setSpacingBefore(10f); // Espaçamento antes da tabela
            table.setSpacingAfter(10f); // Espaçamento depois da tabela

            // Largura relativa das colunas
            float[] columnWidths = {2f, 2f, 2f, 3f, 2f, 2f};
            table.setWidths(columnWidths);

            // Fonte para as células
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            // Cabeçalhos das colunas
            String[] columnHeaders = {"Nome", "CPF", "Tipo de Ocorrência", "Descrição da Ocorrência", "Estagiário", "Coordenador"};
            for (String columnHeader : columnHeaders) {
                PdfPCell headerCell = new PdfPCell(new Phrase(columnHeader, cellFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);
            }

            // Preencher a tabela com os dados
            for (int i = 0; i < recordNome.size(); i++) {
                table.addCell(new PdfPCell(new Phrase(recordNome.get(i), cellFont)));
                table.addCell(new PdfPCell(new Phrase(formatarCPF(recordCpf.get(i)), cellFont)));
                table.addCell(new PdfPCell(new Phrase(recordTipoOcorrencia.get(i), cellFont)));
                table.addCell(new PdfPCell(new Phrase(recordTipoOcorrenciaDescricao.get(i), cellFont)));
                table.addCell(new PdfPCell(new Phrase(recordEstagiario.get(i), cellFont)));
                table.addCell(new PdfPCell(new Phrase(recordCoordenador.get(i), cellFont)));
            }

            // Adicionar a tabela ao documento
            document.add(table);

            // Fechar o documento
            document.close();
            return true;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
