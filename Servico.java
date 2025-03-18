package Aulas_Faculdade.Estrutura_de_Dados_Algoritmo.TrabalhoCadeira;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Aulas_Faculdade.Estrutura_de_Dados_Algoritmo.TrabalhoCadeira.ConexaoBaseDados.Conectar; // importa o metodo conectar na classe


public class Servico {
    Connection conexao = Conectar(); // pega a conexao a base de dados

    // Metodo para consultar saldo(dinheiro)
    public void verificarSaldo() {
        if (conexao != null) {                                //verifica se a conexao foi bem sucedida
            try {                                            // metodo de tratamento de erro try catch
                Statement stmt = conexao.createStatement(); // cria uma conexao com banco de dados para ser possivel executar comandos sql
                ResultSet rs = stmt.executeQuery("SELECT saldo FROM compra WHERE id = 1"); // resultset serve para executar comandos sql diretamente do programa
                if (rs.next()) {                           // verifica se ha mais linhas no resultado, retorna true se tiver e false se nao
                    JOptionPane.showMessageDialog(null, "Saldo: " + rs.getDouble("saldo") + " MZN"); // retorna a consulta feita, de acordo com o tipo  de dado
                }
                registrarMovimento("Consulta Saldo", rs.getDouble("saldo"));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar saldo!"); // se a consulta nao estiver correta, retorna mensagem de erro
            }
        }
    }

    // Metodo para atualizar saldo
    private void Saldonov(double novoSaldo) throws SQLException { // nos seguintes metodos depois de fazer uma tranzacao, o saldo vai ser atuaizado
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate("UPDATE compra SET saldo = " + novoSaldo + " WHERE id = 1");  //volta a executar um comando na tabela
    }

    // metodo para obter o saldo actual, necessario quando quer retornar o saldo para fazer um calculo
    private double getSaldo() throws SQLException {
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT saldo FROM compra WHERE id = 1");
        rs.next();
        return rs.getDouble("saldo");
    }




    // metodo para registrar movimentos (compra, consulta, deposito, levantamento, transferencia)
    // recebe o tipo de movimento(compra, consulta, ect) e o valor, e registra no banco de dados na outra tabela chamada movimentos
    private void registrarMovimento(String tipoMovimento, double valor) throws SQLException {  // o throw e um metodo de tratamento de erro, simplificado ao try catch

        // Obtendo a data e hora atual, mas apenas vai ser interno, o usuario nao vai digitar a data, vai ser automatico

        // o new SimpleDataFormat define o formato da data e hora padrao, na base de dados e datetime, yyyy-mm-dd
        // format(new Date) converte a data actual pra a variavel dataHora, que e uma string, mas para esse formato especifico
        String dataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Comando SQL para inserir os movimentos na tabela "movimento" (tipo, valor e a data e hora da tranzacao)
        String sql = "INSERT INTO movimento (tipo, valor, data_hora) VALUES (?, ?, ?)";

        // pega o comando sql, e adiciona na conexao para ser executado na base de dados
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tipoMovimento); // Tipo do movimento (ex: "Compra de Crédito")
            stmt.setDouble(2, valor);          // Valor do movimento
            stmt.setString(3, dataHora);      // pega a Data e hora do movimento diretamente do sistema

            // Executa o comando na tabela
            stmt.executeUpdate();
        } catch (SQLException e) { // se houver um erro
            JOptionPane.showMessageDialog(null, "Erro ao registrar o movimento!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }





    // metodo para verificar credito
    public void verificarCredito() { // basicament o mesmo processo que o de verificar saldo
        if (conexao != null) {
            try {
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT credito FROM compra WHERE id = 1");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Credito: " + rs.getDouble("credito") + " MZN");
                }
                registrarMovimento("Consulta Credito", rs.getDouble("credito"));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar saldo!");
            }
        }
    }





    // Metodo para comprar credito
    public void comprarCredito() {
        // recebe do usuario o valor a comprar
        String valorCredito = JOptionPane.showInputDialog(null, "Digite o valor do crédito que deseja comprar:", "Compra de Crédito", JOptionPane.INFORMATION_MESSAGE);

        try {
            double valor = Double.parseDouble(valorCredito); // o valor digitado foi uma string, entao agora esta a converter o string, para double, que e o tipo de dado usado na tabela

            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT saldo, credito FROM compra WHERE id = 1"); // busca a coluna saldo e credito
            rs.next();

            double saldo = rs.getDouble("saldo"); // pega os valores das colunas e poe numa variavel, para facilitar os calculos
            double cred = rs.getDouble("credito");

            // verifica se o valor digitado a comprar o credito e maior que o saldo que tem na conta
            if (valor > saldo) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente! Seu saldo é de " + saldo + " MZN.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // verifica se o valor a comprar e menos que 10, nao aceita
            if (valor < 10) {
                JOptionPane.showMessageDialog(null, "O minimo para compra de credito e 10 mzn", "Compra de Credito", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saldo -= valor; // se as condicoes forem cumpridas, o valor do saldo e diminuido
            cred += valor; // e o valor do credito aumenta

            registrarMovimento("Compra de Credito", valor); // adiciona essa compra no metodo registrar movimentos

            // atualiza os valores de credito e saldo na base de dados
            stmt.executeUpdate("UPDATE compra SET saldo = " + saldo + " WHERE id = 1");
            stmt.executeUpdate("UPDATE compra SET credito = " + cred + " WHERE id = 1");
            JOptionPane.showMessageDialog(null, "Crédito comprado com sucesso! \nSaldo atual: " + saldo + " MZN. \n Credito: " + cred + " MZN", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE); // as excecoes se algo der errado
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no comando SQL");
        }
    }






    // Metodo para comprar ofertas(megas, chamada e sms)
    public void comprarOferta(String tipo) {                // recebe o tipo de oferta(mega, chamada ou sms)
        try {

            Statement stmt = conexao.createStatement(); // cria conexao ao BD
            ResultSet rs = stmt.executeQuery("SELECT saldo, credito FROM compra WHERE id = 1"); // busca o saldo e credito
            rs.next();

            // coloca os valores numa variavel pra calculos
            double saldo = rs.getDouble("saldo");
            double credit = rs.getDouble("credito");

            // arrays para guardar as ofertas e os seus precos, e apresentar na tela
            String[] ofertas; // o array de string exibe as ofertas e preco no icone joptionpane.
            double[] precos;  // o array de preco nao e exibido, mas fica nos exatos index, e ao fazer a tranzacao, vai se trabalhar com array de precos

            // Define as ofertas e preços conforme o tipo selecionado ao comecar o metodo
            switch (tipo) {
                case "Megas": //vereifica se o tipo e megas, ele vai apresentar essas ofertas e precos
                    ofertas = new String[]{"10 MT - 295 MB", "20 MT - 550 MB", "50 MT - 1.2 GB"};
                    precos = new double[]{10, 20, 50};
                    break;
                case "SMS":  //vereifica se o tipo e sms, ele vai apresentar essas ofertas e precos
                    ofertas = new String[]{"2 MT - 50 SMS", "5 MT - 100 SMS", "10 MT - 250 SMS"};
                    precos = new double[]{2, 5, 10};
                    break;
                default:    //vereifica se o tipo e chamada, ele vai apresentar essas ofertas e precos, por ser interface, e diferente
                    ofertas = new String[]{"10 MT - 15 min", "20 MT - 35 min", "50 MT - 60 min"};
                    precos = new double[]{10, 20, 50};
                    break;
            }

            // Icone para exibir as opções disponíveis para o usuário escolher uma delas
            // o mesmo icone recebe a escolha do usuario, em formato de uma caixa de opcoes (plainMessage), e os itens apresentados sao do array ofertas, exibe por default a posicao 0
            String escolha = (String) JOptionPane.showInputDialog(null, "Escolha uma oferta de " + tipo, "Ofertas de " + tipo, JOptionPane.PLAIN_MESSAGE, null, ofertas, ofertas[0]);

            if (escolha != null) { // Verifica se o usuário selecionou uma oferta
                for (int i = 0; i < ofertas.length; i++) { // for percorre o array verificando se a opcao escolhida corresponde a uma das oferts
                    if (escolha.equals(ofertas[i])) { // Se a escolha corresponde uma das ofertas (sms, chamada e megas)

                        // liga as posicoes de ofertas com as de precos
                        if (precos[i] > saldo) { // verifica se o preco da oferta e maior que o saldo da conta e se for exibe uma mensagem de erro
                            JOptionPane.showMessageDialog(null, "Saldo insuficiente! Seu saldo é de " + saldo + " MZN.", "Erro", JOptionPane.ERROR_MESSAGE);

                        } else {
                            // array que cria o metodo de pagamento: saldo ou credito, para exibir em uma caixa de opcoes
                            String[] metodo = new String[]{"Saldo", "Credito"};
                            String op = (String) JOptionPane.showInputDialog(null, "Escolha um metodo de pagamento de " + tipo, "Ofertas de " + tipo, JOptionPane.PLAIN_MESSAGE, null, metodo, metodo[0]);

                            // verifica se a opcao do metodo de pagamento do usuario e de dinheiro/saldo
                            if (op.equalsIgnoreCase(metodo[0])) {
                                // reduz o saldo pelo preco da oferta escolhida
                                saldo -= precos[i];

                                // registra a tranzacao no metodo de movimentos
                                registrarMovimento("Compra de Oferta (" + tipo + ")", precos[i]);

                                // atualiza o valor do saldo reduzido na conta
                                stmt.executeUpdate("UPDATE compra SET saldo = " + saldo + " WHERE id = 1");

                                // mensagem de confirmacao
                                JOptionPane.showMessageDialog(null, tipo + " comprado com sucesso! Saldo atual: " + saldo + " MZN.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);


                                // verifica se a opcao do metodo de pagamento do usuario e de credito
                            } else if (op.equalsIgnoreCase(metodo[1])) {
                                credit -= precos[i]; // reduz o valor do credito

                                // registra a compra nos movimentos
                                registrarMovimento("Compra de Oferta (" + tipo + ")", precos[i]);

                                // atualiza o valor de credito na base de dados
                                stmt.executeUpdate("UPDATE compra SET credito = " + credit + " WHERE id = 1");

                                //confirmacao
                                JOptionPane.showMessageDialog(null, tipo + " comprado com sucesso! Credito atual: " + credit + " MZN.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar saldo!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }



    // metodo para levantar dinheiro
    public void levantarDinheiro() {
        // recebe do usuario o valor a ser levantado
        String valorStr = JOptionPane.showInputDialog(null, "Digite o valor a ser levantado:", "Levantamento de Dinheiro", JOptionPane.INFORMATION_MESSAGE);
        // verifica se o campo de valor foi preenchido
        if (valorStr != null) {
            try {
                // converte o valor que o usuario digitou em double, para calculos
                double valor = Double.parseDouble(valorStr);

                // condicao para levantamento: o valor deve ser maior que 15
                if (valor < 15) {
                    JOptionPane.showMessageDialog(null, "O valor mínimo para levantamento é 15 MZN.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Calcular a taxa de levantamento
                double taxa = 0; // cria a variavel taxa

                if (valor <= 100) {
                    taxa = 3;
                } else if (valor <= 1000) {
                    taxa = 8;
                } else if (valor <= 5000) {
                    taxa = 16;
                } else {
                    taxa = 50;
                }

                double saldoAtual = getSaldo(); //
                if (saldoAtual < valor + taxa) {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente! Seu saldo é de " + saldoAtual + " MZN.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Atualizar o saldo
                double novoSaldo = saldoAtual - (valor + taxa);
                Saldonov(novoSaldo);
                registrarMovimento("Levantamento", novoSaldo);
                JOptionPane.showMessageDialog(null, "Levantamento realizado com sucesso! \nTaxa: " + taxa + " MZN. \nSaldo restante: " + novoSaldo + " MZN.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar saldo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo para depositar dinheiro e aumentar o saldo
    public void depositarDinheiro() {
        // pedir para inserir o valor a depositar
        String valorStr = JOptionPane.showInputDialog(null, "Digite o valor a ser depositado:", "Depósito ", JOptionPane.INFORMATION_MESSAGE);

        if (valorStr != null) { // verifica se o campo nao esta vazio
            try {
                double valor = Double.parseDouble(valorStr); // pega o vcalor e converte em double

                // para deposito, o valor nao deve ser menor que 10, verifica
                if (valor < 10) {
                    JOptionPane.showMessageDialog(null, "O valor mínimo para depósito é 10 MZN.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // pega o saldo actual
                double saldoAtual = getSaldo();

                //faz o deposito, aumenta no saldo o valor que depositou
                double novoSaldo = saldoAtual + valor;
                // atualiza a tabela de saldo, com o valor depositado, o saldo total
                Saldonov(novoSaldo);

                // adiciona o a tranzacao do deposito aos movimentos
                registrarMovimento("Deposito", novoSaldo);

                JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso! \nSaldo atual: " + novoSaldo + " MZN.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Tratamento de erros caso o valor e invvalido ou nao concetar ao banco de dados
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar saldo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    //Metodo para transferir dinheiro
    public void realizarTransferencia() {
        // inicializa a variavel que vai buscar o saldo, da base de dados
        double saldo = 0;
        try {
            // Criar um comando SQL para obter o saldo
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT saldo FROM compra WHERE id=1");

            // Se encontrar um saldo, armazena na variável saldo
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco!");
            return;
        }


        // Adicionando ação ao botão
        double saldoTransfer = saldo;
        Connection finalConexao = conexao;

        // Solicita ao usuário que digite o número de destino
        String numero = JOptionPane.showInputDialog("Digite o número de destino (9 dígitos):");

        // Verifica se o número digitado é válido
        if (numero == null || !numero.matches("82\\d{7}|83\\d{7}|84\\d{7}|85\\d{7}|86\\d{7}|87\\d{7}")) {
            JOptionPane.showMessageDialog(null, "Número inválido! A transação foi cancelada.");
            return; // Sai do método caso o número seja inválido
        }

        // Solicita ao usuário o valor a transferir
        String valorStr = JOptionPane.showInputDialog("Digite o valor a transferir:");

        try {
            double valor = Double.parseDouble(valorStr); // Converte o valor digitado para número

            // Verifica se o valor é válido e se há saldo suficiente
            if (valor <= 0 || valor > saldoTransfer) {
                JOptionPane.showMessageDialog(null, "Valor inválido ou saldo insuficiente.");
                return;
            }

            // Atualiza o saldo no banco de dados
            try {
                PreparedStatement stmt = finalConexao.prepareStatement("UPDATE compra SET saldo = saldo - ? WHERE id=1");
                stmt.setDouble(1, valor);
                stmt.executeUpdate();
                registrarMovimento("Transferencia", valor);
                JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar saldo!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Valor inválido!"); // Mensagem de erro caso o valor não seja um número
        }
    }




    // metodo para consulltar ou exibir os movimentos que foram registrados
    public void consultarMovimentos() {
        if (conexao != null) {
            try {
                Statement stmt = conexao.createStatement();
                // busca os tipos, valor, data e hora de todos movimentos para poder exibi-los
                ResultSet rs = stmt.executeQuery("SELECT tipo, valor, data_hora FROM movimento");

                // String builder e uma classe usada para manipylar textos, a forma como sao exibidos
                StringBuilder exibir = new StringBuilder(); // string para armazenar movimentos

                // laco que exibe as linhas de forma como o string builder organizar, se houver um next() ou se houver um proximo elemento, ele vai exibir, ate nao ter nenhum
                while (rs.next()) {
                    exibir.append("Tipo: ").append(rs.getString("tipo"))
                            .append(" | Valor: ").append(rs.getDouble("valor"))
                            .append(" | Data e Hora: ").append(rs.getString("data_hora"))
                            .append("\n\n");
                }

                // verifica se tem movimentos feitos != is.empty(se nao esta vazio)
                if (!exibir.isEmpty()) {

                    // o String builder pra ser exibido no JOptionPane, que so le string, deve ser convertido em string, por isso o exibir.toString
                    JOptionPane.showMessageDialog(null, exibir.toString(), "Movimentos", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // se estar vazio, manda a mensagem de nenhum movimentto registrado
                    JOptionPane.showMessageDialog(null, "Nenhum movimento registrado.", "Movimentos", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar os movimentos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

