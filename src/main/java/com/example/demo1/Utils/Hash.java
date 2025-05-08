package com.example.demo1.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.UUID;

public class Hash {
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Pilih algoritma hash
            byte[] hashBytes = digest.digest(input.getBytes());

            // Konversi byte ke format heksadesimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found", e);
        }
    }

    public static String generateUniqueNoResep() {
        // Menghasilkan UUID acak
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // Potong UUID menjadi 7 karakter pertama
        String noResep = uuid.substring(0, 7);

        return noResep;
    }


//    Random UUID:
//        UUID.randomUUID().toString();

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Masukkan teks: ");
//        String input = scanner.nextLine();
//        String hashed = hash(input);
//        System.out.println("Hash SHA-256: " + hashed);
//    }
}
