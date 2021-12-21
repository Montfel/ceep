package com.montfel.ceep.ui.activity;

import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.montfel.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.montfel.ceep.R;
import com.montfel.ceep.dao.NotaDAO;
import com.montfel.ceep.model.Nota;
import com.montfel.ceep.ui.recycler.adapter.ListaNotasAdapter;
import com.montfel.ceep.ui.recycler.helper.callback.NotaItemTouchHelperCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView insereNota;
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(view ->
                startActivityForResult(new Intent(MainActivity.this,
                        FormularioNotaActivity.class), CODIGO_REQUISICAO_INSERE_NOTA));
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ListaNotasAdapter(todasNotas, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((nota, posicao) -> {
            vaiParaFormularioNotaActivityAltera(nota, posicao);
        });
        new ItemTouchHelper(new NotaItemTouchHelperCallback);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(MainActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10; i++) {
            dao.insere(new Nota("Título " + i, "Descrição " + i));
        }
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODIGO_REQUISICAO_INSERE_NOTA && data.hasExtra(CHAVE_NOTA)) {
            if (resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adapter.adiciona(notaRecebida);
            }
        }

        if (requestCode == CODIGO_REQUISICAO_ALTERA_NOTA && data.hasExtra(CHAVE_NOTA)) {
            if (resultCode == Activity.RESULT_OK) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (posicaoRecebida > POSICAO_INVALIDA) {
                    adapter.altera(posicaoRecebida, notaRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}