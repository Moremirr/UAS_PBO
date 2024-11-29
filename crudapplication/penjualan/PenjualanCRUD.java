package crudapplication.penjualan;

import crudapplication.utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class PenjualanCRUD extends JFrame {
    private JTextField tfIdPenjualan, tfIdPelanggan, tfIdMobil, tfTotalBiaya;
    private JButton btnCreate, btnRead, btnUpdate, btnDelete, btnRefresh;
    private JTable table;
    private Vector<Vector<Object>> data;
    private Vector<String> columnNames;

    public PenjualanCRUD() {
        setTitle("CRUD Data Penjualan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk Form Input
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("ID Penjualan:"));
        tfIdPenjualan = new JTextField();
        formPanel.add(tfIdPenjualan);

        formPanel.add(new JLabel("ID Pelanggan:"));
        tfIdPelanggan = new JTextField();
        formPanel.add(tfIdPelanggan);

        formPanel.add(new JLabel("ID Mobil:"));
        tfIdMobil = new JTextField();
        formPanel.add(tfIdMobil);

        formPanel.add(new JLabel("Total Biaya:"));
        tfTotalBiaya = new JTextField();
        formPanel.add(tfTotalBiaya);

        // Panel untuk Tombol
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        btnCreate = new JButton("Create");
        btnCreate.addActionListener(e -> {
            createPenjualan();
            loadTableData();
        });
        buttonPanel.add(btnCreate);

        btnRead = new JButton("Read");
        btnRead.addActionListener(e -> {
            readPenjualan();
        });
        buttonPanel.add(btnRead);

        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            updatePenjualan();
            loadTableData();
        });
        buttonPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            deletePenjualan();
            loadTableData();
        });
        buttonPanel.add(btnDelete);

        formPanel.add(buttonPanel);

        // Inisialisasi data dan kolom tabel
        columnNames = new Vector<>();
        columnNames.add("ID Penjualan");
        columnNames.add("ID Pelanggan");
        columnNames.add("ID Mobil");
        columnNames.add("Total Biaya");

        data = new Vector<>();

        // JTable untuk Menampilkan Data
        table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Tambahkan ke Frame
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Muat Data Awal
        loadTableData();

        setVisible(true);
    }

    private void createPenjualan() {
        if (tfIdPelanggan.getText().isEmpty() || tfIdMobil.getText().isEmpty() || tfTotalBiaya.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO data_penjualan (idpelanggan, idmobil, totalbiaya) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdPelanggan.getText()));
            pstmt.setInt(2, Integer.parseInt(tfIdMobil.getText()));
            pstmt.setDouble(3, Double.parseDouble(tfTotalBiaya.getText()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data penjualan berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal ditambahkan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void readPenjualan() {
        if (tfIdPenjualan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Penjualan harus diisi untuk membaca data!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_penjualan WHERE idpenjualan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdPenjualan.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tfIdPelanggan.setText(rs.getString("idpelanggan"));
                tfIdMobil.setText(rs.getString("idmobil"));
                tfTotalBiaya.setText(rs.getString("totalbiaya"));
            } else {
                JOptionPane.showMessageDialog(this, "Data penjualan tidak ditemukan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePenjualan() {
        if (tfIdPenjualan.getText().isEmpty() || tfIdPelanggan.getText().isEmpty() || tfIdMobil.getText().isEmpty() || tfTotalBiaya.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE data_penjualan SET idpelanggan = ?, idmobil = ?, totalbiaya = ? WHERE idpenjualan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdPelanggan.getText()));
            pstmt.setInt(2, Integer.parseInt(tfIdMobil.getText()));
            pstmt.setDouble(3, Double.parseDouble(tfTotalBiaya.getText()));
            pstmt.setInt(4, Integer.parseInt(tfIdPenjualan.getText()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data penjualan berhasil diperbarui!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal diperbarui!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePenjualan() {
        if (tfIdPenjualan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Penjualan harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM data_penjualan WHERE idpenjualan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdPenjualan.getText()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data penjualan berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal dihapus!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_penjualan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            data.clear(); // Hapus data lama
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("idpenjualan"));
                row.add(rs.getInt("idpelanggan"));
                row.add(rs.getInt("idmobil"));
                row.add(rs.getDouble("totalbiaya"));
                data.add(row);
            }
            table.revalidate(); // Refresh tabel setelah data dimuat
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PenjualanCRUD();
    }
}
