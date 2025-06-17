package sistemafrequencia;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import banco.ConexaoBanco;
import users.CadastroAluno;
import users.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Painel extends JFrame {

    private static final Logger logger = Logger.getLogger(Painel.class.getName());

    private JPanel alunosEstatisticasPanel;
    private JScrollPane scrollPaneEstatisticas;

    private JButton btnFrequenciaNotas;
    private JButton btnCadastrarAluno;
    private JLabel lblUsuarioNome;
    private JLabel lblUsuarioRA;
    private JLabel lblDataAtual;
    private JLabel lblHeaderFrequencia;

    private Usuario usuarioLogado;

    public Painel(Usuario usuario) {
        this.usuarioLogado = usuario;
        setTitle("Sistema de Frequência - Painel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(128, 0, 128), getWidth(), getHeight(), new Color(0, 128, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout());
        setContentPane(gradientPanel);

        initComponentsCustom();
        carregarAlunosEstatisticas();
        atualizarInformacoesUsuarioNaTela();
    }

    private void initComponentsCustom() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonsPanel.setOpaque(false);

        JButton btnHome = new JButton("HOME");
        btnHome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHome.setBackground(new Color(70, 130, 180));
        btnHome.setForeground(Color.WHITE);
        buttonsPanel.add(btnHome);

        btnFrequenciaNotas = new JButton("FREQUÊNCIA E NOTAS");
        btnFrequenciaNotas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFrequenciaNotas.setBackground(new Color(34, 139, 34));
        btnFrequenciaNotas.setForeground(Color.WHITE);
        btnFrequenciaNotas.addActionListener(evt -> {
            this.dispose();
            new Painel2(usuarioLogado).setVisible(true);
        });
        buttonsPanel.add(btnFrequenciaNotas);

        btnCadastrarAluno = new JButton("CADASTRAR ALUNO");
        btnCadastrarAluno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCadastrarAluno.setBackground(new Color(255, 140, 0));
        btnCadastrarAluno.setForeground(Color.WHITE);
        btnCadastrarAluno.addActionListener(evt -> {
            CadastroAluno cadastroAluno = new CadastroAluno();
            cadastroAluno.setVisible(true);
        });
        buttonsPanel.add(btnCadastrarAluno);
        topPanel.add(buttonsPanel, BorderLayout.WEST);

        JPanel userInfoDatePanel = new JPanel();
        userInfoDatePanel.setLayout(new BoxLayout(userInfoDatePanel, BoxLayout.Y_AXIS));
        userInfoDatePanel.setOpaque(false);
        userInfoDatePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));

        lblUsuarioNome = new JLabel("Nome do Usuário");
        lblUsuarioNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuarioNome.setForeground(Color.WHITE);
        lblUsuarioNome.setAlignmentX(Component.RIGHT_ALIGNMENT);
        userInfoDatePanel.add(lblUsuarioNome);

        lblUsuarioRA = new JLabel("RA: ");
        lblUsuarioRA.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuarioRA.setForeground(Color.WHITE);
        lblUsuarioRA.setAlignmentX(Component.RIGHT_ALIGNMENT);
        userInfoDatePanel.add(lblUsuarioRA);

        topPanel.add(userInfoDatePanel, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        alunosEstatisticasPanel = new JPanel();
        alunosEstatisticasPanel.setLayout(new BoxLayout(alunosEstatisticasPanel, BoxLayout.Y_AXIS));
        alunosEstatisticasPanel.setOpaque(false);
        alunosEstatisticasPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        scrollPaneEstatisticas = new JScrollPane(alunosEstatisticasPanel);
        scrollPaneEstatisticas.setOpaque(false);
        scrollPaneEstatisticas.getViewport().setOpaque(false);
        scrollPaneEstatisticas.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneEstatisticas.setPreferredSize(new Dimension(750, 450));

        centerPanel.add(scrollPaneEstatisticas);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private void carregarAlunosEstatisticas() {
        String sqlSelectAlunos = "SELECT id, nome FROM alunos ORDER BY nome ASC";
        String sqlSelectNotas = "SELECT nota_a1, nota_a2, nota_a3 FROM notas WHERE aluno_id = ?";

        alunosEstatisticasPanel.removeAll();

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 5));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel lblNomeHeader = new JLabel("Nome do Aluno");
        lblNomeHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNomeHeader.setForeground(Color.WHITE);
        lblNomeHeader.setPreferredSize(new Dimension(200, 25));
        headerPanel.add(lblNomeHeader);

        JLabel lblFrequenciaHeader = new JLabel("Frequência (%)");
        lblFrequenciaHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFrequenciaHeader.setForeground(Color.WHITE);
        lblFrequenciaHeader.setPreferredSize(new Dimension(120, 25));
        headerPanel.add(lblFrequenciaHeader);

        JLabel lblNotaFinalHeader = new JLabel("Nota Final");
        lblNotaFinalHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNotaFinalHeader.setForeground(Color.WHITE);
        lblNotaFinalHeader.setPreferredSize(new Dimension(80, 25));
        headerPanel.add(lblNotaFinalHeader);

        alunosEstatisticasPanel.add(headerPanel);

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement pstAlunos = con.prepareStatement(sqlSelectAlunos)) {

            ResultSet rsAlunos = pstAlunos.executeQuery();

            int totalDiasComFrequencia = 0;
            String sqlTotalDias = "SELECT COUNT(DISTINCT data) AS total_dias FROM frequencia";
            try (PreparedStatement pstTotalDias = con.prepareStatement(sqlTotalDias);
                 ResultSet rsTotalDias = pstTotalDias.executeQuery()) {
                if (rsTotalDias.next()) {
                    totalDiasComFrequencia = rsTotalDias.getInt("total_dias");
                }
            }

            while (rsAlunos.next()) {
                int idAluno = rsAlunos.getInt("id");
                String nomeAluno = rsAlunos.getString("nome");

                int diasPresente = 0;
                if (totalDiasComFrequencia > 0) {
                    String sqlDiasPresente = "SELECT COUNT(*) AS dias_presente FROM frequencia WHERE aluno_id = ? AND presenca = TRUE";
                    try (PreparedStatement pstDiasPresente = con.prepareStatement(sqlDiasPresente)) {
                        pstDiasPresente.setInt(1, idAluno);
                        ResultSet rsDiasPresente = pstDiasPresente.executeQuery();
                        if (rsDiasPresente.next()) {
                            diasPresente = rsDiasPresente.getInt("dias_presente");
                        }
                        rsDiasPresente.close();
                    }
                }

                double porcentagemFrequencia = 0.0;
                if (totalDiasComFrequencia > 0) {
                    porcentagemFrequencia = ((double) diasPresente / totalDiasComFrequencia) * 100;
                }

                double notaFinal = 0.0;
                try (PreparedStatement pstNotas = con.prepareStatement(sqlSelectNotas)) {
                    pstNotas.setInt(1, idAluno);
                    ResultSet rsNotas = pstNotas.executeQuery();
                    if (rsNotas.next()) {
                        Double notaA1 = rsNotas.getObject("nota_a1") != null ? rsNotas.getDouble("nota_a1") : null;
                        Double notaA2 = rsNotas.getObject("nota_a2") != null ? rsNotas.getDouble("nota_a2") : null;
                        Double notaA3 = rsNotas.getObject("nota_a3") != null ? rsNotas.getDouble("nota_a3") : null;

                        if (notaA1 != null) notaFinal += notaA1;
                        if (notaA2 != null) notaFinal += notaA2;
                        if (notaA3 != null) notaFinal += notaA3;
                    }
                    rsNotas.close();
                }

                JPanel alunoRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 5));
                alunoRowPanel.setOpaque(false);
                alunoRowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 50)));

                JLabel lblNomeAluno = new JLabel(nomeAluno);
                lblNomeAluno.setPreferredSize(new Dimension(200, 25));
                lblNomeAluno.setForeground(Color.WHITE);
                alunoRowPanel.add(lblNomeAluno);

                JLabel lblFrequencia = new JLabel(String.format("%.0f%%", porcentagemFrequencia));
                lblFrequencia.setPreferredSize(new Dimension(120, 25));
                lblFrequencia.setForeground(Color.WHITE);
                alunoRowPanel.add(lblFrequencia);

                JLabel lblNotaFinal = new JLabel(String.format("%.2f", notaFinal));
                lblNotaFinal.setPreferredSize(new Dimension(80, 25));
                lblNotaFinal.setForeground(Color.WHITE);
                alunoRowPanel.add(lblNotaFinal);

                alunosEstatisticasPanel.add(alunoRowPanel);
            }
            rsAlunos.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao carregar dados de alunos e frequência: ", e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de alunos e frequência: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }

        alunosEstatisticasPanel.revalidate();
        alunosEstatisticasPanel.repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    private void atualizarInformacoesUsuarioNaTela() {
        if (usuarioLogado != null) {
            lblUsuarioNome.setText(usuarioLogado.getFullname());
            lblUsuarioRA.setText("RA: " + usuarioLogado.getIdnumber());
        } else {
            lblUsuarioNome.setText("Usuário Desconhecido");
            lblUsuarioRA.setText("RA: N/A");
        }
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            new Painel(null).setVisible(true);
        });
    }
}