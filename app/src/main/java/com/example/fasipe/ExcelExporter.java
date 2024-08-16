package com.example.fasipe;

import android.os.Environment;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ExcelExporter {

    public static boolean exportToExcel(ArrayList<String> recordNome, ArrayList<String> recordCpf,
                                        ArrayList<String> recordTipoOcorrencia,  ArrayList<String> recordTipoOcorrenciaDescricao, ArrayList<String> recordEstagiario,
                                        ArrayList<String> recordCoordenador) {

        // Cria um novo
        // workbook do Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Cria uma nova planilha
        XSSFSheet sheet = workbook.createSheet("Filtered Data");

        // Cria a primeira linha para cabeçalhos
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Nome", "CPF", "Tipo de Ocorrência", "Descricao da Ocorrência", "Estagiário", "Coordenador"};

        // Preenche os cabeçalhos na primeira linha
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Preenche os dados nas linhas subsequentes
        for (int i = 0; i < recordNome.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(recordNome.get(i));
            row.createCell(1).setCellValue(recordCpf.get(i));
            row.createCell(2).setCellValue(recordTipoOcorrencia.get(i));
            row.createCell(3).setCellValue(recordTipoOcorrenciaDescricao.get(i));
            row.createCell(5).setCellValue(recordEstagiario.get(i));
            row.createCell(6).setCellValue(recordCoordenador.get(i));
        }

        // Salva o arquivo Excel
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FilteredData.xlsx");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
