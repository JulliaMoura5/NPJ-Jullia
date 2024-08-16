package com.example.fasipe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText nome_input, cpf_input;
    EditText tipo_ocorrencia_descricao_input;
    Spinner tipo_ocorrencia_spinner, estagiario_spinner, coordenador_spinner;
    Button update_button, delete_button;

    String id, nome, cpf, tipoOcorrencia, tipoOcorrenciaDescricao, estagiario, coordenador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nome_input = findViewById(R.id.nome_input2);
        cpf_input = findViewById(R.id.cpf_input2);
        tipo_ocorrencia_spinner = findViewById(R.id.tipo_ocorrencia_spinner);
        tipo_ocorrencia_descricao_input = findViewById(R.id.tipo_ocorrencia_descricao_input2);
        estagiario_spinner = findViewById(R.id.estagiario_spinner);
        coordenador_spinner = findViewById(R.id.coordenador_spinner);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // Adaptadores para os Spinners
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tipo_ocorrencia_array));
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo_ocorrencia_spinner.setAdapter(tipoAdapter);

        ArrayAdapter<String> estagiarioAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.estagiario_array));
        estagiarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estagiario_spinner.setAdapter(estagiarioAdapter);

        ArrayAdapter<String> coordenadorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.professor_array));
        coordenadorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coordenador_spinner.setAdapter(coordenadorAdapter);

        // Primeiro, chamamos este método
        getAndSetIntentData();

        // Definir título da action bar após o método getAndSetIntentData
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nome);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = nome_input.getText().toString().trim();
                cpf = cpf_input.getText().toString().trim();
                tipoOcorrencia = tipo_ocorrencia_spinner.getSelectedItem().toString();
                tipoOcorrenciaDescricao = tipo_ocorrencia_descricao_input.getText().toString().trim();
                estagiario = estagiario_spinner.getSelectedItem().toString();
                coordenador = coordenador_spinner.getSelectedItem().toString();

                // Validar se os campos não estão vazios
                if (nome.isEmpty() || cpf.isEmpty() || tipo_ocorrencia_spinner.getSelectedItemPosition() == 0 ||
                        estagiario_spinner.getSelectedItemPosition() == 0 || coordenador_spinner.getSelectedItemPosition() == 0 ||
                        tipoOcorrenciaDescricao.isEmpty()) {
                    Toast.makeText(UpdateActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar tamanho do CPF
                if (cpf.length() != 11) {
                    Toast.makeText(UpdateActivity.this, "CPF deve ter exatamente 11 dígitos", Toast.LENGTH_SHORT).show();
                    return;
                }


                // <---------- Verificação de CPF ------------>
//                if (!CPFValidator.isCPF(cpf)) {
//                    Toast.makeText(UpdateActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                Log.d("UpdateActivity", "Updating record: " + id + ", " + nome + ", " + cpf + ", " + tipoOcorrencia + ", " + tipoOcorrenciaDescricao + ", " + estagiario + ", " + coordenador);
                myDB.updateRecord(id, nome, cpf, tipoOcorrencia, tipoOcorrenciaDescricao, estagiario, coordenador);
                Toast.makeText(UpdateActivity.this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("nome") &&
                getIntent().hasExtra("cpf") && getIntent().hasExtra("tipoOcorrencia") &&
                getIntent().hasExtra("tipoOcorrenciaDescricao") &&
                getIntent().hasExtra("estagiario") && getIntent().hasExtra("coordenador")) {
            // Recebendo dados da Intent
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            cpf = getIntent().getStringExtra("cpf");
            tipoOcorrencia = getIntent().getStringExtra("tipoOcorrencia");
            tipoOcorrenciaDescricao = getIntent().getStringExtra("tipoOcorrenciaDescricao");
            estagiario = getIntent().getStringExtra("estagiario");
            coordenador = getIntent().getStringExtra("coordenador");

            // Definindo dados nos campos
            nome_input.setText(nome);
            cpf_input.setText(cpf);
            tipo_ocorrencia_descricao_input.setText(tipoOcorrenciaDescricao);
            // Selecionando os itens apropriados nos Spinners
            selectSpinnerItemByValue(tipo_ocorrencia_spinner, tipoOcorrencia);
            selectSpinnerItemByValue(estagiario_spinner, estagiario);
            selectSpinnerItemByValue(coordenador_spinner, coordenador);

            Log.d("UpdateActivity", "Received data: " + nome + " " + cpf + " " + tipoOcorrencia + " " + tipoOcorrenciaDescricao + " " + estagiario + " " + coordenador);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método auxiliar para selecionar item no Spinner por valor
    private void selectSpinnerItemByValue(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            for (int position = 0; position < adapter.getCount(); position++) {
                if (adapter.getItem(position).equals(value)) {
                    spinner.setSelection(position);
                    return;
                }
            }
        }
    }

    // Diálogo de confirmação para exclusão
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + nome + " ?");
        builder.setMessage("Tem certeza de que deseja DELETAR esse registro de " + nome + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Nada acontece se o usuário selecionar "No"
            }
        });
        builder.create().show();
    }

    private boolean isCPFValido(String cpf) {
        return cpf.matches("\\d{11}");
    }
}
