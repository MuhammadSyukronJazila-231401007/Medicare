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
}
