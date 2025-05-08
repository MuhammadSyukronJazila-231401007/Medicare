package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.net.Inet4Address;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Obat {
    private String id_obat;
    private String nama_obat;
    private Double harga;
    private Date tgl_expired;
    private Integer stok;
    private String satuan;

    public Obat(String id_obat, String nama_obat, Double harga,
                Date tgl_expired, Integer stok , String satuan) {
        this.id_obat = id_obat;
        this.nama_obat = nama_obat;
        this.harga = harga;
        this.tgl_expired = tgl_expired;
        this.stok = stok;
        this.satuan = satuan;
    }

    public boolean tambahObat(){
        String query = """
            INSERT INTO obat (id_obat, nama_obat, harga, tgl_expired, stok, satuan) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_obat);
            preparedStatement.setString(2, nama_obat);
            preparedStatement.setDouble(3, harga);
            preparedStatement.setDate(4, (java.sql.Date) tgl_expired);
            preparedStatement.setInt(5, stok);
            preparedStatement.setString(6, satuan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editObat() {
        String query = """
        UPDATE obat 
        SET nama_obat = ?, harga = ?, tgl_expired = ?, stok = ?, satuan = ?
        WHERE id_obat = ?
        """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, nama_obat);
            preparedStatement.setDouble(2, harga);
            preparedStatement.setDate(3, (java.sql.Date) tgl_expired);
            preparedStatement.setInt(4, stok);
            preparedStatement.setString(5, satuan);
            preparedStatement.setString(6, id_obat); // pakai di WHERE

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Obat> ambilData() {
        List<Obat> daftarObat = new ArrayList<>();
        String query = "SELECT * FROM obat";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Obat obat = new Obat(
                        resultSet.getString("id_obat"),
                        resultSet.getString("nama_obat"),
                        resultSet.getDouble("harga"),
                        resultSet.getDate("tgl_expired"),
                        resultSet.getInt("stok"),
                        resultSet.getString("satuan")

                );
                daftarObat.add(obat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarObat;
    }

    public static List<String> ambilNamaObat() {
        List<String> daftarNamaObat = new ArrayList<>();
        String query = "SELECT nama_obat FROM obat";  // Query untuk mengambil nama obat dari tabel Obat

        try (Connection connection = MySQLConnection.getConnection(); // Pastikan MySQLConnection adalah kelas yang mengelola koneksi ke DB
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Ambil nama obat dari hasil query
            while (resultSet.next()) {
                String namaObat = resultSet.getString("nama_obat");
                daftarNamaObat.add(namaObat); // Menambahkan nama obat ke dalam list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarNamaObat;  // Mengembalikan daftar nama obat
    }

    public boolean updateStok(int jumlahTambah) {
        this.stok += jumlahTambah; // Update objek

        if(stok < 0) return false;

        String query = "UPDATE obat SET stok = ? WHERE id_obat = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setInt(1, this.stok);
            preparedStatement.setString(2, this.id_obat);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteObat() {
        String query = "DELETE FROM obat WHERE id_obat = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, this.id_obat);

            // Mengeksekusi perintah DELETE dan memeriksa apakah baris terhapus
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cekKetersediaan(){
        if(stok != 0) return true;
        return false;
    }

    public Double hitungHarga(int jumlah){
        return  jumlah * harga;
    }

    public String getIdObat() {
        return id_obat;
    }

    public String getNamaObat() {
        return nama_obat;
    }

    public Double getHarga() {
        return harga;
    }

    public Date getTglExpired() {
        return tgl_expired;
    }

    public Integer getStok() {
        return stok;
    }

    public String getSatuan() {
        return satuan;
    }
}
