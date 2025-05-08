package com.example.demo1.Model;

import com.example.demo1.PenggunaSekarang;
import com.example.demo1.Utils.Hash;
import com.example.demo1.Utils.MySQLConnection;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Pengguna {
    public String id_pengguna;
    public String nama;
    public String username;
    public String password;
    public String no_hp;
    public String email;
    public String peran;
    private String jadwal;
    private String keahlian;

    public Pengguna(String id, String nama, String username, String password, String noHp, String email, String peran, String keahlian, String jadwal) {
        this.id_pengguna = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.no_hp = noHp;
        this.email = email;
        this.peran = peran;
        this.keahlian = keahlian;
        this.jadwal = jadwal;
    }

    // Constructor dengan default keahlian & jadwal
    public Pengguna(String id, String nama, String username, String password, String noHp, String email, String peran) {
        this(id, nama, username, password, noHp, email, peran, "kosong", "kosong");
    }

    public boolean deletePengguna() {
        String sql = "DELETE FROM pengguna WHERE id_pengguna = ?";

        try (Connection conn = MySQLConnection.getConnection();  // Ganti dengan class koneksi milikmu
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.id_pengguna);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tambahPengguna(){
        String query = """
            INSERT INTO pengguna (id_pengguna, nama, username, password, no_hp, email, peran, keahlian, jadwal) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_pengguna);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, no_hp);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, peran);
            preparedStatement.setString(8, keahlian);
            preparedStatement.setString(9, jadwal);

            return preparedStatement.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Pengguna> ambilData() {
        List<Pengguna> daftarPengguna = new ArrayList<>();
        String query = "SELECT * FROM pengguna";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String peran = resultSet.getString("peran");
                Dokter dokter = new Dokter(
                        resultSet.getString("id_pengguna"),
                        resultSet.getString("nama"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("no_hp"),
                        resultSet.getString("email"),
                        peran,
                        resultSet.getString("jadwal"),
                        resultSet.getString("keahlian")
                );
                daftarPengguna.add(dokter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPengguna;
    }

    public static boolean verifLogin(String username, String password) {
        String query = "SELECT id_pengguna, nama, username, no_hp, email, peran, jadwal, keahlian, password FROM pengguna WHERE username = ?";

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if(Hash.hash(password).equals(hashedPassword)){
                    PenggunaSekarang.tambahPengguna(resultSet.getString("id_pengguna"), resultSet.getString("nama"), resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("no_hp"), resultSet.getString("email"), resultSet.getString("peran"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean editPengguna() {
        String query = """
        UPDATE pengguna 
        SET nama = ?, no_hp = ?, email = ?, peran = ?, keahlian = ?, jadwal = ?, password = ?
        WHERE id_pengguna = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, this.nama);
            preparedStatement.setString(2, this.no_hp);
            preparedStatement.setString(3, this.email);
            preparedStatement.setString(4, this.peran);
            preparedStatement.setString(5, this.keahlian);
            preparedStatement.setString(6, this.jadwal);
            preparedStatement.setString(7, this.password);
            preparedStatement.setString(8, this.id_pengguna);  // Menggunakan username dari objek ini

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

    public String getIdPengguna() {
        return id_pengguna;
    }
    public String getNama() {
        return nama;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getNoHp() {
        return no_hp;
    }
    public String getEmail() {
        return email;
    }
    public String getPeran() {
        return peran;
    }
    public String getJadwal() {
        return jadwal;
    }
    public String getKeahlian() {
        return keahlian;
    }

}
