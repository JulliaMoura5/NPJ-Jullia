package com.example.fasipe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ImageView emptyImageView;
    TextView noDataText;
    EditText searchEditText;
    Button searchButton;

    MyDatabaseHelper myDB;
    ArrayList<String> recordId, recordNome, recordCpf, recordTipoOcorrencia, recordTipoOcorrenciaDescricao, recordEstagiario, recordCoordenador;
    CustomAdapter customAdapter;
    Spinner professorSpinner;

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar dados ao retomar a atividade
        storeDataInArrays();
        customAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.add_button);
        emptyImageView = findViewById(R.id.empty_imageview);
        noDataText = findViewById(R.id.no_data);
        searchEditText = findViewById(R.id.searchEditText);
        professorSpinner = findViewById(R.id.coordenador_spinner);
        searchButton = findViewById(R.id.searchButton);

        // Configurar adaptador para o spinner de professor
        ArrayAdapter<CharSequence> coordenadorAdapter = ArrayAdapter.createFromResource(
                this, R.array.professor_array, android.R.layout.simple_spinner_item);
        coordenadorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professorSpinner.setAdapter(coordenadorAdapter);

        searchButton.setOnClickListener(view -> filter(searchEditText.getText().toString().trim()));

        addButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddActivity.class)));

        myDB = new MyDatabaseHelper(MainActivity.this);
        recordId = new ArrayList<>();
        recordNome = new ArrayList<>();
        recordCpf = new ArrayList<>();
        recordTipoOcorrencia = new ArrayList<>();
        recordTipoOcorrenciaDescricao = new ArrayList<>();
        recordEstagiario = new ArrayList<>();
        recordCoordenador = new ArrayList<>();

        customAdapter = new CustomAdapter(MainActivity.this, this, recordId, recordNome, recordCpf, recordTipoOcorrencia, recordTipoOcorrenciaDescricao,
                recordEstagiario, recordCoordenador);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        checkPermissions();
    }

    private void filter(String searchText) {
        String selectedCoordenador = professorSpinner.getSelectedItem().toString();

        ArrayList<String> filteredRecordId = new ArrayList<>();
        ArrayList<String> filteredRecordNome = new ArrayList<>();
        ArrayList<String> filteredRecordCpf = new ArrayList<>();
        ArrayList<String> filteredRecordTipoOcorrencia = new ArrayList<>();
        ArrayList<String> filteredRecordTipoOcorrenciaDescricao = new ArrayList<>();
        ArrayList<String> filteredRecordEstagiario = new ArrayList<>();
        ArrayList<String> filteredRecordCoordenador = new ArrayList<>();

        for (int i = 0; i < recordNome.size(); i++) {
            if ((recordNome.get(i).toLowerCase().contains(searchText.toLowerCase()) ||
                    recordCpf.get(i).toLowerCase().contains(searchText.toLowerCase())) &&
                    (selectedCoordenador.equals("Selecione um professor") || recordCoordenador.get(i).equals(selectedCoordenador))) {
                filteredRecordId.add(recordId.get(i));
                filteredRecordNome.add(recordNome.get(i));
                filteredRecordCpf.add(recordCpf.get(i));
                filteredRecordTipoOcorrencia.add(recordTipoOcorrencia.get(i));
                filteredRecordTipoOcorrenciaDescricao.add(recordTipoOcorrenciaDescricao.get(i));
                filteredRecordEstagiario.add(recordEstagiario.get(i));
                filteredRecordCoordenador.add(recordCoordenador.get(i));
            }
        }

        customAdapter.filterList(filteredRecordId, filteredRecordNome, filteredRecordCpf, filteredRecordTipoOcorrencia, filteredRecordTipoOcorrenciaDescricao, filteredRecordEstagiario, filteredRecordCoordenador);
        updateViewVisibility();
    }

    private void updateViewVisibility() {
        if (searchEditText.getText().toString().trim().isEmpty() && customAdapter.getItemCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        }
    }

    void storeDataInArrays() {
        recordId.clear();
        recordNome.clear();
        recordCpf.clear();
        recordTipoOcorrencia.clear();
        recordTipoOcorrenciaDescricao.clear();
        recordEstagiario.clear();
        recordCoordenador.clear();

        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                recordId.add(cursor.getString(0));
                recordNome.add(cursor.getString(1));
                recordCpf.add(cursor.getString(2));
                recordTipoOcorrencia.add(cursor.getString(3));
                recordTipoOcorrenciaDescricao.add(cursor.getString(4));
                recordEstagiario.add(cursor.getString(5));
                recordCoordenador.add(cursor.getString(6));
            }
            emptyImageView.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        }
        cursor.close();
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
            myDB.deleteAllData();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_all) {
            confirmDialog();
            return true;
        } else if (id == R.id.export_excel) {
            exportToExcel();
            return true;
        } else if (id == R.id.export_pdf) {
            exportToPdf();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exportToPdf() {
        String searchText = searchEditText.getText().toString().trim();
        String selectedCoordenador = professorSpinner.getSelectedItem().toString();

        ArrayList<String> exportNome = new ArrayList<>();
        ArrayList<String> exportCpf = new ArrayList<>();
        ArrayList<String> exportTipoOcorrencia = new ArrayList<>();
        ArrayList<String> exportTipoOcorrenciaDescricao = new ArrayList<>();
        ArrayList<String> exportEstagiario = new ArrayList<>();
        ArrayList<String> exportCoordenador = new ArrayList<>();

        if (!searchText.isEmpty() || !selectedCoordenador.equals("Selecione um professor")) {
            exportNome = customAdapter.getFilteredRecordNomes();
            exportCpf = customAdapter.getFilteredRecordCpfs();
            exportTipoOcorrencia = customAdapter.getFilteredRecordTipoOcorrencias();
            exportTipoOcorrenciaDescricao = customAdapter.getFilteredRecordTipoOcorrenciaDescricoes();
            exportEstagiario = customAdapter.getFilteredRecordEstagiarios();
            exportCoordenador = customAdapter.getFilteredRecordCoordenadores();
        } else {
            // Todos os registros
            exportNome = recordNome;
            exportCpf = recordCpf;
            exportTipoOcorrencia = recordTipoOcorrencia;
            exportTipoOcorrenciaDescricao = recordTipoOcorrenciaDescricao;
            exportEstagiario = recordEstagiario;
            exportCoordenador = recordCoordenador;
        }

        boolean isExported = PdfExporter.exportToPdf(exportNome, exportCpf, exportTipoOcorrencia, exportTipoOcorrenciaDescricao, exportEstagiario, exportCoordenador);
        if (isExported) {
            Toast.makeText(this, "Dados exportados para PDF", Toast.LENGTH_SHORT).show();
            sharePdf();
        } else {
            Toast.makeText(this, "Falha ao exportar dados para PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePdf() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FilteredData.pdf");
        if (file.exists()) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Salvar PDF usando"));
        } else {
            Toast.makeText(this, "Erro ao localizar o arquivo PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToExcel() {
        String searchText = searchEditText.getText().toString().trim();
        String selectedCoordenador = professorSpinner.getSelectedItem().toString();

        ArrayList<String> exportNome = new ArrayList<>();
        ArrayList<String> exportCpf = new ArrayList<>();
        ArrayList<String> exportTipoOcorrencia = new ArrayList<>();
        ArrayList<String> exportTipoOcorrenciaDescricao = new ArrayList<>();
        ArrayList<String> exportEstagiario = new ArrayList<>();
        ArrayList<String> exportCoordenador = new ArrayList<>();

        if (!searchText.isEmpty() || !selectedCoordenador.equals("Selecione um professor")) {
            exportNome = customAdapter.getFilteredRecordNomes();
            exportCpf = customAdapter.getFilteredRecordCpfs();
            exportTipoOcorrencia = customAdapter.getFilteredRecordTipoOcorrencias();
            exportTipoOcorrenciaDescricao = customAdapter.getFilteredRecordTipoOcorrenciaDescricoes();
            exportEstagiario = customAdapter.getFilteredRecordEstagiarios();
            exportCoordenador = customAdapter.getFilteredRecordCoordenadores();
        } else {
            // Todos os registros
            exportNome = recordNome;
            exportCpf = recordCpf;
            exportTipoOcorrencia = recordTipoOcorrencia;
            exportTipoOcorrenciaDescricao = recordTipoOcorrenciaDescricao;
            exportEstagiario = recordEstagiario;
            exportCoordenador = recordCoordenador;
        }

        boolean isExported = ExcelExporter.exportToExcel(exportNome, exportCpf, exportTipoOcorrencia, exportTipoOcorrenciaDescricao, exportEstagiario, exportCoordenador);
        if (isExported) {
            Toast.makeText(this, "Dados exportados para Excel", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Falha ao exportar dados para Excel", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
