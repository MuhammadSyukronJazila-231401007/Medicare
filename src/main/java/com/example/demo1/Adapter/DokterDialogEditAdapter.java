package com.example.demo1.Adapter;

import com.example.demo1.Model.Dokter;
import com.example.demo1.Model.Pengguna;
import com.example.demo1.Utils.Hash;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class DokterDialogEditAdapter implements DialogEditAdapter{
    @Override
    public void tampilkanDialogEdit(Pengguna pengguna, TableView<Pengguna> tableUser) {
        Dialog<Pengguna> dialog = new Dialog<>();
        dialog.setTitle("Edit Pengguna");
        dialog.setHeaderText("Perbarui data Dokter");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        // Komponen input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField(pengguna.getNama());
        TextField usernameField = new TextField(pengguna.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Kosongkan jika tidak ingin mengubah");

        TextField noHpField = new TextField(pengguna.getNoHp());
        TextField emailField = new TextField(pengguna.getEmail());
        ComboBox<String> peranCombo = new ComboBox<>(FXCollections.observableArrayList("Admin", "Dokter", "Apoteker", "Resepsionis", "Kasir"));
        peranCombo.setValue(pengguna.getPeran());

        TextField keahlianField = new TextField(((Dokter)pengguna).getKeahlian());
        TextField jadwalField = new TextField(pengguna.getJadwal());

        grid.add(new Label("Nama:"), 0, 0);       grid.add(namaField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);   grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);   grid.add(passwordField, 1, 2);
        grid.add(new Label("No HP:"), 0, 3);      grid.add(noHpField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);      grid.add(emailField, 1, 4);
        grid.add(new Label("Peran:"), 0, 5);      grid.add(peranCombo, 1, 5);
        grid.add(new Label("Keahlian:"), 0, 6);   grid.add(keahlianField, 1, 6);
        grid.add(new Label("Jadwal:"), 0, 7);     grid.add(jadwalField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String password = passwordField.getText().isEmpty() ? pengguna.getPassword() : Hash.hash(passwordField.getText());

                    Dokter penggunaBaru = new Dokter(
                            pengguna.getIdPengguna(), // tetap pakai ID lama
                            namaField.getText(),
                            usernameField.getText(),
                            password,
                            noHpField.getText(),
                            emailField.getText(),
                            peranCombo.getValue(),
                            jadwalField.getText(),
                            keahlianField.getText()
                    );
                    return penggunaBaru;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pengguna> hasil = dialog.showAndWait();
        hasil.ifPresent(penggunaBaru -> {
            boolean sukses = ((Dokter) penggunaBaru).editDokter();
            if (sukses) {
                int index = tableUser.getItems().indexOf(pengguna);
                tableUser.getItems().set(index, penggunaBaru);
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal memperbarui data pengguna di database.");
                gagal.showAndWait();
            }
        });
    }
}
