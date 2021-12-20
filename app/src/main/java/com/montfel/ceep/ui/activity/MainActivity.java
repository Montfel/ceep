package com.montfel.ceep.ui.activity;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.montfel.ceep.R;
import com.montfel.ceep.dao.NotaDAO;
import com.montfel.ceep.model.Nota;
import com.montfel.ceep.ui.recycler.ListaNotasAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        recyclerView = findViewById(R.id.recyclerview);

        NotaDAO dao = new NotaDAO();
        for (int i = 1; i <= 10000; i++)
            dao.insere(new Nota("Título" + i, "Descrição" + i));
        List<Nota> todasNotas = dao.todos();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ListaNotasAdapter(todasNotas, this));
    }
}