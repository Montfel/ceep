package com.montfel.ceep.ui.activity;

import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.montfel.ceep.R;
import com.montfel.ceep.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {

    private EditText titulo, descricao;
    private int posicaoRecebida = POSICAO_INVALIDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle("Insere nota");
        inicializaComponentes();

        Intent dadosRecebidos = getIntent();
        if (dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle("Altera nota");
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencheCampos(notaRecebida);
        }
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaComponentes() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isMenuSalvaNota(item)) {
            Nota notaCriada = new Nota(titulo.getText().toString(), descricao.getText().toString());
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private boolean isMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}