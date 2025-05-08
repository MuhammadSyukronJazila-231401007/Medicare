package com.example.demo1.Model;

import com.example.demo1.Utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ruangan {
    protected String id_ruangan;
    protected String nama_ruangan;
    protected Integer kapasitas;
    protected String jenis_ruangan;

    public String getIdRuangan() {
        return id_ruangan;
    }

    public String getNamaRuangan() {
        return nama_ruangan;
    }

    public Integer getKapasitas() {
        return kapasitas;
    }

    public String getJenisRuangan() {
        return jenis_ruangan;
    }



    public Ruangan(String id_ruangan, String nama_ruangan, Integer kapasitas, String jenis_ruangan) {
        this.id_ruangan = id_ruangan;
        this.nama_ruangan = nama_ruangan;
        this.kapasitas = kapasitas;
        this.jenis_ruangan = jenis_ruangan;
    }

    public boolean tambahRuangan(){
        String query = """
            INSERT INTO ruangan (id_ruangan, nama_ruangan, kapasitas, jenis_ruangan) 
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connectiondb = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connectiondb.prepareStatement(query)) {

            preparedStatement.setString(1, id_ruangan);
            preparedStatement.setString(2, nama_ruangan);
            preparedStatement.setInt(3, kapasitas);
            preparedStatement.setString(4, jenis_ruangan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Ruangan> ambilData() {
        List<Ruangan> daftarRuangan = new ArrayList<>();
        String query = "SELECT * FROM ruangan";

        try (Connection connectiondb = MySQLConnection.getConnection();
             Statement statement = connectiondb.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Ruangan ruangan = new Ruangan(
                        resultSet.getString("id_ruangan"),
                        resultSet.getString("nama_ruangan"),
                        resultSet.getInt("kapasitas"),
                        resultSet.getString("jenis_ruangan")
                );
                daftarRuangan.add(ruangan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarRuangan;
    }

    public boolean editRuangan(String namaBaru, Integer kapasitasbaru, String jenisBaru) {
        String query = """
        UPDATE ruangan 
        SET nama_ruangan = ?, kapasitas = ?, jenis_ruangan = ?
        WHERE id_ruangan = ?
        """;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, namaBaru);
            preparedStatement.setInt(2, kapasitasbaru);
            preparedStatement.setString(3, jenisBaru);
            preparedStatement.setString(4, id_ruangan);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
