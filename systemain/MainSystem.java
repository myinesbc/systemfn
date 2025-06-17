package systemain;

import login.SistemaLogin;
import users.CadastroAluno;
import sistemafrequencia.Painel;
import sistemafrequencia.Painel2;
import javax.swing.SwingUtilities;

public class MainSystem {
    public static void main(String[] args) {
        System.out.println("==== Iniciando sistema ====");

        SwingUtilities.invokeLater(() -> {
            SistemaLogin sistemaLogin = new SistemaLogin();
            sistemaLogin.setVisible(true);
        });

        System.out.println("O sistema de login foi iniciado.");
    }
}