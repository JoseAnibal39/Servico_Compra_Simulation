package Aulas_Faculdade.Estrutura_de_Dados_Algoritmo.TrabalhoCadeira;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServicoPrincipal {
    public static void main(String[] args) {

        Servico c = new Servico();
        JFrame janela = new JFrame("Serviço de Compra");
        janela.setSize(400, 430);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(null);
        janela.setResizable(false);
        janela.setLocationRelativeTo(null);

        // Rótulo informativo
        JLabel label = new JLabel("Escolha uma opção:");
        label.setBounds(100, 0, 200, 30);
        janela.add(label);

        JButton saldoBotao = new JButton("Verificar Saldo");
        saldoBotao.setBounds(100, 30, 180, 30);
        saldoBotao.addActionListener(_ -> c.verificarSaldo());
        janela.add(saldoBotao);

        JButton verCredBotao = new JButton("Verificar Credito");
        verCredBotao.setBounds(100, 70, 180, 30);
        verCredBotao.addActionListener(_ -> c.verificarCredito());
        janela.add(verCredBotao);

        // Criando botões para as opções do menu
        JButton creditoBot = new JButton("Comprar Crédito");
        creditoBot.setBounds(100, 110, 180, 30);
        creditoBot.addActionListener(_ -> c.comprarCredito());
        janela.add(creditoBot);


        JButton ofertasBot = new JButton("Comprar Ofertas");

        janela.add(ofertasBot);
        ofertasBot.setBounds(100, 150, 180, 30);
        ofertasBot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame janela2 = new JFrame("Comprar Ofertas");
                janela2.setSize(300, 200);
                janela2.setLayout(null);
                janela2.setResizable(false);
                janela2.setLocationRelativeTo(null);
                janela2.setVisible(true);

                JButton megasBot = new JButton("Comprar Megas");
                megasBot.setBounds(60, 10, 180, 30);
                megasBot.addActionListener(_ -> c.comprarOferta("Megas"));
                janela2.add(megasBot);

                JButton smsBot = new JButton("Comprar SMS");
                smsBot.setBounds(60, 50, 180, 30);
                smsBot.addActionListener(_ -> c.comprarOferta("SMS"));
                janela2.add(smsBot);

                JButton chamadaBot = new JButton("Comprar Chamadas");
                chamadaBot.setBounds(60, 90, 180, 30);
                chamadaBot.addActionListener(_ -> c.comprarOferta("Chamadas"));
                janela2.add(chamadaBot);
            }
        });


        JButton btnTransferir = new JButton("Transferir Saldo");
        btnTransferir.setBounds(100, 190, 180, 30);
        btnTransferir.addActionListener(_ -> c.realizarTransferencia());
        janela.add(btnTransferir); // Adicionando o botão na janela

        // Botões para Levantamento e Depósito
        JButton levantar = new JButton("Levantar Dinheiro");
        levantar.setBounds(100, 230, 180, 30);
        levantar.addActionListener(_ -> c.levantarDinheiro());
        janela.add(levantar);

        JButton depositar = new JButton("Depositar Dinheiro");
        depositar.setBounds(100, 270, 180, 30);
        depositar.addActionListener(_ -> c.depositarDinheiro());
        janela.add(depositar);

        JButton movimento = new JButton("Consultar Movimentos");
        movimento.setBounds(100, 310, 180, 30);
        movimento.addActionListener(_ -> c.consultarMovimentos());
        janela.add(movimento);

        JButton exitButton = new JButton("Sair");
        exitButton.setBounds(100, 350, 180, 30);
        exitButton.addActionListener(_ -> System.exit(0));
        janela.add(exitButton);

        janela.setVisible(true);
    }
}
