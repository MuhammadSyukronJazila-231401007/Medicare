package com.example.demo1.Controller;

import com.example.demo1.MainApplication;
import com.example.demo1.Model.Pengguna;
import com.example.demo1.Utils.SideBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessage;

    @FXML
    private ToggleButton registrasiToggleBtn;

    public void loginBtnOnAction(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
//
//        String username = "syukron12";
//        String password = "admin123";

        if(!username.isBlank() && !password.isBlank()){
            if(Pengguna.verifLogin(username, password))
                SideBar.ubahHalaman(loginBtn, "Dashboard");
            else
                loginMessage.setText("Username atau Password salah");
        }else{
            loginMessage.setText("Username dan Password tidak boleh kosong!");
        }
    }

    public void registrasiToggleOnAction(ActionEvent event) {
        try {
            SideBar.ubahHalaman(registrasiToggleBtn, "registrasi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        if(Pengguna.verifLogin("syukron12","admin123")){
//            System.out.println("Login");
//        }else{
//            System.out.println("Slaah");
//        }
    }

//    public void showPopupCustom() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/popup.fxml"));
//            Parent root = loader.load();
//
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.APPLICATION_MODAL); // Mencegah interaksi dengan jendela lain
//            popupStage.setTitle("Popup Kustom");
//            popupStage.setScene(new Scene(root));
//            popupStage.showAndWait(); // Tunggu sampai popup ditutup
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void showInputDialogBawaan() {
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Input Dialog");
//        dialog.setHeaderText("Masukkan Nama Anda");
//        dialog.setContentText("Nama:");
//
//        Optional<String> result = dialog.showAndWait();
//        result.ifPresent(name -> System.out.println("Nama yang dimasukkan: " + name));
//    }
}
