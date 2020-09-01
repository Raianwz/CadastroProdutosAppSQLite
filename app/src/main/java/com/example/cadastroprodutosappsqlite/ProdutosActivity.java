package com.example.cadastroprodutosappsqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    List<Produtos> produtosList;
    ProdutoAdapter produtoAdapter;
    SQLiteDatabase myBancoDados;
    ListView listViewProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos_layout);

        listViewProdutos = findViewById(R.id.listarProdutosView);
        produtosList = new ArrayList<>();

        myBancoDados = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarProdutosDatabase();
    }


    //Listará todos os produtos do banco de dados--------------------------------------------------------------
    private void visualizarProdutosDatabase() {

        Cursor cursorProdutos = myBancoDados.rawQuery("SELECT * FROM produtos", null);

        if (cursorProdutos.moveToFirst()) {
            do {
                produtosList.add(new Produtos(
                        cursorProdutos.getInt(0),    //id
                        cursorProdutos.getString(1), //produto
                        cursorProdutos.getString(2), // tipo
                        cursorProdutos.getString(3)  // dataEntrada
                ));
            } while (cursorProdutos.moveToNext());
        }
        cursorProdutos.close();

        //Verificar o layout
        produtoAdapter = new ProdutoAdapter(this, R.layout.produtos_molde, produtosList, myBancoDados);

        listViewProdutos.setAdapter(produtoAdapter);
    }
    //----------------------------------------------------------------------------------------------------------
}