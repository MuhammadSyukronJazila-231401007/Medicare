package com.example.demo1.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JadwalChecker {

    // Fungsi untuk mengecek apakah waktu input berada dalam rentang waktu
    public static boolean isWaktuDalamRentang(String waktuInput, String waktuMulai, String waktuSelesai) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // Mengubah string waktu menjadi objek Date
        Date input = sdf.parse(waktuInput);
        Date mulai = sdf.parse(waktuMulai);
        Date selesai = sdf.parse(waktuSelesai);

        // Mengecek apakah waktu input berada dalam rentang waktu
        return (input.equals(mulai) || input.after(mulai)) && (input.equals(selesai) || input.before(selesai));
    }

    public static void main(String[] args) {
        try {
            // Input waktu dan rentang waktu
            String waktuInput = "11:00"; // waktu yang diinputkan
            String waktuMulai = "10:00"; // waktu mulai
            String waktuSelesai = "13:00"; // waktu selesai

            boolean result = isWaktuDalamRentang(waktuInput, waktuMulai, waktuSelesai);
            System.out.println(result);

        } catch (ParseException e) {
            System.out.println("Format waktu salah!");
        }
    }
}
