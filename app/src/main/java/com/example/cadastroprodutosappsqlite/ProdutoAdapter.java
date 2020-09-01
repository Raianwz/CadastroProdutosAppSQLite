package com.example.cadastroprodutosappsqlite;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ProdutoAdapter extends ArrayAdapter<Produtos> {

    //Variaveis Globais do Adaptador
    Context myContext;
    int produtosLayout;
    List<Produtos> produtosList;
    SQLiteDatabase myBancoDados;

    //Construtor do Adaptador
    public ProdutoAdapter(@NonNull Context myContext, int produtosLayout, List<Produtos> produtosList, SQLiteDatabase myBancoDados) {
        super(myContext, produtosLayout, produtosList);

        this.myContext = myContext;
        this.produtosLayout = produtosLayout;
        this.produtosList = produtosList;
        this.myBancoDados = myBancoDados;
    }

    //Inflar layout com o modelo e suas ações
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(produtosLayout, null);

        final Produtos produtos = produtosList.get(position);

        TextView txtViewNomeProduto = view.findViewById(R.id.nome_produto);
        TextView txtViewTipoProduto = view.findViewById(R.id.nome_tipo);
        TextView txtViewDataEntrada = view.findViewById(R.id.nome_data);

        txtViewNomeProduto.setText(produtos.getProduto());
        txtViewTipoProduto.setText(produtos.getTipo());
        txtViewDataEntrada.setText(produtos.getDataEntrada());

        Button btnEditar = view.findViewById(R.id.btnEditar);
        Button btnExcluir = view.findViewById(R.id.btnExcluir);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarProduto(produtos);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Deseja Excluir?");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM produtos WHERE id = ?";
                        myBancoDados.execSQL(sql, new Integer[]{produtos.getId()});
                        //chamar o método para atualizar a lista de produtos
                        recarregarProdutosDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Não vai fazer nada
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;


    }

    //Irá alterar o produto no banco de dados--------------------------------------------------------------
    public void alterarProduto(final Produtos produtos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);

        LayoutInflater inflater = LayoutInflater.from(myContext);

        View view = inflater.inflate(R.layout.caixa_alterar_produto, null);
        builder.setView(view);

        final EditText txtEditarProduto = view.findViewById(R.id.txtEditarProduto);
        final EditText txtEditarTipo = view.findViewById(R.id.txtEditarTipo);

        txtEditarProduto.setText(produtos.getProduto());
        txtEditarTipo.setText(produtos.getTipo());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnAlterar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String produto = txtEditarProduto.getText().toString().trim();
                String tipo = txtEditarTipo.getText().toString().trim();

                if (produto.isEmpty()) {
                    txtEditarProduto.setError("Produto está em branco");
                    txtEditarProduto.requestFocus();
                    return;
                }

                if (tipo.isEmpty()) {
                    txtEditarTipo.setError("Tipo do produto está em branco");
                    txtEditarTipo.requestFocus();
                    return;
                }

                String sql = "UPDATE produtos SET produto = ?, tipo = ? WHERE id = ?";
                myBancoDados.execSQL(sql, new String[]{produto, tipo, String.valueOf(produtos.getId())});
                Toast.makeText(myContext, "Produto alterado com sucesso!!!", Toast.LENGTH_LONG).show();

                //chamar o método para atualizar a lista de produtos
                recarregarProdutosDB();

                //limpa a estrutura do AlertDialog
                dialog.dismiss();
            }
        });

    }
    //-----------------------------------------------------------------------------------------------------------

    public void recarregarProdutosDB() {
        Cursor cursorProdutos = myBancoDados.rawQuery("SELECT * FROM produtos", null);
        if (cursorProdutos.moveToFirst()) {
            produtosList.clear();
            do {
                produtosList.add(new Produtos(
                        cursorProdutos.getInt(0),
                        cursorProdutos.getString(1),
                        cursorProdutos.getString(2),
                        cursorProdutos.getString(3)
                ));
            } while (cursorProdutos.moveToNext());
        }

        cursorProdutos.close();
        notifyDataSetChanged();
    }


}