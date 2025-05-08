package com.example.demo1.Controller;

import com.example.demo1.Model.Pengguna;
import com.example.demo1.Utils.Hash;
import com.example.demo1.Utils.SideBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.UUID;

public class RegistrasiController {

    @FXML
    private ToggleButton loginToggleBtn;

    @FXML
    private ComboBox<String> peranComboBox;

    @FXML
    private TextField namaField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField noHPField;

    @FXML
    private PasswordField passwordField;

    public void initialize() {
        // Membuat daftar pilihan yang bisa dipilih di ComboBox
        ObservableList<String> peranList = FXCollections.observableArrayList(
                "Resepsionis", "Admin", "Dokter", "Apotker", "Kasir"
        );
        peranComboBox.setItems(peranList);
    }

    public void registrasiBtnOnAction(ActionEvent event){
        String nama = namaField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String noHp = noHPField.getText();
        String password = Hash.hash(passwordField.getText());
        String peran = peranComboBox.getValue();

        if(!nama.isBlank() && !email.isBlank() && !noHp.isBlank() && !password.isBlank() && !peran.isBlank()){
            Pengguna pengguna = new Pengguna(UUID.randomUUID().toString(), nama, username, password, noHp, email, peran);
            if(pengguna.tambahPengguna()){
                showPopup();
                clearField();
            }else{
                showPopupError("Gagal", "Gagal Registrasi. Coba lagi");
            }
        }else{
            showPopupError("Invalid Data","Data tidak boleh kosong!");
        }
    }

    public void loginToggleOnAction(ActionEvent event) {
        try {
            SideBar.ubahHalaman(loginToggleBtn, "login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Jenis alert: INFO, WARNING, ERROR, dll.
        alert.setTitle("Pemberitahuan");
        alert.setHeaderText("Berhasil");
        alert.setContentText("Berhasil Registrasi. Silahkan login");
        alert.showAndWait(); // Menampilkan popup dan menunggu user menutupnya
    }

    public void showPopupError(String header, String konten) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Jenis alert: INFO, WARNING, ERROR, dll.
        alert.setTitle("Pemberitahuan");
        alert.setHeaderText(header);
        alert.setContentText(konten);
        alert.showAndWait(); // Menampilkan popup dan menunggu user menutupnya
    }

    public void clearField(){
        namaField.clear();
        emailField.clear();
        usernameField.clear();
        noHPField.clear();
        passwordField.clear();
        peranComboBox.getSelectionModel().clearSelection();
    }

}
