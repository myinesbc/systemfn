package sistemafrequencia;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import banco.ConexaoBanco;
import users.Usuario;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;

public class Painel2 extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(Painel2.class.getName());

    private JPanel alunosPanel;
    private List<AlunoFrequencia> listaAlunosFrequencia;

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private JLabel labelNomeUsuario;

    private SimpleDateFormat sdfDb;
    private DecimalFormat decimalFormat;

    private Usuario usuarioLogado;

    public Painel2(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Sistema de Frequência - Frequência e Notas");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
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

        initComponentsCustom(gradientPanel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today = sdf.format(new Date());
        jLabel3.setText(today);

        sdfDb = new SimpleDateFormat("yyyy-MM-dd");
        decimalFormat = new DecimalFormat("0.00");
        
        if (usuarioLogado != null) {
            labelNomeUsuario.setText("Usuário: " + usuarioLogado.getFullname);
        }

        alunosPanel = new JPanel();
        alunosPanel.setLayout(new BoxLayout(alunosPanel, BoxLayout.Y_AXIS));
        alunosPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(alunosPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        gradientPanel.add(scrollPane, BorderLayout.CENTER);

        listaAlunosFrequencia = new ArrayList<>();
        carregarAlunosDoBanco();

        jButton2.addActionListener(e -> salvarFrequenciaNotas());
        
        pack();
    }

    private void initComponentsCustom(JPanel parentPanel) {
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelNomeUsuario = new JLabel();

        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButton1.setText("HOME");
        jButton1.setBackground(new Color(70, 130, 180));
        jButton1.setForeground(Color.WHITE);
        jButton1.addActionListener(evt -> {
            this.dispose();
            new Painel(usuarioLogado).setVisible(true);
        });

        jButton2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButton2.setText("SALVAR");
        jButton2.setBackground(new Color(34, 139, 34));
        jButton2.setForeground(Color.WHITE);

        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("FREQUÊNCIA");
        jLabel2.setPreferredSize(new Dimension(120, 25));
        jLabel2.setForeground(Color.WHITE);

        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(" ");
        jLabel3.setPreferredSize(new Dimension(100, 25));
        jLabel3.setForeground(Color.WHITE);

        labelNomeUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelNomeUsuario.setForeground(Color.WHITE);
        labelNomeUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(jButton1);
        buttonsPanel.add(jButton2);

        JPanel dateInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        dateInfoPanel.setOpaque(false);
        dateInfoPanel.add(jLabel2);
        dateInfoPanel.add(jLabel3);
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(labelNomeUsuario);

        JPanel headerContainerPanel = new JPanel(new BorderLayout());
        headerContainerPanel.setOpaque(false);
        headerContainerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        headerContainerPanel.add(buttonsPanel, BorderLayout.WEST);
        headerContainerPanel.add(userInfoPanel, BorderLayout.CENTER);
        headerContainerPanel.add(dateInfoPanel, BorderLayout.EAST);
        
        parentPanel.add(headerContainerPanel, BorderLayout.NORTH);
    }


    private void carregarAlunosDoBanco() {
        String sqlSelectAlunos = "SELECT id, nome FROM alunos ORDER BY nome ASC";
        String sqlSelectFrequencia = "SELECT presenca FROM frequencia WHERE aluno_id = ? AND data = ?";
        String sqlSelectNotas = "SELECT nota_a1, nota_a2, nota_a3 FROM notas WHERE aluno_id = ?";

        alunosPanel.removeAll();
        listaAlunosFrequencia.clear();

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblNomeAlunoHeader = new JLabel("Nome do Aluno");
        lblNomeAlunoHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNomeAlunoHeader.setForeground(Color.WHITE);
        lblNomeAlunoHeader.setPreferredSize(new Dimension(300, 20));
        headerPanel.add(lblNomeAlunoHeader);

        JLabel lblPresenteHeader = new JLabel("Presente");
        lblPresenteHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPresenteHeader.setForeground(Color.WHITE);
        lblPresenteHeader.setPreferredSize(new Dimension(70, 20));
        headerPanel.add(lblPresenteHeader);

        JLabel lblAusenteHeader = new JLabel("Ausente");
        lblAusenteHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAusenteHeader.setForeground(Color.WHITE);
        lblAusenteHeader.setPreferredSize(new Dimension(70, 20));
        headerPanel.add(lblAusenteHeader);

        JLabel lblNotaHeader = new JLabel("Notas A1/A2/A3");
        lblNotaHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNotaHeader.setForeground(Color.WHITE);
        lblNotaHeader.setPreferredSize(new Dimension(150, 20));
        headerPanel.add(lblNotaHeader);

        alunosPanel.add(headerPanel);

        String dataAtualDB = sdfDb.format(new Date());

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement pstAlunos = con.prepareStatement(sqlSelectAlunos)) {

            ResultSet rsAlunos = pstAlunos.executeQuery();

            while (rsAlunos.next()) {
                int idAluno = rsAlunos.getInt("id");
                String nomeAluno = rsAlunos.getString("nome");

                JPanel alunoRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                alunoRowPanel.setOpaque(false);
                alunoRowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel lblNomeAluno = new JLabel(nomeAluno);
                lblNomeAluno.setForeground(Color.WHITE);
                lblNomeAluno.setPreferredSize(new Dimension(300, 25));
                alunoRowPanel.add(lblNomeAluno);

                JCheckBox cbPresente = new JCheckBox();
                cbPresente.setOpaque(false);
                cbPresente.setPreferredSize(new Dimension(70, 25));
                cbPresente.setHorizontalAlignment(JCheckBox.CENTER);
                alunoRowPanel.add(cbPresente);

                JCheckBox cbAusente = new JCheckBox();
                cbAusente.setOpaque(false);
                cbAusente.setPreferredSize(new Dimension(70, 25));
                cbAusente.setHorizontalAlignment(JCheckBox.CENTER);
                alunoRowPanel.add(cbAusente);

                makeExclusive(cbPresente, cbAusente);

                JButton btnNotas = new JButton("Notas");
                btnNotas.setBackground(new Color(255, 140, 0));
                btnNotas.setForeground(Color.WHITE);
                btnNotas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnNotas.setPreferredSize(new Dimension(150, 25));
                btnNotas.addActionListener(evt -> {
                    for (AlunoFrequencia af : listaAlunosFrequencia) {
                        if (af.getId() == idAluno) {
                            avaliarAluno(af);
                            break;
                        }
                    }
                });
                alunoRowPanel.add(btnNotas);

                AlunoFrequencia alunoFreq = new AlunoFrequencia(idAluno, nomeAluno, cbPresente, cbAusente, btnNotas);
                listaAlunosFrequencia.add(alunoFreq);

                try (PreparedStatement pstFrequencia = con.prepareStatement(sqlSelectFrequencia)) {
                    pstFrequencia.setInt(1, idAluno);
                    pstFrequencia.setString(2, dataAtualDB);
                    ResultSet rsFrequencia = pstFrequencia.executeQuery();

                    if (rsFrequencia.next()) {
                        boolean presenca = rsFrequencia.getBoolean("presenca");
                        if (presenca) {
                            cbPresente.setSelected(true);
                        } else {
                            cbAusente.setSelected(true);
                        }
                    }
                    rsFrequencia.close();
                }

                try (PreparedStatement pstNotas = con.prepareStatement(sqlSelectNotas)) {
                    pstNotas.setInt(1, idAluno);
                    ResultSet rsNotas = pstNotas.executeQuery();

                    if (rsNotas.next()) {
                        Double notaA1 = rsNotas.getObject("nota_a1") != null ? rsNotas.getDouble("nota_a1") : null;
                        Double notaA2 = rsNotas.getObject("nota_a2") != null ? rsNotas.getDouble("nota_a2") : null;
                        Double notaA3 = rsNotas.getObject("nota_a3") != null ? rsNotas.getDouble("nota_a3") : null;

                        alunoFreq.setNotaA1(notaA1);
                        alunoFreq.setNotaA2(notaA2);
                        alunoFreq.setNotaA3(notaA3);
                    }
                    rsNotas.close();
                }

                alunosPanel.add(alunoRowPanel);
            }
            rsAlunos.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao carregar alunos, frequência e notas do banco de dados", e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }

        alunosPanel.revalidate();
        alunosPanel.repaint();
    }

    private void salvarFrequenciaNotas() {
        String dataAtualDB = sdfDb.format(new Date());

        String sqlCheckExistingFrequency = "SELECT COUNT(*) FROM frequencia WHERE aluno_id = ? AND data = ?";
        String sqlUpdateFrequency = "UPDATE frequencia SET presenca = ? WHERE aluno_id = ? AND data = ?";
        String sqlInsertFrequency = "INSERT INTO frequencia (aluno_id, data, presenca) VALUES (?, ?, ?)";

        String sqlCheckExistingNotes = "SELECT COUNT(*) FROM notas WHERE aluno_id = ?";
        String sqlUpdateNotes = "UPDATE notas SET nota_a1 = ?, nota_a2 = ?, nota_a3 = ? WHERE aluno_id = ?";
        String sqlInsertNotes = "INSERT INTO notas (aluno_id, nota_a1, nota_a2, nota_a3) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);

            for (AlunoFrequencia aluno : listaAlunosFrequencia) {
                boolean presenca = aluno.getCbPresente().isSelected();
                try (PreparedStatement pstCheckFreq = con.prepareStatement(sqlCheckExistingFrequency)) {
                    pstCheckFreq.setInt(1, aluno.getId());
                    pstCheckFreq.setString(2, dataAtualDB);
                    ResultSet rsCheckFreq = pstCheckFreq.executeQuery();
                    rsCheckFreq.next();
                    boolean freqExists = rsCheckFreq.getInt(1) > 0;
                    rsCheckFreq.close();

                    if (freqExists) {
                        try (PreparedStatement pstUpdateFreq = con.prepareStatement(sqlUpdateFrequency)) {
                            pstUpdateFreq.setBoolean(1, presenca);
                            pstUpdateFreq.setInt(2, aluno.getId());
                            pstUpdateFreq.setString(3, dataAtualDB);
                            pstUpdateFreq.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement pstInsertFreq = con.prepareStatement(sqlInsertFrequency)) {
                            pstInsertFreq.setInt(1, aluno.getId());
                            pstInsertFreq.setString(2, dataAtualDB);
                            pstInsertFreq.setBoolean(3, presenca);
                            pstInsertFreq.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement pstCheckNotes = con.prepareStatement(sqlCheckExistingNotes)) {
                    pstCheckNotes.setInt(1, aluno.getId());
                    ResultSet rsCheckNotes = pstCheckNotes.executeQuery();
                    rsCheckNotes.next();
                    boolean notesExists = rsCheckNotes.getInt(1) > 0;
                    rsCheckNotes.close();

                    if (notesExists) {
                        try (PreparedStatement pstUpdateNotes = con.prepareStatement(sqlUpdateNotes)) {
                            setDoubleOrNull(pstUpdateNotes, 1, aluno.getNotaA1());
                            setDoubleOrNull(pstUpdateNotes, 2, aluno.getNotaA2());
                            setDoubleOrNull(pstUpdateNotes, 3, aluno.getNotaA3());
                            pstUpdateNotes.setInt(4, aluno.getId());
                            pstUpdateNotes.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement pstInsertNotes = con.prepareStatement(sqlInsertNotes)) {
                            pstInsertNotes.setInt(1, aluno.getId());
                            setDoubleOrNull(pstInsertNotes, 2, aluno.getNotaA1());
                            setDoubleOrNull(pstInsertNotes, 3, aluno.getNotaA2());
                            setDoubleOrNull(pstInsertNotes, 4, aluno.getNotaA3());
                            pstInsertNotes.executeUpdate();
                        }
                    }
                }
            }
            con.commit();
            JOptionPane.showMessageDialog(this, "Frequência e notas salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao salvar frequência e notas no banco de dados", e);
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage(), "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
            try (Connection con = ConexaoBanco.getConnection()) {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Erro ao reverter transação", rollbackEx);
            }
        }
    }

    private void setDoubleOrNull(PreparedStatement pst, int parameterIndex, Double value) throws SQLException {
        if (value != null) {
            pst.setDouble(parameterIndex, value);
        } else {
            pst.setNull(parameterIndex, java.sql.Types.DECIMAL);
        }
    }

    private void avaliarAluno(AlunoFrequencia alunoFreq) {
        Object[] options = {"A1", "A2", "A3"};
        String[] evaluationNames = {"A1", "A2", "A3"};

        int choice = JOptionPane.showOptionDialog(
            this,
            "Escolha qual avaliação você deseja registrar ou alterar:",
            "Notas",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        JTextField numField = new JTextField();
        ((AbstractDocument) numField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("[0-9.,]*")) {
                    super.insertString(fb, off, str, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("[0-9.,]*")) {
                    super.replace(fb, off, len, str, attr);
                }
            }
        });


        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(numField);
        numField.setPreferredSize(new Dimension(100, 25));

        Double currentNote = null;
        if (choice == 0) currentNote = alunoFreq.getNotaA1();
        else if (choice == 1) currentNote = alunoFreq.getNotaA2();
        else if (choice == 2) currentNote = alunoFreq.getNotaA3();

        if (currentNote != null) {
            numField.setText(decimalFormat.format(currentNote).replace(",", "."));
        }

        int res = JOptionPane.showConfirmDialog(
            this,
            inputPanel,
            "Digite a nota para " + evaluationNames[choice] + " de " + alunoFreq.getNome(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            String text = numField.getText().trim();
            if (!text.isEmpty()) {
                try {
                    double value = Double.parseDouble(text.replace(",", "."));

                    if ((choice == 0 || choice == 1) && !(value >= 0 && value <= 30)) {
                        JOptionPane.showMessageDialog(this, "Para " + evaluationNames[choice] + ", o valor deve estar entre 0 e 30.", "Erro de Nota", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (choice == 2 && !(value >= 0 && value <= 40)) {
                        JOptionPane.showMessageDialog(this, "Para A3, o valor deve estar entre 0 e 40.", "Erro de Nota", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (choice == 0) alunoFreq.setNotaA1(value);
                    else if (choice == 1) alunoFreq.setNotaA2(value);
                    else if (choice == 2) alunoFreq.setNotaA3(value);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Número inválido. Por favor, digite um valor numérico válido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                int confirmClear = JOptionPane.showConfirmDialog(this, "Deseja remover esta nota?", "Limpar Nota", JOptionPane.YES_NO_OPTION);
                if (confirmClear == JOptionPane.YES_OPTION) {
                    if (choice == 0) alunoFreq.setNotaA1(null);
                    else if (choice == 1) alunoFreq.setNotaA2(null);
                    else if (choice == 2) alunoFreq.setNotaA3(null);
                }
            }
        }
    }

    private void makeExclusive(javax.swing.JCheckBox box1, javax.swing.JCheckBox box2) {
        box1.addActionListener(e -> {
            if (box1.isSelected()) box2.setSelected(false);
        });
        box2.addActionListener(e -> {
            if (box2.isSelected()) box1.setSelected(false);
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Painel2(null).setVisible(true);
        });
    }

    private class AlunoFrequencia {
        private int id;
        private String nome;
        private JCheckBox cbPresente;
        private JCheckBox cbAusente;
        private JButton btnNotas;

        private Double notaA1;
        private Double notaA2;
        private Double notaA3;

        public AlunoFrequencia(int id, String nome, JCheckBox cbPresente, JCheckBox cbAusente, JButton btnNotas) {
            this.id = id;
            this.nome = nome;
            this.cbPresente = cbPresente;
            this.cbAusente = cbAusente;
            this.btnNotas = btnNotas;
        }

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public JCheckBox getCbPresente() {
            return cbPresente;
        }

        public JCheckBox getCbAusente() {
            return cbAusente;
        }

        public JButton getBtnNotas() {
            return btnNotas;
        }

        public Double getNotaA1() {
            return notaA1;
        }

        public void setNotaA1(Double notaA1) {
            this.notaA1 = notaA1;
        }

        public Double getNotaA2() {
            return notaA2;
        }

        public void setNotaA2(Double notaA2) {
            this.notaA2 = notaA2;
        }

        public Double getNotaA3() {
            return notaA3;
        }

        public void setNotaA3(Double notaA3) {
            this.notaA3 = notaA3;
        }
    }
}