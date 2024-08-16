package com.example.fasipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {

    EditText nome_input, cpf_input;
    TextInputEditText tipo_ocorrencia_descricao;
    Spinner tipo_ocorrencia_input, estagiario_input, coordenador_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nome_input = findViewById(R.id.nome_input);
        cpf_input = findViewById(R.id.cpf_input);
        tipo_ocorrencia_input = findViewById(R.id.tipo_ocorrencia_spinner);
        tipo_ocorrencia_descricao = findViewById(R.id.tipo_ocorrencia_descricao_input);
        estagiario_input = findViewById(R.id.estagiario_spinner);
        coordenador_input = findViewById(R.id.coordenador_spinner);
        add_button = findViewById(R.id.add_button);

        // Configurar adapters para os Spinners
        ArrayAdapter<CharSequence> tipoOcorrenciaAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_ocorrencia_array, android.R.layout.simple_spinner_item);
        tipoOcorrenciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo_ocorrencia_input.setAdapter(tipoOcorrenciaAdapter);
        tipo_ocorrencia_input.setSelection(0, false);

        ArrayAdapter<CharSequence> estagiarioAdapter = ArrayAdapter.createFromResource(this,
                R.array.estagiario_array, android.R.layout.simple_spinner_item);
        estagiarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estagiario_input.setAdapter(estagiarioAdapter);
        estagiario_input.setSelection(0, false);

        ArrayAdapter<CharSequence> coordenadorAdapter = ArrayAdapter.createFromResource(this,
                R.array.professor_array, android.R.layout.simple_spinner_item);
        coordenadorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coordenador_input.setAdapter(coordenadorAdapter);
        coordenador_input.setSelection(0, false);

        // Definir prompt nos Spinners
        tipo_ocorrencia_input.setPrompt(getString(R.string.tipo_de_ocorr_ncia));
        estagiario_input.setPrompt(getString(R.string.estagi_rio));
        coordenador_input.setPrompt(getString(R.string.Professosr));

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nome_input.getText().toString().trim();
                String cpf = cpf_input.getText().toString().trim();
                String tipoocorrencia = tipo_ocorrencia_input.getSelectedItem().toString().trim();
                String tipoocorrenciaDescricao = tipo_ocorrencia_descricao.getText().toString().trim();
                String estagiario = estagiario_input.getSelectedItem().toString().trim();
                String coordenador = coordenador_input.getSelectedItem().toString().trim();

                // Validar se os campos não estão vazios
                if (nome.isEmpty() || cpf.isEmpty() || tipoocorrencia.equals("Selecione um tipo de ocorrência") ||
                        estagiario.equals("Selecione um estagiário") || coordenador.equals("Selecione um professor") ||
                        tipoocorrenciaDescricao.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar tamanho do CPF
                if (cpf.length() != 11) {
                    Toast.makeText(AddActivity.this, "CPF deve ter exatamente 11 dígitos", Toast.LENGTH_SHORT).show();
                    return;
                }




//                // <-------------------VALIDAÇÃO DE CPF-------------->


//                if (!CPFValidator.isCPF(cpf)) {
//                    Toast.makeText(AddActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
//                    return;
//                }



                

                // Se todos os campos estiverem preenchidos corretamente
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addRecord(nome, cpf, tipoocorrencia, tipoocorrenciaDescricao, estagiario, coordenador);
                Toast.makeText(AddActivity.this, "Registro adicionado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
