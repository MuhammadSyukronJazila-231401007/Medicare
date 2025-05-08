package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RawatInap {
    private String id_rawatInap;
    private String nama_pasien;
    private String nama_ruangan;
    private String tgl_masuk;  // Menggunakan String untuk tanggal
    private String tgl_keluar; // Menggunakan String untuk tanggal
    private String status;
    private boolean lunas; // Variabel untuk menyimpan status lunas

    // Konstruktor
    public RawatInap(String id_rawatInap, String nama_pasien, String nama_ruangan, String tgl_masuk, String tgl_keluar, String status, boolean lunas) {
        this.id_rawatInap = id_rawatInap;
        this.nama_pasien = nama_pasien;
        this.nama_ruangan = nama_ruangan;
        this.tgl_masuk = tgl_masuk;
        this.tgl_keluar = tgl_keluar;
        this.status = status;
        this.lunas = lunas;
    }

    // Konstruktor
    public RawatInap(String id_rawatInap, String nama_pasien, String nama_ruangan, String tgl_masuk, String tgl_keluar, String status) {
        this.id_rawatInap = id_rawatInap;
        this.nama_pasien = nama_pasien;
        this.nama_ruangan = nama_ruangan;
        this.tgl_masuk = tgl_masuk;
        this.tgl_keluar = tgl_keluar;
        this.status = status;
        this.lunas = false;
    }

    // Getter dan Setter untuk lunas
    public boolean isLunas() {
        return lunas;
    }

    public void setLunas(boolean lunas) {
        this.lunas = lunas;
    }
    // Tambah Rawat Inap
    public boolean tambahRawatInap() {
        String query = """
            INSERT INTO rawatinap (id_rawatinap, nama_pasien, nama_ruangan, tgl_masuk, tgl_keluar, status, lunas) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """; // Menyertakan kolom 'lunas'

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_rawatInap);
            preparedStatement.setString(2, nama_pasien);
            preparedStatement.setString(3, nama_ruangan);
            preparedStatement.setString(4, tgl_masuk);
            preparedStatement.setString(5, tgl_keluar);
            preparedStatement.setString(6, status);
            preparedStatement.setBoolean(7, lunas);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<RawatInap> ambilTunggakanPasien(String namaPasien) {
        List<RawatInap> tunggakan = new ArrayList<>();
        String query = "SELECT * FROM rawatinap WHERE nama_pasien = ? AND lunas = false"; // Lunas == false

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, namaPasien);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RawatInap rawatInap = new RawatInap(
                        resultSet.getString("id_rawatInap"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getString("tgl_masuk"),
                        resultSet.getString("tgl_keluar"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("lunas") // Mengambil status lunas
                );
                tunggakan.add(rawatInap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tunggakan;
    }

    public float hitungBiayaTotal() {
        if (!lunas) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime tglMasuk = LocalDateTime.parse(tgl_masuk, formatter);
            LocalDateTime tglKeluar = LocalDateTime.parse(tgl_keluar, formatter);

            long selisihHari = ChronoUnit.DAYS.between(tglMasuk.toLocalDate(), tglKeluar.toLocalDate());
            selisihHari = selisihHari == 0 ? 1 : selisihHari;

            return selisihHari * Kamar.getHargaKamar(nama_ruangan);
        }
        return 0;
    }

    // Ambil Data Rawat Inap
    public static List<RawatInap> ambilData() {
        List<RawatInap> daftarRawatInap = new ArrayList<>();
        String query = "SELECT * FROM rawatinap";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                RawatInap rawatInap = new RawatInap(
                        resultSet.getString("id_rawatInap"),
                        resultSet.getString("nama_pasien"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getString("tgl_masuk"),
                        resultSet.getString("tgl_keluar"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("lunas") // Mengambil status lunas
                );
                daftarRawatInap.add(rawatInap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarRawatInap;
    }

    // Edit Rawat Inap
    public boolean editRawatInap() {
        String query = """
        UPDATE rawatInap 
        SET nama_pasien = ?, nama_ruangan = ?, tgl_masuk = ?, tgl_keluar = ?, lunas = ?, status = ?
        WHERE id_rawatInap = ?
        """; // Menyertakan kolom 'lunas'

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nama_pasien);
            preparedStatement.setString(2, nama_ruangan);
            preparedStatement.setString(3, tgl_masuk);
            preparedStatement.setString(4, tgl_keluar);
            preparedStatement.setBoolean(5, lunas); // Menyimpan status lunas
            preparedStatement.setString(6, status);
            preparedStatement.setString(7, id_rawatInap);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hapus Rawat Inap
    public boolean hapusRawatInap() {
        String query = "DELETE FROM rawatInap WHERE id_rawatInap = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id_rawatInap);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getIdRawatInap() {
        return id_rawatInap;
    }

    public String getNamaPasien() {
        return nama_pasien;
    }

    public String getNamaRuangan() {
        return nama_ruangan;
    }

    public String getTglMasuk() {
        return tgl_masuk;
    }

    public String getTglKeluar() {
        return tgl_keluar;
    }

    public String getStatus() {
        return status;
    }

    public void updateLunas() {
        String query = """
        UPDATE rawatinap lunas = ?
        WHERE id_rawatInap = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, lunas);
            preparedStatement.setString(2, id_rawatInap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

