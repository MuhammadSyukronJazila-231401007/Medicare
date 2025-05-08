package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Pemeriksaan {
    private String id_pemeriksaan;
    private String nama_dokter; // Ganti id_dokter dengan nama_dokter
    private String nama_pasien; // Ganti id_pasien dengan nama_pasien
    private String tgl_daftar; // Ubah tipe menjadi String sesuai permintaan
    private String jadwal;
    private String nama_ruangan;
    private Double biaya;
    private boolean lunas;

    // Constructor
    public Pemeriksaan(String id_pemeriksaan, String nama_dokter, String nama_pasien, String tgl_daftar, String jadwal, String nama_ruangan, Double biaya) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.nama_dokter = nama_dokter;
        this.nama_pasien = nama_pasien;
        this.tgl_daftar = tgl_daftar;
        this.jadwal = jadwal;
        this.nama_ruangan = nama_ruangan;
        this.biaya = biaya;
        this.lunas = false;
    }

    public Pemeriksaan(String id_pemeriksaan, String nama_dokter, String nama_pasien, String tgl_daftar, String jadwal, String nama_ruangan, Double biaya, boolean lunas) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.nama_dokter = nama_dokter;
        this.nama_pasien = nama_pasien;
        this.tgl_daftar = tgl_daftar;
        this.jadwal = jadwal;
        this.nama_ruangan = nama_ruangan;
        this.biaya = biaya;
        this.lunas = lunas;
    }

    public boolean isLunas() {
        return lunas;
    }

    public void setLunas(boolean lunas) {
        this.lunas = lunas;
    }

    public boolean updateLunas() {
        String query = "UPDATE pemeriksaan SET lunas = TRUE WHERE id_pemeriksaan = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id_pemeriksaan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk menambah data pemeriksaan
    public boolean tambahPemeriksaan() {
        String query = """
            INSERT INTO pemeriksaan (id_pemeriksaan, nama_dokter, nama_pasien, tgl_daftar, jadwal, nama_ruangan, biaya) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_pemeriksaan);
            preparedStatement.setString(2, nama_dokter); // Menggunakan nama dokter
            preparedStatement.setString(3, nama_pasien); // Menggunakan nama pasien
            preparedStatement.setString(4, tgl_daftar); // Menyimpan tgl_daftar sebagai String
            preparedStatement.setString(5, jadwal);
            preparedStatement.setString(6, nama_ruangan);
            preparedStatement.setDouble(7, biaya);

            return preparedStatement.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fungsi untuk mengambil data pemeriksaan
    public static List<Pemeriksaan> ambilData() {
        List<Pemeriksaan> daftarPemeriksaan = new ArrayList<>();
        String query = "SELECT * FROM pemeriksaan";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Pemeriksaan pemeriksaan = new Pemeriksaan(
                        resultSet.getString("id_pemeriksaan"),
                        resultSet.getString("nama_dokter"), // Ambil nama_dokter dari database
                        resultSet.getString("nama_pasien"), // Ambil nama_pasien dari database
                        resultSet.getString("tgl_daftar"), // Ambil tgl_daftar sebagai String
                        resultSet.getString("jadwal"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getDouble("biaya")
                );
                daftarPemeriksaan.add(pemeriksaan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPemeriksaan;
    }

    // Fungsi untuk mengedit data pemeriksaan
    public boolean editPemeriksaan() {
        String query = """
        UPDATE pemeriksaan 
        SET nama_dokter = ?, nama_pasien = ?, tgl_daftar = ?, jadwal = ?, nama_ruangan = ?, biaya = ?
        WHERE id_pemeriksaan = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nama_dokter);
            preparedStatement.setString(2, nama_pasien);
            preparedStatement.setString(3, tgl_daftar); // Update tgl_daftar sebagai String
            preparedStatement.setString(4, jadwal);
            preparedStatement.setString(5, nama_ruangan);
            preparedStatement.setDouble(6, biaya);
            preparedStatement.setString(7, id_pemeriksaan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk menghapus data pemeriksaan
    public boolean hapusPemeriksaan() {
        String query = "DELETE FROM pemeriksaan WHERE id_pemeriksaan = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id_pemeriksaan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Menambahkan fungsi untuk ambil tunggakan pasien
    public static List<Pemeriksaan> ambilTunggakanPasien(String namaPasien) {
        List<Pemeriksaan> pemeriksaanList = new ArrayList<>();
        String query = "SELECT * FROM pemeriksaan WHERE nama_pasien = ? AND lunas = 0";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {
            preparedStatement.setString(1, namaPasien);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Pemeriksaan pemeriksaan = new Pemeriksaan(
                        resultSet.getString("id_pemeriksaan"),
                        resultSet.getString("nama_dokter"), // Ambil nama_dokter dari database
                        resultSet.getString("nama_pasien"), // Ambil nama_pasien dari database
                        resultSet.getString("tgl_daftar"), // Ambil tgl_daftar sebagai String
                        resultSet.getString("jadwal"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getDouble("biaya"),
                        resultSet.getBoolean("lunas")
                );
                pemeriksaanList.add(pemeriksaan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pemeriksaanList;
    }

    // Getter and Setter Methods
    public String getIdPemeriksaan() {
        return id_pemeriksaan;
    }

    public void setId_pemeriksaan(String id_pemeriksaan) {
        this.id_pemeriksaan = id_pemeriksaan;
    }

    public String getNamaDokter() {
        return nama_dokter;
    }

    public void setNama_dokter(String nama_dokter) {
        this.nama_dokter = nama_dokter;
    }

    public String getNamaPasien() {
        return nama_pasien;
    }

    public void setNama_pasien(String nama_pasien) {
        this.nama_pasien = nama_pasien;
    }

    public String getTglDaftar() {
        return tgl_daftar;
    }

    public void setTgl_daftar(String tgl_daftar) {
        this.tgl_daftar = tgl_daftar;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getNamaRuangan() {
        return nama_ruangan;
    }

    public void setNama_ruangan(String nama_ruangan) {
        this.nama_ruangan = nama_ruangan;
    }

    public Double getBiaya() {
        return biaya;
    }

    public void setBiaya(Double biaya) {
        this.biaya = biaya;
    }
}
