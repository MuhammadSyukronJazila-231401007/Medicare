package com.example.demo1.Model;

import com.example.demo1.Utils.JadwalChecker;
import com.example.demo1.Utils.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class Dokter extends Pengguna {
    private String jadwal;
    private String keahlian;

    public String getJadwal() {
        return jadwal;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public Dokter(String id_pengguna, String nama, String username, String password, String no_hp, String email, String peran, String jadwal, String keahlian) {
        super(id_pengguna, nama, username, password, no_hp, email, peran);
        this.jadwal = jadwal;
        this.keahlian = keahlian;
    }

    public boolean tambahDokter(){
        String query = """
            INSERT INTO pengguna (id_pengguna, nama, username, password, no_hp, email, peran, jadwal, keahlian) 
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
            preparedStatement.setString(8, jadwal);
            preparedStatement.setString(9, keahlian);

            return preparedStatement.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cekJadwal(String jadwalDitanya){
        String[] waktu = jadwal.split("-");
        try {
            return JadwalChecker.isWaktuDalamRentang(jadwalDitanya, waktu[0], waktu[1]);
        }catch (ParseException e){
            e.printStackTrace();
            return false;
        }
    }
}
