package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Pembayaran {
    private String idPembayaran;
    private String namaPasien;
    private Double totalBayar;
    private String metodePembayaran;
    private String tanggalPembayaran;
    private String status;
    private String jenisLayanan;  // Menambahkan jenis layanan

    // Konstruktor
    public Pembayaran(String idPembayaran, String namaPasien, Double totalBayar,
                      String metodePembayaran, String tanggalPembayaran, String status, String jenisLayanan) {
        this.idPembayaran = idPembayaran;
        this.namaPasien = namaPasien;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.tanggalPembayaran = tanggalPembayaran;
        this.status = status;
        this.jenisLayanan = jenisLayanan;  // Menambahkan jenis layanan pada konstruktor
    }

    // Getter
    public String getIdPembayaran() {
        return idPembayaran;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public Double getTotalBayar() {
        return totalBayar;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public String getTanggalPembayaran() {
        return tanggalPembayaran;
    }

    public String getStatus() {
        return status;
    }

    public String getJenisLayanan() {  // Getter untuk jenis layanan
        return jenisLayanan;
    }

    // Fungsi untuk menambah pembayaran ke database
    public boolean tambahPembayaran() {
        String query = """
            INSERT INTO pembayaran (id_pembayaran, nama_pasien, total_bayar, metode_pembayaran, tanggal_pembayaran, status, jenis_layanan) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;  // Memperbarui query dengan menambahkan kolom jenis_layanan

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, idPembayaran);
            preparedStatement.setString(2, namaPasien);
            preparedStatement.setDouble(3, totalBayar);
            preparedStatement.setString(4, metodePembayaran);
            preparedStatement.setString(5, tanggalPembayaran);
            preparedStatement.setString(6, status);
            preparedStatement.setString(7, jenisLayanan);  // Menambahkan jenis layanan

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fungsi untuk mengedit pembayaran
    public boolean editPembayaran() {
        String query = """
        UPDATE pembayaran 
        SET nama_pasien = ?, total_bayar = ?, metode_pembayaran = ?, tanggal_pembayaran = ?, status = ?, jenis_layanan = ?
        WHERE id_pembayaran = ?
        """;  // Menambahkan jenis_layanan dalam query UPDATE

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, namaPasien);
            preparedStatement.setDouble(2, totalBayar);
            preparedStatement.setString(3, metodePembayaran);
            preparedStatement.setString(4,tanggalPembayaran);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, jenisLayanan);  // Menambahkan jenis layanan
            preparedStatement.setString(7, idPembayaran);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fungsi untuk mengambil data pembayaran dari database
    public static List<Pembayaran> ambilData() {
        List<Pembayaran> daftarPembayaran = new ArrayList<>();
        String query = "SELECT * FROM pembayaran";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Pembayaran pembayaran = new Pembayaran(
                        resultSet.getString("id_pembayaran"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getDouble("total_bayar"),
                        resultSet.getString("metode_pembayaran"),
                        resultSet.getString("tanggal_pembayaran"),
                        resultSet.getString("status"),
                        resultSet.getString("jenis_layanan")  // Mengambil jenis layanan dari result set
                );
                daftarPembayaran.add(pembayaran);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPembayaran;
    }

    // Fungsi untuk menghapus pembayaran dari database
    public boolean deletePembayaran() {
        String query = "DELETE FROM pembayaran WHERE id_pembayaran = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, this.idPembayaran);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fungsi untuk mengupdate status pembayaran
    public boolean updateStatus(String statusBaru) {
        this.status = statusBaru;

        String query = "UPDATE pembayaran SET status = ? WHERE id_pembayaran = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, this.status);
            preparedStatement.setString(2, this.idPembayaran);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Fungsi untuk cek status pembayaran
    public boolean cekStatusPembayaran() {
        return "Lunas".equalsIgnoreCase(status);
    }
}
