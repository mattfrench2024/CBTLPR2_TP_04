import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class TelaPesquisa extends JFrame {

    JTextField txtBusca, txtNome, txtSalario, txtCargo;
    JButton btnPesquisar, btnAnterior, btnProximo;

    ArrayList<Funcionario> lista = new ArrayList<>();
    int indice = 0;

    Connection conn;

    public TelaPesquisa() {
        super("TRABALHO PRATICO 04");

        setLayout(null);
        setSize(450, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblBusca = new JLabel("Nome:");
        lblBusca.setBounds(20, 20, 60, 20);
        add(lblBusca);

        txtBusca = new JTextField();
        txtBusca.setBounds(80, 20, 200, 25);
        add(txtBusca);

        btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBounds(290, 20, 120, 25);
        add(btnPesquisar);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 70, 60, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(80, 70, 200, 25);
        add(txtNome);

        JLabel lblSalario = new JLabel("Salário:");
        lblSalario.setBounds(20, 100, 60, 20);
        add(lblSalario);

        txtSalario = new JTextField();
        txtSalario.setBounds(80, 100, 200, 25);
        add(txtSalario);

        JLabel lblCargo = new JLabel("Cargo:");
        lblCargo.setBounds(20, 130, 60, 20);
        add(lblCargo);

        txtCargo = new JTextField();
        txtCargo.setBounds(80, 130, 200, 25);
        add(txtCargo);

        btnAnterior = new JButton("Anterior");
        btnAnterior.setBounds(80, 170, 120, 30);
        add(btnAnterior);

        btnProximo = new JButton("Próximo");
        btnProximo.setBounds(220, 170, 120, 30);
        add(btnProximo);

        conectar();

        btnPesquisar.addActionListener(e -> pesquisar());
        btnAnterior.addActionListener(e -> anterior());
        btnProximo.addActionListener(e -> proximo());

        setVisible(true);
    }

    // -----------------------------------------
    // CONEXÃO COM SQL SERVER
    // -----------------------------------------
    public void conectar() {
    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        String url = "jdbc:sqlserver://localhost:1433;databaseName=aulajava;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String pass = "root";

        conn = DriverManager.getConnection(url, user, pass);
        System.out.println("Conectado ao SQL Server!");


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de conexão:\n" + ex.getMessage());
        }
    }

    // -----------------------------------------
    // PESQUISA COM LIKE E PREPAREDSTATEMENT
    // -----------------------------------------
    public void pesquisar() {
        lista.clear();
        indice = 0;

        try {
           String sql = 
    "SELECT f.nome_func, f.sal_func, c.ds_cargo " +
    "FROM tbfuncs f " +
    "JOIN tbcargos c ON f.cod_cargo = c.cd_cargo " +
    "WHERE f.nome_func LIKE ?";


            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + txtBusca.getText() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Funcionario(
                        rs.getString("nome_func"),
                        rs.getDouble("sal_func"),
                        rs.getString("ds_cargo")
                ));
            }

            if (lista.size() == 0) {
                JOptionPane.showMessageDialog(this, "Nenhum registro encontrado.");
                return;
            }

            mostrarRegistro(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // -----------------------------------------
    // NAVEGAÇÃO
    // -----------------------------------------
    public void anterior() {
        if (indice > 0) {
            indice--;
            mostrarRegistro(indice);
        }
    }

    public void proximo() {
        if (indice < lista.size() - 1) {
            indice++;
            mostrarRegistro(indice);
        }
    }

    // -----------------------------------------
    // EXIBE REGISTRO NA TELA
    // -----------------------------------------
    public void mostrarRegistro(int i) {
        Funcionario f = lista.get(i);
        txtNome.setText(f.nome);
        txtSalario.setText(String.valueOf(f.salario));
        txtCargo.setText(f.cargo);
    }

    // -----------------------------------------
    // OBJETO PARA GUARDA DE REGISTROS
    // -----------------------------------------
    class Funcionario {
        String nome, cargo;
        double salario;

        public Funcionario(String nome, double salario, String cargo) {
            this.nome = nome;
            this.salario = salario;
            this.cargo = cargo;
        }
    }

    public static void main(String[] args) {
        new TelaPesquisa();
    }
}
