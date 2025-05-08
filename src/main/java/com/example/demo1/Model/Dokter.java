package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Dokter extends Pengguna {
    private String keahlian;

    public Dokter(String id_pengguna, String nama, String username, String password, String no_hp, String email, String peran, String jadwal, String keahlian) {
        super(id_pengguna, nama, username, password, no_hp, email, peran, jadwal);
        this.keahlian = keahlian;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public boolean tambahDokter(){
        String query = """
            INSERT INTO pengguna (id_pengguna, nama, username, password, no_hp, email, peran, jadwal, keahlian)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, getIdPengguna());
            preparedStatement.setString(2, getNama());
            preparedStatement.setString(3, getUsername());
            preparedStatement.setString(4, getPassword());
            preparedStatement.setString(5, getNoHp());
            preparedStatement.setString(6, getEmail());
            preparedStatement.setString(7, getPeran());
            preparedStatement.setString(8, getJadwal());
            preparedStatement.setString(9, keahlian);

            return preparedStatement.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editDokter() {
        String query = """
        UPDATE pengguna 
        SET nama = ?, no_hp = ?, email = ?, peran = ?, keahlian = ?, jadwal = ?, password = ?
        WHERE id_pengguna = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, this.getNama());
            preparedStatement.setString(2, this.getNoHp());
            preparedStatement.setString(3, this.getEmail());
            preparedStatement.setString(4, this.getPeran());
            preparedStatement.setString(5, this.getKeahlian());
            preparedStatement.setString(6, this.getJadwal());
            preparedStatement.setString(7, this.getPassword());
            preparedStatement.setString(8, this.getIdPengguna());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getNamaDokter() {
        List<String> daftarNamaDokter = new ArrayList<>();
        String query = "SELECT nama FROM pengguna WHERE peran = 'Dokter'"; // Mengambil nama pengguna dengan peran Dokter

        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Ambil nama dokter dari hasil query
            while (resultSet.next()) {
                String namaDokter = resultSet.getString("nama");
                daftarNamaDokter.add(namaDokter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarNamaDokter;
    }

    public static String getJadwalDokter(String namaDokter) {
        String jadwalDokter = null;
        String query = "SELECT jadwal FROM pengguna WHERE nama = ? AND peran = 'Dokter'"; // Query untuk mengambil jadwal dokter

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameter nama dokter
            statement.setString(1, namaDokter);

            // Eksekusi query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Ambil jadwal dokter dari hasil query
                if (resultSet.next()) {
                    jadwalDokter = resultSet.getString("jadwal");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jadwalDokter; // Kembalikan jadwal dokter, atau null jika tidak ditemukan
    }

    public static boolean isJadwalDokterValid(String namaDokter, String waktuPemeriksaan) {
        String jadwalDokter = getJadwalDokter(namaDokter);
        try {
            String[] jadwal = jadwalDokter.split("-");
            String jadwalMulai = jadwal[0];
            String jadwalSelesai = jadwal[1];

            // Mengubah waktu menjadi format LocalTime
            LocalTime waktuMulai = LocalTime.parse(jadwalMulai);
            LocalTime waktuSelesai = LocalTime.parse(jadwalSelesai);
            LocalTime waktuPemeriksaanTime = LocalTime.parse(waktuPemeriksaan);

            // Cek apakah waktu pemeriksaan berada dalam rentang waktu dokter
            return !waktuPemeriksaanTime.isBefore(waktuMulai) && !waktuPemeriksaanTime.isAfter(waktuSelesai);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Jika format waktu tidak valid
        }
    }
}
