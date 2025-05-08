package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;
import javafx.scene.control.IndexRange;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Resep {
    private String noResep;
    private String tanggal;
    private String namaPasien;
    private String dokter;
    private String obat;
    private boolean lunas;

    // Constructor
    public Resep(String noResep, String tanggal, String namaPasien, String dokter, String obat) {
        this.noResep = noResep;
        this.tanggal = tanggal;
        this.namaPasien = namaPasien;
        this.dokter = dokter;
        this.obat = obat;
        this.lunas = false;
    }

    public Resep(String noResep, String tanggal, String namaPasien, String dokter, String obat, boolean lunas) {
        this.noResep = noResep;
        this.tanggal = tanggal;
        this.namaPasien = namaPasien;
        this.dokter = dokter;
        this.obat = obat;
        this.lunas = lunas;
    }

    // Getter and Setter methods for table columns
    public String getNoResep() {
        return noResep;
    }

    public void setNoResep(String noResep) {
        this.noResep = noResep;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getDokter() {
        return dokter;
    }

    public void setDokter(String dokter) {
        this.dokter = dokter;
    }

    public String getObat() {
        return obat;
    }

    public void setObat(String obat) {
        this.obat = obat;
    }

    public static List<Resep> ambilDataResep() {
        List<Resep> daftarResep = new ArrayList<>();
        String query = "SELECT no_resep, tanggal, nama_pasien, dokter, obat FROM resep";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Resep resep = new Resep(
                        resultSet.getString("no_resep"),
                        resultSet.getString("tanggal"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getString("dokter"),
                        resultSet.getString("obat")
                );
                daftarResep.add(resep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarResep;
    }

    public boolean tambahResep() {
        String query = """
        INSERT INTO resep (no_resep, tanggal, nama_pasien, dokter, obat)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, noResep);
            preparedStatement.setString(2, tanggal);
            preparedStatement.setString(3, namaPasien);
            preparedStatement.setString(4, dokter);
            preparedStatement.setString(5, obat);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editResep() {
        String query = """
        UPDATE resep 
        SET tanggal = ?, nama_pasien = ?, dokter = ?, obat = ?
        WHERE no_resep = ?
    """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, tanggal);
            preparedStatement.setString(2, namaPasien);
            preparedStatement.setString(3, dokter);
            preparedStatement.setString(4, obat);
            preparedStatement.setString(5, noResep);  // Gunakan no_resep sebagai identifier

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusResep() {
        String query = """
        DELETE FROM resep WHERE no_resep = ?
    """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, noResep);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Resep> ambilTunggakanPasien(String namaPasien) {
        List<Resep> tunggakan = new ArrayList<>();
        String query = "SELECT * FROM resep WHERE nama_pasien = ? AND lunas = false"; // Lunas == false

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, namaPasien);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Resep resep = new Resep(
                        resultSet.getString("no_resep"),
                        resultSet.getString("tanggal"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getString("dokter"),
                        resultSet.getString("obat"),
                        resultSet.getBoolean("lunas")
                );
                tunggakan.add(resep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tunggakan;
    }

    // Getter dan Setter untuk lunas
    public boolean isLunas() {
        return lunas;
    }

    public void setLunas(boolean lunas) {
        this.lunas = lunas;
    }

    public void updateLunas() {
        String query = """
        UPDATE resep lunas = ?
        WHERE no_resep = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, lunas);
            preparedStatement.setString(2, noResep);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int hitungBiayaObat() {
        int totalHarga = 0;

        // Pisahkan nama-nama obat berdasarkan koma
        String[] daftarObat = obat.split(",");

        String query = "SELECT harga FROM obat WHERE nama_obat = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (String namaObat : daftarObat) {
                namaObat = namaObat.trim(); // Hilangkan spasi ekstra

                preparedStatement.setString(1, namaObat);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalHarga += resultSet.getInt("harga");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalHarga;
    }

}

