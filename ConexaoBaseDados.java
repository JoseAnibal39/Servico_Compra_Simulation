package Aulas_Faculdade.Estrutura_de_Dados_Algoritmo.TrabalhoCadeira;

import java.sql.*;

public class ConexaoBaseDados {

    public static Connection Conectar() {
        try {
            Connection conexao = DriverManager.getConnection("jdbc:mariadb://localhost:3306/trabalhoprojose", "root", "Jose2007");
            System.out.println("Conexão bem sucedida!!");
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar à base de dados: " + e.getMessage());
        }
        return null;
    }
}
