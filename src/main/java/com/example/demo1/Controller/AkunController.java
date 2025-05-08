package com.example.demo1.Controller;

import com.example.demo1.Model.Dokter;
import com.example.demo1.Model.Pengguna;
import com.example.demo1.PenggunaSekarang;
import com.example.demo1.Utils.Hash;
import com.example.demo1.Utils.SideBar;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;
import java.util.UUID;

import static com.example.demo1.Utils.Tabel.setColumnCenter;

public class AkunController {
    @FXML
    private Label labelTambah;

    @FXML
    private Label labelCari;

    @FXML
    private Button pasienBtn;

    @FXML
    private Button pemeriksaanBtn;

    @FXML
    private Button inapBtn;

    @FXML
    private Button medisBtn;

    @FXML
    private Button obatBtn;

    @FXML
    private Button resepBtn;

    @FXML
    private Button pembayaranBtn;

    @FXML
    private Button ruanganBtn;

    @FXML
    private Button akunBtn;

    @FXML
    private Label username;

    @FXML
    private  Label peran;

    @FXML private TableView<Pengguna> tableUser;

    @FXML private TableColumn<Pengguna, String> colNo;
    @FXML private TableColumn<Pengguna, String> colNama;
    @FXML private TableColumn<Pengguna, String> colUsername;
    @FXML private TableColumn<Pengguna, String> colEmail;
    @FXML private TableColumn<Pengguna, String> colRole;
    @FXML private TableColumn<Pengguna, String> colJadwal;
    @FXML private TableColumn<Pengguna, Void> colAksi;

    @FXML Button tambah;

    @FXML
    private ImageView logoutImage;

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        SideBar.logout(event);
    }

    public void pasienBtnAction(ActionEvent event){
        SideBar.ubahHalaman(pasienBtn, "pasien");
    }

    public void pemeriksaanBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(pemeriksaanBtn, "Pemeriksaan");
    }

    public void inapBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(inapBtn, "RawatInap");
    }

    public void medisBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(medisBtn, "RekamMedis");
    }

    public void obatBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(obatBtn, "Obat");
    }

    public void resepBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(resepBtn, "Resep");
    }

    public void pembayaranBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(pembayaranBtn, "Pembayaran");
    }

    public void ruanganBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(ruanganBtn, "Ruangan");
    }

    public void akunBtnAction(ActionEvent event) {
        SideBar.ubahHalaman(akunBtn, "Akun");
    }

    @FXML
    public void initialize() {
        username.setText(PenggunaSekarang.penggunaSekarang.username);
        peran.setText(PenggunaSekarang.penggunaSekarang.peran);

        labelTambah.setGraphic(new FontIcon("fas-plus") {{
            setIconColor(Color.WHITE);
            setIconSize(13);
        }});
        labelCari.setGraphic(new FontIcon("fas-search") {{
            setIconColor(Color.GRAY);
            setIconSize(13);
        }});

        isiTabel();
        tambah.setOnAction(e -> tampilkanDialogTambah());
    }

    private void isiTabel() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tableUser.getItems().indexOf(cellData.getValue()) + 1).asString());

        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("peran"));
        colJadwal.setCellValueFactory(new PropertyValueFactory<>("jadwal"));

        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnEdit, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                btnEdit.setOnAction(event -> {
                    Pengguna pengguna = getTableView().getItems().get(getIndex());
                    tampilkanDialogEdit(pengguna);
                });

                btnHapus.setOnAction(event -> {
                    Pengguna pengguna = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data pengguna?");
                    alert.setContentText("Pengguna: " + pengguna.nama);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (pengguna.deletePengguna()) {
                            getTableView().getItems().remove(pengguna);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        // Ambil data dan set ke tabel
        tableUser.setItems(FXCollections.observableArrayList(Dokter.ambilData()));

        // Pusatkan teks di kolom
        setColumnCenter(colNama);
        setColumnCenter(colUsername);
        setColumnCenter(colEmail);
        setColumnCenter(colRole);
        setColumnCenter(colJadwal);
        setColumnCenter(colNo);
    }

    private void tampilkanDialogTambah() {
        Dialog<Pengguna> dialog = new Dialog<>();
        dialog.setTitle("Tambah Pengguna");
        dialog.setHeaderText("Masukkan data pengguna");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        // Komponen input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField noHpField = new TextField();
        TextField emailField = new TextField();
        ComboBox<String> peranCombo = new ComboBox<>(FXCollections.observableArrayList("Admin", "Dokter", "Apoteker", "Resepsionis", "Kasir"));
        TextField keahlianField = new TextField();
        TextField jadwalField = new TextField();
        jadwalField.setPromptText("HH:mm-HH:mm");
        

        grid.add(new Label("Nama:"), 0, 0);       grid.add(namaField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);   grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);   grid.add(passwordField, 1, 2);
        grid.add(new Label("No HP:"), 0, 3);      grid.add(noHpField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);      grid.add(emailField, 1, 4);
        grid.add(new Label("Peran:"), 0, 5);      grid.add(peranCombo, 1, 5);
        grid.add(new Label("Keahlian:"), 0, 6);      grid.add(keahlianField, 1, 6);
        grid.add(new Label("Jadwal:"), 0, 7);      grid.add(jadwalField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Konversi input ke objek Pengguna
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String id = UUID.randomUUID().toString();
                    return new Pengguna(
                            id,
                            namaField.getText(),
                            usernameField.getText(),
                            Hash.hash(passwordField.getText()),
                            noHpField.getText(),
                            emailField.getText(),
                            peranCombo.getValue(),
                            keahlianField.getText(),
                            jadwalField.getText()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pengguna> hasil = dialog.showAndWait();
        hasil.ifPresent(penggunaBaru -> {
            boolean sukses = penggunaBaru.tambahPengguna(); // pastikan method ini sudah dibuat
            if (sukses) {
                tableUser.getItems().add(penggunaBaru);
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan pengguna ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEdit(Pengguna pengguna) {
        Dialog<Pengguna> dialog = new Dialog<>();
        dialog.setTitle("Edit Pengguna");
        dialog.setHeaderText("Perbarui data pengguna");

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

        TextField keahlianField = new TextField(pengguna.getKeahlian());
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

                    Pengguna penggunaBaru = new Pengguna(
                            pengguna.getIdPengguna(), // tetap pakai ID lama
                            namaField.getText(),
                            usernameField.getText(),
                            password,
                            noHpField.getText(),
                            emailField.getText(),
                            peranCombo.getValue(),
                            keahlianField.getText(),
                            jadwalField.getText()
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
            boolean sukses = penggunaBaru.editPengguna(); // pastikan method update ada
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
