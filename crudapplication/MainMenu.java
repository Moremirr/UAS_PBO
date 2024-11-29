package crudapplication;

import crudapplication.pelanggan.PelangganCRUD;
import crudapplication.mobil.MobilCRUD;
import crudapplication.penjualan.PenjualanCRUD;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Main Menu");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton btnPelanggan = new JButton("CRUD Pelanggan");
        btnPelanggan.addActionListener(e -> new PelangganCRUD());
        add(btnPelanggan);

        JButton btnMobil = new JButton("CRUD Mobil");
        btnMobil.addActionListener(e -> new MobilCRUD());
        add(btnMobil);

        JButton btnPenjualan = new JButton("CRUD Penjualan");
        btnPenjualan.addActionListener(e -> new PenjualanCRUD());
        add(btnPenjualan);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
