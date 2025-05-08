package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RekamMedis {
    private String noRekamMedis;  // Menggunakan noRekamMedis sebagai ID unik
    private String namaPasien;
    private String tanggal;
    private String dokter;
    private String diagnosa;
    private String catatan;

    // Constructor untuk RekamMedis
    public RekamMedis(String noRekamMedis, String dokter, String namaPasien, String tanggal, String diagnosa, String catatan) {
        this.noRekamMedis = noRekamMedis;
        this.dokter = dokter;
        this.namaPasien = namaPasien;
        this.tanggal = tanggal;
        this.diagnosa = diagnosa;
        this.catatan = catatan;
    }

    // Getter dan Setter
    public String getNoRekamMedis() {
        return noRekamMedis;
    }

    public void setNoRekamMedis(String noRekamMedis) {
        this.noRekamMedis = noRekamMedis;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDokter() {
        return dokter;
    }

    public void setDokter(String dokter) {
        this.dokter = dokter;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        this.diagnosa = diagnosa;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    // Fungsi untuk menambah Rekam Medis ke Database
    public boolean tambahRekamMedis() {
        String query = """
            INSERT INTO rekamMedis (no_rekamMedis, nama_pasien, tanggal, dokter, diagnosa, catatan) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, noRekamMedis);
            preparedStatement.setString(2, namaPasien);
            preparedStatement.setString(3, tanggal);
            preparedStatement.setString(4, dokter);
            preparedStatement.setString(5, diagnosa);
            preparedStatement.setString(6, catatan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fungsi untuk mengambil data Rekam Medis
    public static List<RekamMedis> ambilData() {
        List<RekamMedis> daftarRekamMedis = new ArrayList<>();
        String query = "SELECT * FROM rekamMedis";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                RekamMedis rekamMedis = new RekamMedis(
                        resultSet.getString("no_rekamMedis"),
                        resultSet.getString("dokter"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getString("tanggal"),
                        resultSet.getString("diagnosa"),
                        resultSet.getString("catatan")
                );
                daftarRekamMedis.add(rekamMedis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarRekamMedis;
    }

    // Fungsi untuk mengedit Rekam Medis
    public boolean editRekamMedis() {
        String query = """
            UPDATE rekamMedis 
            SET nama_pasien = ?, dokter = ?, tanggal = ?, diagnosa = ?, catatan = ?
            WHERE no_rekamMedis = ?
        """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, namaPasien);
            preparedStatement.setString(2, dokter);
            preparedStatement.setString(3, tanggal);
            preparedStatement.setString(4, diagnosa);
            preparedStatement.setString(5, catatan);
            preparedStatement.setString(6, noRekamMedis);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk menghapus Rekam Medis
    public boolean hapusRekamMedis() {
        String query = "DELETE FROM rekamMedis WHERE no_rekamMedis = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, noRekamMedis);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
