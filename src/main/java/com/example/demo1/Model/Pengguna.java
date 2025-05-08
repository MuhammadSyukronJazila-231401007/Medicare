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
    private String id_pengguna;
    private String nama;
    private String username;
    private String password;
    private String no_hp;
    private String email;
    private String peran;
    private String jadwal = "kosong";

    public Pengguna(String id, String nama, String username, String password, String noHp, String email, String peran, String jadwal) {
        this.id_pengguna = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.no_hp = noHp;
        this.email = email;
        this.peran = peran;
        this.jadwal = jadwal;
    }

    public Pengguna(String id, String nama, String username, String password, String noHp, String email, String peran) {
        this.id_pengguna = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.no_hp = noHp;
        this.email = email;
        this.peran = peran;
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
            preparedStatement.setString(8, "kosong");
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
                Pengguna akun;
                if(peran.equals("Dokter")){
                    akun = new Dokter(
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
                }else{
                    akun = new Pengguna(
                            resultSet.getString("id_pengguna"),
                            resultSet.getString("nama"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("no_hp"),
                            resultSet.getString("email"),
                            peran,
                            resultSet.getString("jadwal")
                    );
                }
                daftarPengguna.add(akun);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPengguna;
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
            preparedStatement.setString(5, "kosong");
            preparedStatement.setString(6, this.jadwal);
            preparedStatement.setString(7, this.password);
            preparedStatement.setString(8, this.id_pengguna);  // Menggunakan username dari objek ini

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
}
