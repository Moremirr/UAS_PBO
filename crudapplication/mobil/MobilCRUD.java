package crudapplication.mobil;

import crudapplication.utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MobilCRUD extends JFrame {
    private JTextField tfIdMobil, tfMerk, tfTahun, tfHarga;
    private JButton btnCreate, btnRead, btnUpdate, btnDelete, btnRefresh;
    private JTable table;
    private Vector<Vector<Object>> data;
    private Vector<String> columnNames;

    public MobilCRUD() {
        setTitle("CRUD Data Mobil");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk Form Input
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("ID Mobil:"));
        tfIdMobil = new JTextField();
        formPanel.add(tfIdMobil);

        formPanel.add(new JLabel("Merk:"));
        tfMerk = new JTextField();
        formPanel.add(tfMerk);

        formPanel.add(new JLabel("Tahun:"));
        tfTahun = new JTextField();
        formPanel.add(tfTahun);

        formPanel.add(new JLabel("Harga:"));
        tfHarga = new JTextField();
        formPanel.add(tfHarga);

        // Panel untuk Tombol
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        btnCreate = new JButton("Create");
        btnCreate.addActionListener(e -> {
            createMobil();
            loadTableData();
        });
        buttonPanel.add(btnCreate);

        btnRead = new JButton("Read");
        btnRead.addActionListener(e -> readMobil());
        buttonPanel.add(btnRead);

        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            updateMobil();
            loadTableData();
        });
        buttonPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            deleteMobil();
            loadTableData();
        });
        buttonPanel.add(btnDelete);

        formPanel.add(buttonPanel);

        // Inisialisasi data dan kolom tabel
        columnNames = new Vector<>();
        columnNames.add("ID Mobil");
        columnNames.add("Merk");
        columnNames.add("Tahun");
        columnNames.add("Harga");

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

    private void createMobil() {
        if (tfMerk.getText().isEmpty() || tfTahun.getText().isEmpty() || tfHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO data_mobil (merk, tahun, harga) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfMerk.getText());
            pstmt.setInt(2, Integer.parseInt(tfTahun.getText()));
            pstmt.setDouble(3, Double.parseDouble(tfHarga.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data mobil berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal ditambahkan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void readMobil() {
        if (tfIdMobil.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Mobil harus diisi untuk membaca data!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_mobil WHERE idmobil = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdMobil.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tfMerk.setText(rs.getString("merk"));
                tfTahun.setText(rs.getString("tahun"));
                tfHarga.setText(rs.getString("harga"));
            } else {
                JOptionPane.showMessageDialog(this, "Data mobil tidak ditemukan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMobil() {
        if (tfIdMobil.getText().isEmpty() || tfMerk.getText().isEmpty() || tfTahun.getText().isEmpty() || tfHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE data_mobil SET merk = ?, tahun = ?, harga = ? WHERE idmobil = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfMerk.getText());
            pstmt.setInt(2, Integer.parseInt(tfTahun.getText()));
            pstmt.setDouble(3, Double.parseDouble(tfHarga.getText()));
            pstmt.setInt(4, Integer.parseInt(tfIdMobil.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data mobil berhasil diperbarui!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal diperbarui!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMobil() {
        if (tfIdMobil.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Mobil harus diisi!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM data_mobil WHERE idmobil = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(tfIdMobil.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data mobil berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal dihapus!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_mobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            data.clear(); // Hapus data lama
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("idmobil"));
                row.add(rs.getString("merk"));
                row.add(rs.getInt("tahun"));
                row.add(rs.getDouble("harga"));
                data.add(row);
            }
            table.revalidate(); // Refresh tabel setelah data dimuat
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MobilCRUD();
    }
}
