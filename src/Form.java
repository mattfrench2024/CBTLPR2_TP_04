/*
IFSP - CAMPUS CUBATÃO
TURMA: ADS 471 - LINGUAGEM DE PROGRAMAÇÃO II
INTEGRANTES:
-> Matheus Correia de França
-> Davi Leite Coelho
*/

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class Form extends JFrame {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=aulajava;encrypt=false;trustServerCertificate=true;";
    private static final String USUARIO = "sa";
    private static final String SENHA = "root";

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private JPanel painelPrincipal;
    private JPanel painelSuperior;
    private JPanel painelInferior;
    private JSeparator separator = new JSeparator();

    private JTextField txtNomePesquisado = new JTextField(20);
    private JTextField txtNome = new JTextField(20);
    private JTextField txtSalario = new JTextField(20);
    private JTextField txtCargo = new JTextField(20);

    private JButton btnPesquisar = new JButton("Pesquisar");
    private JButton btnAnterior = new JButton("Anterior");
    private JButton btnProximo = new JButton("Próximo");

    public Form() {
        setTitle("TP04 - LPR2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        painelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelSuperior.setBorder(new EmptyBorder(10, 10, 0, 0));
        painelSuperior.add(new JLabel("Nome:"));
        painelSuperior.add(txtNomePesquisado);
        painelSuperior.add(btnPesquisar);

        painelInferior = new JPanel(new GridLayout(4, 2, 10, 10));
        painelInferior.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelInferior.add(new JLabel("Nome:"));
        txtNome.setEditable(false);
        painelInferior.add(txtNome);

        painelInferior.add(new JLabel("Salário:"));
        txtSalario.setEditable(false);
        painelInferior.add(txtSalario);

        painelInferior.add(new JLabel("Cargo:"));
        txtCargo.setEditable(false);
        painelInferior.add(txtCargo);

        painelInferior.add(btnAnterior);
        painelInferior.add(btnProximo);

        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        separator.setForeground(Color.BLACK);
        painelPrincipal.add(painelSuperior);
        painelPrincipal.add(separator);
        painelPrincipal.add(painelInferior);

        btnAnterior.setEnabled(false);
        btnProximo.setEnabled(false);

        btnPesquisar.addActionListener(e -> pesquisarFuncionario());
        btnProximo.addActionListener(e -> navegar(true));
        btnAnterior.addActionListener(e -> navegar(false));

        add(painelPrincipal, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        pesquisarFuncionario();
    }

    private void pesquisarFuncionario() {
        String nome = txtNomePesquisado.getText().trim();

        String sql = "SELECT f.nome_func, f.sal_func, c.ds_cargo "
                   + "FROM tbfuncs f "
                   + "JOIN tbcargos c ON f.cod_cargo = c.cd_cargo "
                   + "WHERE f.nome_func LIKE ?";

        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            ps = conn.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            if (nome.isEmpty())
                ps.setString(1, "%");
            else
                ps.setString(1, nome + "%");

            rs = ps.executeQuery();

            if (rs.next())
                atualizarCampos();
            else {
                JOptionPane.showMessageDialog(this,
                    "Nenhum registro encontrado.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
                limpaTela();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao pesquisar: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navegar(boolean proximo) {
        try {
            if (rs == null)
                return;

            boolean moveu = proximo ? rs.next() : rs.previous();

            if (moveu)
                atualizarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao navegar: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCampos() {
        try {
            txtNome.setText(rs.getString("nome_func"));
            txtSalario.setText(String.format("R$ %.2f", rs.getDouble("sal_func")));
            txtCargo.setText(rs.getString("ds_cargo"));

            btnAnterior.setEnabled(!rs.isFirst());
            btnProximo.setEnabled(!rs.isLast());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao atualizar campos: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpaTela() {
        txtNome.setText("");
        txtSalario.setText("");
        txtCargo.setText("");
        btnAnterior.setEnabled(false);
        btnProximo.setEnabled(false);
    }

    public static void main(String[] args) {
    String sqlCreateCargos =
        "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbcargos') "
        + "CREATE TABLE tbcargos ("
        + "  cd_cargo INT PRIMARY KEY IDENTITY(1,1),"
        + "  ds_cargo VARCHAR(30) UNIQUE"
        + ")";

    String sqlCreateFuncs =
        "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tbfuncs') "
        + "CREATE TABLE tbfuncs ("
        + "  cod_func INT PRIMARY KEY IDENTITY(1,1),"
        + "  nome_func VARCHAR(50) UNIQUE,"
        + "  sal_func DECIMAL(10,2),"
        + "  cod_cargo INT,"
        + "  FOREIGN KEY (cod_cargo) REFERENCES tbcargos (cd_cargo)"
        + ")";

    String sqlInserts =
        // cargos
        "IF NOT EXISTS (SELECT * FROM tbcargos WHERE ds_cargo = 'Programador Júnior') "
        + "INSERT INTO tbcargos (ds_cargo) VALUES ('Programador Júnior');"

        + "IF NOT EXISTS (SELECT * FROM tbcargos WHERE ds_cargo = 'Analista de Sistemas') "
        + "INSERT INTO tbcargos (ds_cargo) VALUES ('Analista de Sistemas');"

        // funcionários (buscando o cd_cargo correto)
        + "IF NOT EXISTS (SELECT * FROM tbfuncs WHERE nome_func = 'Matheus') "
        + "INSERT INTO tbfuncs (nome_func, sal_func, cod_cargo) "
        + "SELECT 'Matheus', 4200.00, cd_cargo FROM tbcargos "
        + "WHERE ds_cargo = 'Programador Júnior';"

        + "IF NOT EXISTS (SELECT * FROM tbfuncs WHERE nome_func = 'Davi') "
        + "INSERT INTO tbfuncs (nome_func, sal_func, cod_cargo) "
        + "SELECT 'Davi', 5200.00, cd_cargo FROM tbcargos "
        + "WHERE ds_cargo = 'Analista de Sistemas';";

    try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
         Statement stmt = conn.createStatement()) {

        stmt.execute(sqlCreateCargos);
        stmt.execute(sqlCreateFuncs);
        stmt.execute(sqlInserts);

        System.out.println("Banco criado e populado com sucesso.");

    } catch (SQLException e) {
        System.out.println("Erro ao iniciar o Banco de Dados: " + e.getMessage());
    }

    SwingUtilities.invokeLater(() -> new Form().setVisible(true));
}


}
