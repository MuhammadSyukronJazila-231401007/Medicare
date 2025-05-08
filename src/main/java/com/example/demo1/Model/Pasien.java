package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pasien {
    public String id_pasien;
    public String nama;
    public String no_hp;
    public Integer usia;
    public String alamat;
    public String jenis_kelamin;
    public String gol_darah;
    public String asuransi;
    public String keluhan;
    public boolean rawat_inap;

    public Pasien(String id_pasien, String nama, String no_hp, Integer usia, String alamat, String jenis_kelamin, String gol_darah, String asuransi, String keluhan, boolean rawat_inap) {
        this.id_pasien = id_pasien;
        this.nama = nama;
        this.no_hp = no_hp;
        this.usia = usia;
        this.alamat = alamat;
        this.jenis_kelamin = jenis_kelamin;
        this.gol_darah = gol_darah;
        this.asuransi = asuransi;
        this.keluhan = keluhan;
        this.rawat_inap = rawat_inap;
    }

    public boolean tambahPasein(){
        String query = """
            INSERT INTO pasien (id_pasien, nama, no_hp, usia, alamat, jenis_kelamin, gol_darah, asuransi, keluhan, rawat_inap) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_pasien);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, no_hp);
            preparedStatement.setInt(4, usia);
            preparedStatement.setString(5, alamat);
            preparedStatement.setString(6, jenis_kelamin);
            preparedStatement.setString(7, gol_darah);
            preparedStatement.setString(8, asuransi);
            preparedStatement.setString(9, keluhan);
            preparedStatement.setBoolean(10, rawat_inap);


            return preparedStatement.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            e.printStackTrace();  // Nampilin error di terminal
            return false;
        }
    }

    public String getIdPasien() {
        return id_pasien;
    }

    public String getNama() {
        return nama;
    }

    public String getNoHp() {
        return no_hp;
    }

    public Integer getUsia() {
        return usia;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getJenisKelamin() {
        return jenis_kelamin;
    }

    public String getGolDarah() {
        return gol_darah;
    }

    public String getAsuransi() {
        return asuransi;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public boolean isRawatInap() {
        return rawat_inap;
    }

    public static List<Pasien> ambilData() {
        List<Pasien> daftarPasien = new ArrayList<>();
        String query = "SELECT * FROM pasien";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Pasien pasien = new Pasien(
                        resultSet.getString("id_pasien"),
                        resultSet.getString("nama"),
                        resultSet.getString("no_hp"),
                        resultSet.getInt("usia"),
                        resultSet.getString("alamat"),
                        resultSet.getString("jenis_kelamin"),
                        resultSet.getString("gol_darah"),
                        resultSet.getString("asuransi"),
                        resultSet.getString("keluhan"),
                        resultSet.getBoolean("rawat_inap")
                );
                daftarPasien.add(pasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPasien;
    }

    public boolean editPasien() {
        String query = """
        UPDATE pasien 
        SET nama = ?, no_hp = ?, usia = ?, alamat = ?, jenis_kelamin = ?, gol_darah = ?, asuransi = ?, keluhan = ?, rawat_inap = ?
        WHERE id_pasien = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, this.nama);
            preparedStatement.setString(2, this.no_hp);
            preparedStatement.setInt(3, this.usia);
            preparedStatement.setString(4, this.alamat);
            preparedStatement.setString(5, this.jenis_kelamin);
            preparedStatement.setString(6, this.gol_darah);
            preparedStatement.setString(7, this.asuransi);
            preparedStatement.setString(8, this.keluhan);
            preparedStatement.setBoolean(9, this.rawat_inap);
            preparedStatement.setString(10, this.id_pasien);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePasien() {
        String query = "DELETE FROM pasien WHERE id_pasien = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, this.id_pasien);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk mengambil pasien yang sedang rawat inap
    public static List<String> getPasienRawatInap() {
        List<String> pasienList = new ArrayList<>();
        String query = "SELECT nama FROM pasien WHERE rawat_inap = 1";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                pasienList.add(resultSet.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pasienList;
    }

    // Fungsi untuk update status pasien saat keluar
    public static void updateStatusPasien(RawatInap rawatInap) {
        String query = "UPDATE pasien SET rawat_inap = 0 WHERE nama = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, rawatInap.getNamaPasien());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> ambilNamaPasien() {
        List<String> daftarNamaPasien = new ArrayList<>();
        String query = "SELECT nama FROM pasien"; // Sesuaikan dengan nama kolom yang tepat di tabel pasien

        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Ambil nama pasien dari hasil query
            while (resultSet.next()) {
                String namaPasien = resultSet.getString("nama");
                daftarNamaPasien.add(namaPasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarNamaPasien;
    }

    public boolean cekAsuransi() {
//        String asuransiCek
        // Daftar asuransi kesehatan yang populer di Indonesia
        List<String> asuransiKesehatan = Arrays.asList(
                "BPJS",
                "Prudential",
                "Allianz",
                "AXA Mandiri",
                "Cigna",
                "Manulife",
                "FWD",
                "BNI Life",
                "Generali",
                "Tugu Insurance"
        );
        return asuransiKesehatan.contains(asuransi);
    }

}
