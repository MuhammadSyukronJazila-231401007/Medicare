package com.example.demo1.Utils;

import com.example.demo1.PenggunaSekarang;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class SideBar {

    private static final HashMap<String, Set<String>> aksesHalaman = new HashMap<>();

    static {
        aksesHalaman.put("Admin", null); // null = akses semua
        aksesHalaman.put("Dokter", Set.of("Dashboard", "RekamMedis", "Resep"));
        aksesHalaman.put("Apoteker", Set.of("Dashboard","Obat", "Resep"));
        aksesHalaman.put("Resepsionis", Set.of("Dashboard","pasien", "Pemeriksaan", "RawatInap"));
        aksesHalaman.put("Kasir", Set.of("Dashboard", "Pembayaran"));
    }

    public static void ubahHalaman(Button btn, String ket) {
        gantiHalaman((Stage) btn.getScene().getWindow(), ket);
    }

    public static void ubahHalaman(ToggleButton btn, String ket) {
        gantiHalaman((Stage) btn.getScene().getWindow(), ket);
    }

    private static void gantiHalaman(Stage stage, String ket) {
        if (!punyaAkses(ket)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Akses Ditolak");
            alert.setHeaderText(null);
            alert.setContentText("Anda tidak memiliki akses untuk membuka halaman \"" + ket + "\".");
            alert.showAndWait();
            return;
        }

        int width = 900;
        int height = 500;

        if (ket.equals("login") || ket.equals("registrasi")) {
            width = 555;
            height = 316;
        }

        try {
            FXMLLoader loader = new FXMLLoader(SideBar.class.getResource("/com/example/demo1/FXML/" + ket + ".fxml"));
            loader.load();
            Parent root = loader.getRoot();
            stage.setScene(new Scene(root, width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean punyaAkses(String halaman) {
        if (halaman.equals("login") || halaman.equals("registrasi")) {
            return true;
        }

        String peran = PenggunaSekarang.penggunaSekarang.getPeran();
        Set<String> daftarHalaman = aksesHalaman.get(peran);

        return daftarHalaman == null || daftarHalaman.contains(halaman);
    }

    public static void logout(MouseEvent event) {
        // Tampilkan dialog konfirmasi
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Jika pengguna memilih OK, maka logout

            // Kosongkan data pengguna
            PenggunaSekarang.penggunaSekarang = null;

            // Ambil stage dari event
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Arahkan ke halaman login
            gantiHalaman(stage, "login");
        }
    }
}
