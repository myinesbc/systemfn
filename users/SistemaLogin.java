package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import users.Usuario;
import banco.UsuarioDAO;
import javax.swing.text.*;
import sistemafrequencia.Painel;

public class SistemaLogin extends JFrame {
    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    public SistemaLogin() {
        setTitle("Sistema de Login e Cadastro");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        painelPrincipal.add(criarPainelLogin(), "login");
        painelPrincipal.add(criarPainelCadastro(), "cadastro");

        add(painelPrincipal);
        cardLayout.show(painelPrincipal, "login");
    }
    
    private void limitarCaracteres(JTextField campo, int maxLength) {
        AbstractDocument doc = (AbstractDocument) campo.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                int totalLength = fb.getDocument().getLength() + text.length() - length;
                if (totalLength <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                int totalLength = fb.getDocument().getLength() + text.length();
                if (totalLength <= maxLength) {
                    super.insertString(fb, offset, text, attr);
                }
            }
        });
    }
    
    private JPanel criarPainelLogin() {
        JPanel painel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(128, 0, 128), getWidth(), getHeight(), Color.GREEN);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
                            
        painel.setLayout(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("/unifg-removebg-preview.png"));
        JLabel imagemLabel = new JLabel(icon);
        imagemLabel.setBounds(150, 20, 300, 150);
        painel.add(imagemLabel);

        JLabel idnumberLabel = new JLabel("RA:"); 
        idnumberLabel.setForeground(Color.WHITE);
        idnumberLabel.setBounds(150, 200, 80, 25);
        painel.add(idnumberLabel);

        JTextField idnumberField = new JTextField();
        idnumberField.setBounds(230, 200, 200, 25);
        painel.add(idnumberField);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setForeground(Color.WHITE);
        senhaLabel.setBounds(150, 250, 80, 25);
        painel.add(senhaLabel);

        JPasswordField senhaField = new JPasswordField();
        senhaField.setBounds(230, 250, 200, 25);
        painel.add(senhaField);
        limitarCaracteres(senhaField, 10);

        JButton loginButton = new JButton("Entrar");
        loginButton.setBounds(230, 300, 100, 30);
        painel.add(loginButton);

        loginButton.addActionListener(e -> {
            String idnumberText = idnumberField.getText().trim();
            String senha = new String(senhaField.getPassword());

            if (idnumberText.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                return;
            }
            
            if (!idnumberText.matches("\\d+")) { 
                JOptionPane.showMessageDialog(this, "O RA deve conter apenas números.");
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorIdnumber(idnumberText); 

            if (usuario != null && usuario.getPassword().equals(senha)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getFullname() + "!");
                
                Painel painelPrincipalApp = new Painel(usuario);
                painelPrincipalApp.setVisible(true);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "RA ou senha inválidos.");
            }
        });

        JButton irCadastroButton = new JButton("Não tem conta? Cadastre-se");
        irCadastroButton.setBounds(200, 350, 200, 25);
        painel.add(irCadastroButton);
        irCadastroButton.addActionListener(e -> cardLayout.show(painelPrincipal, "cadastro"));

        return painel;
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setLayout(null);

        JLabel imagemLabel = new JLabel(new ImageIcon(getClass().getResource("/unifg-removebg-preview.png")));
        imagemLabel.setBounds(20, 50, 150, 300);
        painel.add(imagemLabel);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(200, 50, 80, 25);
        painel.add(nomeLabel);

        JTextField nomeField = new JTextField();
        nomeField.setBounds(280, 50, 250, 25);
        nomeField.setBackground(new Color(128, 0, 128));
        nomeField.setForeground(Color.WHITE);
        painel.add(nomeField);

        JLabel idnumberLabel = new JLabel("RA:");
        idnumberLabel.setBounds(200, 100, 80, 25);
        painel.add(idnumberLabel);

        JTextField idnumberField = new JTextField();
        idnumberField.setBounds(280, 100, 250, 25);
        idnumberField.setBackground(new Color(128, 0, 128));
        idnumberField.setForeground(Color.WHITE);
        painel.add(idnumberField);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setBounds(200, 150, 80, 25);
        painel.add(senhaLabel);

        JPasswordField senhaField = new JPasswordField();
        senhaField.setBounds(280, 150, 250, 25);
        senhaField.setBackground(new Color(128, 0, 128));
        senhaField.setForeground(Color.WHITE);
        painel.add(senhaField);
        limitarCaracteres(senhaField, 10);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setBounds(280, 200, 150, 30);
        cadastrarButton.setBackground(Color.BLACK);
        cadastrarButton.setForeground(Color.WHITE);
        painel.add(cadastrarButton);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String idnumberText = idnumberField.getText().trim();
            String senha = new String(senhaField.getPassword());

            if (nome.isEmpty() || idnumberText.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                return;
            }
            if (nome.split("\\s+").length < 2) {
                JOptionPane.showMessageDialog(this, "Insira o nome completo.");
                return;
            }
            if (!idnumberText.matches("\\d+") || idnumberText.length() < 10) {
                JOptionPane.showMessageDialog(this, "Insira um RA válido (apenas números, mínimo 10 dígitos).");
                return;
            }
            if (senha.length() < 8) {
                JOptionPane.showMessageDialog(this, "A senha deve ter, no mínimo, 8 caracteres.");
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario existente = usuarioDAO.buscarPorIdnumber(idnumberText); 
            if (existente != null) {
                JOptionPane.showMessageDialog(this, "Este RA já está cadastrado."); 
                return;
            }

            Usuario novoUsuario = new Usuario(nome, idnumberText, senha); 
            boolean sucesso = usuarioDAO.inserirUsuario(novoUsuario);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                nomeField.setText("");
                idnumberField.setText("");
                senhaField.setText("");
                cardLayout.show(painelPrincipal, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
            }
        });

        JButton voltarLoginButton = new JButton("Já tem conta? Faça login");
        voltarLoginButton.setBounds(280, 250, 200, 25);
        painel.add(voltarLoginButton);
        voltarLoginButton.addActionListener(e -> cardLayout.show(painelPrincipal, "login"));

        return painel;
    }
}