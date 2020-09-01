package com.example.cadastroprodutosappsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáveis globais
    public static final String NOME_BANCO_DE_DADOS = "mercado.db";

    EditText txtProduto, txtTipo;
    Button btnAdicionar, btnVisualizar;

    SQLiteDatabase myBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //anunciando ao java componentes do xml
        txtProduto = findViewById(R.id.txtProduto);
        txtTipo = findViewById(R.id.txtTipo);

        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(this);

        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnVisualizar.setOnClickListener(this);

        //Criando banco de dados
        myBancoDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        //criar a tabela no banco de dados especificado
        criarTabelaProduto();

    }

    // este método criará a tabela, mas como chamaremos esse método sempre que lançarmos o app,
    // adicionei IF NOT EXISTS ao SQL, ou seja, só criará a tabela quando a tabela ainda não estiver criada
    //-------------------------------------------------------------------------------------------------------------------
    private void criarTabelaProduto() {
        String sql =
                "CREATE TABLE IF NOT EXISTS produtos (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "produto varchar(200) NOT NULL," +
                        "tipo varchar(200) NOT NULL," +
                        "dataEntrada datetime NOT NULL);";

        myBancoDados.execSQL(sql);
    }
    //-------------------------------------------------------------------------------------------------------------------

    //Este método validará o nome do produto e o tipo -------------------------------------------------------------------
    private boolean verificarEntrada(String produto, String tipo) {
        if (produto.isEmpty()) {
            txtProduto.setError("Por favor, digite o nome do produto");
            txtProduto.requestFocus();
            return false;
        }
        if (tipo.isEmpty()) {
            txtTipo.setError("Por favor, digite o tipo do produto");
            txtTipo.requestFocus();
            return false;
        }
        return true;
    }
    //-------------------------------------------------------------------------------------------------------------------

    //Neste método vamos fazer a operação para adicionar os produtos
    private void adicionarProduto() {

        String nomeProduto = txtProduto.getText().toString().trim();
        String tipoProduto = txtTipo.getText().toString().trim();

        // obtendo o horário atual para data de inclusão
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dataEntrada = simpleDateFormat.format(calendar.getTime());

        //validando entrada
        if (verificarEntrada(nomeProduto, tipoProduto)) {

            String insertSQL = "INSERT INTO produtos (" +
                    "produto, " +
                    "tipo, " +
                    "dataEntrada)" +
                    "VALUES(?, ?, ?);";

            // usando o mesmo método execSQL para inserir valores, desta vez tem dois parâmetros:
            // 1°string sql e 2° são os parâmetros que devem ser vinculados à consulta
            myBancoDados.execSQL(insertSQL, new String[]{nomeProduto, tipoProduto, dataEntrada});

            Toast.makeText(getApplicationContext(), "Produto adicionado com sucesso!!!", Toast.LENGTH_SHORT).show();

        }
    }
    //-------------------------------------------------------------------------------------------------------------------


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdicionar:
                adicionarProduto();
                break;
            case R.id.btnVisualizar:
                startActivity(new Intent(getApplicationContext(), ProdutosActivity.class));
                break;
        }

    }
}