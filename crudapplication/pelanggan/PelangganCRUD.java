package crudapplication.pelanggan;

import crudapplication.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class PelangganCRUD extends JFrame {
    private JTextField tfId, tfNama, tfNIK, tfNoTelp, tfAlamat;
    private JButton btnCreate, btnRead, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel tableModel;

    public PelangganCRUD() {
        setTitle("CRUD Data Pelanggan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk Form Input
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("ID Pelanggan:"));
        tfId = new JTextField();
        formPanel.add(tfId);

        formPanel.add(new JLabel("Nama:"));
        tfNama = new JTextField();
        formPanel.add(tfNama);

        formPanel.add(new JLabel("NIK:"));
        tfNIK = new JTextField();
        formPanel.add(tfNIK);

        formPanel.add(new JLabel("No. Telp:"));
        tfNoTelp = new JTextField();
        formPanel.add(tfNoTelp);

        formPanel.add(new JLabel("Alamat:"));
        tfAlamat = new JTextField();
        formPanel.add(tfAlamat);

        // Panel untuk Tombol
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        btnCreate = new JButton("Create");
        btnCreate.addActionListener(e -> {
            createPelanggan();
            loadTableData();
        });
        buttonPanel.add(btnCreate);

        btnRead = new JButton("Read");
        btnRead.addActionListener(e -> readPelanggan());
        buttonPanel.add(btnRead);

        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            updatePelanggan();
            loadTableData();
        });
        buttonPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            deletePelanggan();
            loadTableData();
        });
        buttonPanel.add(btnDelete);

        formPanel.add(buttonPanel);

        // Inisialisasi data dan kolom tabel
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID Pelanggan");
        columnNames.add("Nama");
        columnNames.add("NIK");
        columnNames.add("No. Telp");
        columnNames.add("Alamat");

        tableModel = new DefaultTableModel(columnNames, 0); // Initialize with no rows
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Tambahkan ke Frame
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Muat Data Awal
        loadTableData();

        setVisible(true);
    }

    private void createPelanggan() {
        if (tfNama.getText().isEmpty() || tfNIK.getText().isEmpty() || tfNoTelp.getText().isEmpty() || tfAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO data_pelanggan (nama, nik, notelp, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfNama.getText());
            pstmt.setString(2, tfNIK.getText());
            pstmt.setString(3, tfNoTelp.getText());
            pstmt.setString(4, tfAlamat.getText());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal ditambahkan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void readPelanggan() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_pelanggan WHERE idpelanggan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tfNama.setText(rs.getString("nama"));
                tfNIK.setText(rs.getString("nik"));
                tfNoTelp.setText(rs.getString("notelp"));
                tfAlamat.setText(rs.getString("alamat"));
            } else {
                JOptionPane.showMessageDialog(this, "Data pelanggan tidak ditemukan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePelanggan() {
        if (tfId.getText().isEmpty() || tfNama.getText().isEmpty() || tfNIK.getText().isEmpty() || tfNoTelp.getText().isEmpty() || tfAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE data_pelanggan SET nama = ?, nik = ?, notelp = ?, alamat = ? WHERE idpelanggan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfNama.getText());
            pstmt.setString(2, tfNIK.getText());
            pstmt.setString(3, tfNoTelp.getText());
            pstmt.setString(4, tfAlamat.getText());
            pstmt.setInt(5, Integer.parseInt(tfId.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diperbarui!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal diperbarui!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePelanggan() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Pelanggan harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM data_pelanggan WHERE idpelanggan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfId.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal dihapus!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_pelanggan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            tableModel.setRowCount(0); // Clear the existing rows
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("idpelanggan"));
                row.add(rs.getString("nama"));
                row.add(rs.getString("nik"));
                row.add(rs.getString("notelp"));
                row.add(rs.getString("alamat"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PelangganCRUD();
    }
}
