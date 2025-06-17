package sistemafrequencia; // Make sure this matches your package

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.text.DecimalFormat;

public class AlunoFrequencia {
    private int id;
    private String nome;
    private JCheckBox cbPresente;
    private JCheckBox cbAusente;
    private JTextField tfNotaTotal;
    private JButton btnNotas; // Reference to the "Notas" button

    private Double notaA1;
    private Double notaA2;
    private Double notaA3;

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public AlunoFrequencia(int id, String nome, JCheckBox cbPresente, JCheckBox cbAusente, JTextField tfNotaTotal, JButton btnNotas) {
        this.id = id;
        this.nome = nome;
        this.cbPresente = cbPresente;
        this.cbAusente = cbAusente;
        this.tfNotaTotal = tfNotaTotal;
        this.btnNotas = btnNotas;
    }

    // Getters
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

    public JTextField getTfNotaTotal() {
        return tfNotaTotal;
    }

    public JButton getBtnNotas() {
        return btnNotas;
    }

    public Double getNotaA1() {
        return notaA1;
    }

    public Double getNotaA2() {
        return notaA2;
    }

    public Double getNotaA3() {
        return notaA3;
    }

    // Setters
    public void setNotaA1(Double notaA1) {
        this.notaA1 = notaA1;
    }

    public void setNotaA2(Double notaA2) {
        this.notaA2 = notaA2;
    }

    public void setNotaA3(Double notaA3) {
        this.notaA3 = notaA3;
    }

    // Method to calculate and update the total note displayed in the UI
    public void atualizarNotaTotalUI() {
        double total = 0.0;
        if (notaA1 != null) total += notaA1;
        if (notaA2 != null) total += notaA2;
        if (notaA3 != null) total += notaA3;

        if (notaA1 == null && notaA2 == null && notaA3 == null) {
            tfNotaTotal.setText(""); // Display empty if no notes are set
        } else {
            tfNotaTotal.setText(df.format(total));
        }
    }
}
