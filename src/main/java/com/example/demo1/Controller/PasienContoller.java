package com.example.demo1.Controller;
import com.example.demo1.Model.Pasien;
import com.example.demo1.PenggunaSekarang;
import com.example.demo1.Utils.SideBar;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class PasienContoller {

    @FXML private Label labelTambah;

    @FXML private Label labelCari;

    @FXML private Button pasienBtn;

    @FXML private Button pemeriksaanBtn;

    @FXML private Button inapBtn;

    @FXML private Button medisBtn;

    @FXML private Button obatBtn;

    @FXML private Button resepBtn;

    @FXML private Button pembayaranBtn;

    @FXML private Button ruanganBtn;

    @FXML private Button akunBtn;

    @FXML private Label username;

    @FXML private  Label peran;

    @FXML private TableView<Pasien> tabelPasien;
    @FXML private TableColumn<Pasien, String> colNama;
    @FXML private TableColumn<Pasien, String> colJK;
    @FXML private TableColumn<Pasien, Integer> colUmur;
    @FXML private TableColumn<Pasien, String> colAlamat;
    @FXML private TableColumn<Pasien, String> colTelp;
    @FXML private TableColumn<Pasien, String> colStatus;
    @FXML private TableColumn<Pasien, Void> colAksi;
    @FXML private TableColumn<Pasien, String> colNo;

    @FXML private Button tambah;

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

    private void isiTabel(){
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelPasien.getItems().indexOf(cellData.getValue()) + 1).asString());

        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJK.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colUmur.setCellValueFactory(new PropertyValueFactory<>("usia"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colTelp.setCellValueFactory(new PropertyValueFactory<>("noHp"));

        colStatus.setCellValueFactory(cellData -> {
            boolean rawatInap = cellData.getValue().rawat_inap;
            String status = rawatInap ? "Rawat Inap" : "Rawat Jalan";
            return new SimpleStringProperty(status);
        });

        // Kolom aksi dengan tombol
        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);
            {
                hbox.getChildren().addAll(btn, btnHapus); // tambahkan tombol ke HBox
                hbox.setAlignment(Pos.CENTER);

                btn.setOnAction(event -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    showEditDialog(pasien, getTableView());
                });

                btnHapus.setOnAction(event -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data pasien?");
                    alert.setContentText("Pasien: " + pasien.nama);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (pasien.deletePasien()) {
                            getTableView().getItems().remove(pasien);
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

        // Ambil data dan set ke table
        tabelPasien.setItems(FXCollections.observableArrayList(Pasien.ambilData()));

        setColumnCenter(colNama);
        setColumnCenter(colJK);
        setColumnCenter(colUmur);
        setColumnCenter(colAlamat);
        setColumnCenter(colTelp);
        setColumnCenter(colStatus);
        setColumnCenter(colNo);
    }

    private void tampilkanDialogTambah() {
        Dialog<Pasien> dialog = new Dialog<>();
        dialog.setTitle("Tambah Pasien");
        dialog.setHeaderText("Masukkan data pasien");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        // Komponen input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField();
        TextField noHpField = new TextField();
        TextField usiaField = new TextField();
        TextArea alamatField = new TextArea();
        TextArea keluhanField = new TextArea();
        alamatField.setPrefRowCount(2); // Tinggi area
        keluhanField.setPrefRowCount(2);
        TextField asuransiField = new TextField();

        ComboBox<String> jkCombo = new ComboBox<>(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        ComboBox<String> golDarahCombo = new ComboBox<>(FXCollections.observableArrayList("A", "B", "AB", "O"));
        CheckBox rawatInapBox = new CheckBox("Rawat Inap");

        grid.add(new Label("Nama:"), 0, 0); grid.add(namaField, 1, 0);
        grid.add(new Label("No HP:"), 0, 1); grid.add(noHpField, 1, 1);
        grid.add(new Label("Usia:"), 0, 2); grid.add(usiaField, 1, 2);
        grid.add(new Label("Alamat:"), 0, 3); grid.add(alamatField, 1, 3);
        grid.add(new Label("Keluhan:"), 0, 4); grid.add(keluhanField, 1, 4);
        grid.add(new Label("Asuransi:"), 0, 5); grid.add(asuransiField, 1, 5);
        grid.add(new Label("Jenis Kelamin:"), 0, 6); grid.add(jkCombo, 1, 6);
        grid.add(new Label("Golongan Darah:"), 0, 7); grid.add(golDarahCombo, 1, 7);
        grid.add(rawatInapBox, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Konversi hasil input jadi Pasien
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String id = UUID.randomUUID().toString(); // Bisa pakai format ID lain
                    return new Pasien(
                            id,
                            namaField.getText(),
                            noHpField.getText(),
                            Integer.parseInt(usiaField.getText()),
                            alamatField.getText(),
                            jkCombo.getValue(),
                            golDarahCombo.getValue(),
                            asuransiField.getText(),
                            keluhanField.getText(),
                            rawatInapBox.isSelected()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pasien> hasil = dialog.showAndWait();
        hasil.ifPresent(pasienBaru -> {
            boolean sukses = pasienBaru.tambahPasein();
            if (sukses) {
                tabelPasien.getItems().add(pasienBaru);
            } else {
                System.out.println("Gagal menambah pasien ke database.");
            }
        });
    }

    public void showEditDialog(Pasien pasien, TableView<Pasien> tableView) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Data Pasien");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField(pasien.nama);
        TextField noHpField = new TextField(pasien.no_hp);
        TextField usiaField = new TextField(String.valueOf(pasien.usia));
        TextField alamatField = new TextField(pasien.alamat);
        TextField keluhanField = new TextField(pasien.keluhan);
        ComboBox<String> jkCombo = new ComboBox<>(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        jkCombo.setValue(pasien.jenis_kelamin);
        TextField golDarahField = new TextField(pasien.gol_darah);
        TextField asuransiField = new TextField(pasien.asuransi);
        CheckBox rawatInapCheck = new CheckBox("Rawat Inap");
        rawatInapCheck.setSelected(pasien.rawat_inap);

        // Tambahkan field ke grid
        grid.add(new Label("Nama"), 0, 0); grid.add(namaField, 1, 0);
        grid.add(new Label("No HP"), 0, 1); grid.add(noHpField, 1, 1);
        grid.add(new Label("Usia"), 0, 2); grid.add(usiaField, 1, 2);
        grid.add(new Label("Alamat"), 0, 3); grid.add(alamatField, 1, 3);
        grid.add(new Label("Keluhan"), 0, 4); grid.add(keluhanField, 1, 4);
        grid.add(new Label("Jenis Kelamin"), 0, 5); grid.add(jkCombo, 1, 5);
        grid.add(new Label("Golongan Darah"), 0, 6); grid.add(golDarahField, 1, 6);
        grid.add(new Label("Asuransi"), 0, 7); grid.add(asuransiField, 1, 7);
        grid.add(rawatInapCheck, 1, 8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Update nilai objek
            pasien.nama = namaField.getText();
            pasien.no_hp = noHpField.getText();
            pasien.usia = Integer.parseInt(usiaField.getText());
            pasien.alamat = alamatField.getText();
            pasien.keluhan = keluhanField.getText();
            pasien.jenis_kelamin = jkCombo.getValue();
            pasien.gol_darah = golDarahField.getText();
            pasien.asuransi = asuransiField.getText();
            pasien.rawat_inap = rawatInapCheck.isSelected();

            // Simpan perubahan ke database
            if (pasien.editPasien()) {
                tableView.refresh(); // update tampilan
            }
        }
    }


}
