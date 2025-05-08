package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;
import javafx.collections.ObservableIntegerArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Kamar extends Ruangan {
    private String status_kamar;
    private String tipe_kamar;
    private Double biaya_permalam;

    public Kamar(String id_ruangan, String nama_ruangan, Integer kapasitas,
                 String jenis_ruangan, String tipe_kamar, Double biaya_permalam, String status_kamar ) {
        super(id_ruangan, nama_ruangan, kapasitas, jenis_ruangan);
        this.status_kamar = status_kamar;
        this.tipe_kamar = tipe_kamar;
        this.biaya_permalam = biaya_permalam;
    }

    public String getStatusKamar() {
        return status_kamar;
    }

    public String getTipeKamar() {
        return tipe_kamar;
    }

    public Double getBiayaPermalam() {
        return biaya_permalam;
    }

    public String getNamaRuangan(){
        return nama_ruangan;
    }

    public boolean deleteKamar() {
        String sql = "DELETE FROM kamar WHERE id_ruangan = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.id_ruangan); // pastikan 'id' adalah field dari objek Kamar
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tambahKamar(){
        String query = """
            INSERT INTO kamar (id_ruangan, status_kamar, tipe_kamar, biaya_permalam, nama_ruangan, kapasitas, jenis_ruangan) 
            VALUES (?, ?, ?, ?, ?, ? ,?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_ruangan);
            preparedStatement.setString(2, status_kamar);
            preparedStatement.setString(3, tipe_kamar);
            preparedStatement.setDouble(4, biaya_permalam);
            preparedStatement.setString(5, nama_ruangan);
            preparedStatement.setInt(6, kapasitas);
            preparedStatement.setString(7, jenis_ruangan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Kamar> ambilDataKamar() {
        List<Kamar> daftarKamar = new ArrayList<>();
        String query = "SELECT * FROM kamar";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Kamar kamar = new Kamar(
                        resultSet.getString("id_ruangan"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getInt("kapasitas"),
                        resultSet.getString("jenis_ruangan"),
                        resultSet.getString("tipe_kamar"),
                        resultSet.getDouble("biaya_permalam"),
                        resultSet.getString("status_kamar")
                );
                daftarKamar.add(kamar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKamar;
    }

    public boolean editKamar() {
        String query = """
        UPDATE kamar 
        SET status_kamar = ?, tipe_kamar = ?, biaya_permalam = ?, nama_ruangan = ?, kapasitas = ?, jenis_ruangan = ?
        WHERE id_ruangan = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status_kamar);
            preparedStatement.setString(2, tipe_kamar);
            preparedStatement.setDouble(3, biaya_permalam);
            preparedStatement.setString(4, nama_ruangan);
            preparedStatement.setInt(5, kapasitas);
            preparedStatement.setString(6, jenis_ruangan);
            preparedStatement.setString(7, id_ruangan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk mengambil nama ruangan dari tabel kamar dengan jenis_ruangan = 'Kamar Inap'
    public static List<String> getNamaRuanganKamarInap() {
        List<String> ruanganList = new ArrayList<>();
        String query = "SELECT nama_ruangan FROM kamar WHERE jenis_ruangan = 'Kamar Inap'";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                ruanganList.add(resultSet.getString("nama_ruangan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ruanganList;
    }

    public static List<String> getRuanganPemeriksaan() {
        List<String> daftarRuanganPemeriksaan = new ArrayList<>();
        String query = "SELECT nama_ruangan FROM kamar WHERE jenis_ruangan = 'Ruang Pemeriksaan'"; // Mengambil ruangan dengan jenis Pemeriksaan

        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Ambil nama ruangan pemeriksaan dari hasil query
            while (resultSet.next()) {
                String namaRuangan = resultSet.getString("nama_ruangan");
                daftarRuanganPemeriksaan.add(namaRuangan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarRuanganPemeriksaan;
    }

    public static float getHargaKamar(String namaKamar) {
        String query = "SELECT biaya_permalam FROM kamar WHERE nama_ruangan = ?";
        float biaya = 0;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, namaKamar);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                biaya = resultSet.getFloat("biaya_permalam");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return biaya;
    }
}

