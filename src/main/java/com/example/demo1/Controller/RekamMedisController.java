package com.example.demo1.Controller;

import com.example.demo1.Model.Dokter;
import com.example.demo1.Model.Pasien;
import com.example.demo1.Model.Pengguna;
import com.example.demo1.Model.RekamMedis;
import com.example.demo1.PenggunaSekarang;
import com.example.demo1.Utils.Hash;
import com.example.demo1.Utils.SideBar;
import com.example.demo1.Utils.Tabel;
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

public class RekamMedisController {
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

    @FXML private TableColumn<RekamMedis, String> colNo;
    @FXML private TableColumn<RekamMedis, String> colNamaPasien;
    @FXML private TableColumn<RekamMedis, String> colTanggal;
    @FXML private TableColumn<RekamMedis, String> colDokter;
    @FXML private TableColumn<RekamMedis, String> colDiagnosa;
    @FXML private TableColumn<RekamMedis, String> colCatatan;
    @FXML private TableColumn<RekamMedis, Void> colAksi;

    @FXML private TableView<RekamMedis> tabelMedis;
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
        username.setText(PenggunaSekarang.penggunaSekarang.getUsername());
        peran.setText(PenggunaSekarang.penggunaSekarang.getPeran());

        labelTambah.setGraphic(new FontIcon("fas-plus") {{
            setIconColor(Color.WHITE);
            setIconSize(13);
        }});
        labelCari.setGraphic(new FontIcon("fas-search") {{
            setIconColor(Color.GRAY);
            setIconSize(13);
        }});

        isiTabel();
        tambah.setOnAction(e -> tampilkanDialogTambahRekamMedis());
    }

    private void isiTabel() {
        // Menampilkan nomor urut
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelMedis.getItems().indexOf(cellData.getValue()) + 1).asString());

        // Menghubungkan kolom dengan properti dari objek RekamMedis
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("dokter"));
        colDiagnosa.setCellValueFactory(new PropertyValueFactory<>("diagnosa"));
        colCatatan.setCellValueFactory(new PropertyValueFactory<>("catatan"));

        // Menambahkan penataan kolom (seperti tengah-tengah)
        Tabel.setColumnCenter(colNamaPasien);
        Tabel.setColumnCenter(colTanggal);
        Tabel.setColumnCenter(colDokter);
        Tabel.setColumnCenter(colDiagnosa);
        Tabel.setColumnCenter(colCatatan);
        Tabel.setColumnCenter(colNo);

        // Aksi kolom (Edit dan Hapus)
        colAksi.setCellFactory(col -> new TableCell<RekamMedis, Void>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnEdit, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                // Tombol Edit
                btnEdit.setOnAction(event -> {
                    RekamMedis rekamMedis = getTableView().getItems().get(getIndex());
                    tampilkanDialogEditRekamMedis(rekamMedis, getTableView());
                });

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    RekamMedis rekamMedis = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data rekam medis?");
                    alert.setContentText("Rekam Medis Pasien: " + rekamMedis.getNamaPasien());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (rekamMedis.hapusRekamMedis()) {
                            getTableView().getItems().remove(rekamMedis);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data rekam medis.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah RekamMedis
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data rekam medis ke dalam tabel
        tabelMedis.setItems(FXCollections.observableArrayList(RekamMedis.ambilData()));
    }

    private void tampilkanDialogTambahRekamMedis() {
        Dialog<RekamMedis> dialog = new Dialog<>();
        dialog.setTitle("Tambah Rekam Medis");
        dialog.setHeaderText("Masukkan data rekam medis");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih Nama Pasien dari tabel Pasien
        ComboBox<String> namaPasienComboBox = new ComboBox<>();
        namaPasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien())); // Ambil nama pasien dari tabel
        namaPasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih Nama Dokter dari tabel Dokter
        ComboBox<String> dokterComboBox = new ComboBox<>();
        dokterComboBox.setItems(FXCollections.observableArrayList(Dokter.getNamaDokter())); // Ambil nama dokter dari pengguna dengan peran "Dokter"
        dokterComboBox.setPromptText("Pilih Dokter");

        // Input field untuk Diagnosa
        TextField diagnosaField = new TextField();

        // Ganti TextField dengan TextArea untuk catatan
        TextArea catatanField = new TextArea();
        catatanField.setWrapText(true);  // Memungkinkan teks dibungkus ke baris berikutnya
        catatanField.setPrefRowCount(3);  // Menetapkan tinggi yang lebih besar agar lebih mudah dibaca
        catatanField.setPrefColumnCount(25); // Menetapkan lebar kolom sesuai keinginan

        // Ambil tanggal saat ini sebagai string
        String tanggal = java.time.LocalDate.now().toString();

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(namaPasienComboBox, 1, 0);
        grid.add(new Label("Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Diagnosa:"), 0, 2); grid.add(diagnosaField, 1, 2);
        grid.add(new Label("Catatan:"), 0, 3); grid.add(catatanField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Generate unique No Rekam Medis
                    String noRekamMedis = Hash.generateUniqueNoResep();  // Menghasilkan No Rekam Medis unik
                    String namaPasien = namaPasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();
                    String diagnosa = diagnosaField.getText();
                    String catatan = catatanField.getText();

                    // Membuat objek RekamMedis dengan data yang dimasukkan
                    return new RekamMedis(noRekamMedis, dokter, namaPasien, tanggal, diagnosa, catatan);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<RekamMedis> hasil = dialog.showAndWait();
        hasil.ifPresent(rekamMedisBaru -> {
            boolean sukses = rekamMedisBaru.tambahRekamMedis(); // Menambahkan ke database
            if (sukses) {
                tabelMedis.getItems().add(rekamMedisBaru); // Menambahkan ke dalam tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan rekam medis ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEditRekamMedis(RekamMedis rekamMedis, TableView<RekamMedis> tabelMedis) {
        Dialog<RekamMedis> dialog = new Dialog<>();
        dialog.setTitle("Edit Rekam Medis");
        dialog.setHeaderText("Edit data rekam medis");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih Nama Pasien dari tabel Pasien
        ComboBox<String> namaPasienComboBox = new ComboBox<>();
        namaPasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien())); // Ambil nama pasien dari tabel
        namaPasienComboBox.setValue(rekamMedis.getNamaPasien()); // Set nilai default sesuai data rekam medis
        namaPasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih Nama Dokter dari tabel Dokter
        ComboBox<String> dokterComboBox = new ComboBox<>();
        dokterComboBox.setItems(FXCollections.observableArrayList(Dokter.getNamaDokter())); // Ambil nama dokter dari pengguna dengan peran "Dokter"
        dokterComboBox.setValue(rekamMedis.getDokter()); // Set nilai default sesuai data rekam medis
        dokterComboBox.setPromptText("Pilih Dokter");

        // Input field untuk Diagnosa
        TextField diagnosaField = new TextField();
        diagnosaField.setText(rekamMedis.getDiagnosa());  // Set nilai default sesuai data rekam medis

        // Ganti TextField dengan TextArea untuk catatan
        TextArea catatanField = new TextArea();
        catatanField.setWrapText(true);  // Memungkinkan teks dibungkus ke baris berikutnya
        catatanField.setPrefRowCount(3);  // Menetapkan tinggi yang lebih besar agar lebih mudah dibaca
        catatanField.setPrefColumnCount(25); // Menetapkan lebar kolom sesuai keinginan
        catatanField.setText(rekamMedis.getCatatan());  // Set nilai default sesuai data rekam medis

        // Tanggal tidak bisa diedit, jadi tidak perlu menambahkannya di sini

        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(namaPasienComboBox, 1, 0);
        grid.add(new Label("Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Diagnosa:"), 0, 2); grid.add(diagnosaField, 1, 2);
        grid.add(new Label("Catatan:"), 0, 3); grid.add(catatanField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Tetap menggunakan noRekamMedis yang ada karena tidak boleh diubah
                    String noRekamMedis = rekamMedis.getNoRekamMedis();
                    String namaPasien = namaPasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();
                    String diagnosa = diagnosaField.getText();
                    String catatan = catatanField.getText();

                    // Membuat objek RekamMedis dengan data yang dimasukkan
                    return new RekamMedis(noRekamMedis, dokter, namaPasien, java.sql.Date.valueOf(java.time.LocalDate.now()).toString(), diagnosa, catatan);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<RekamMedis> hasil = dialog.showAndWait();
        hasil.ifPresent(rekamMedisBaru -> {
            boolean sukses = rekamMedisBaru.editRekamMedis();
            if (sukses) {
                int index = tabelMedis.getItems().indexOf(rekamMedis);
                tabelMedis.getItems().set(index, rekamMedisBaru); // Update data di tabel
                tabelMedis.refresh(); // Refresh tabel untuk menampilkan data yang sudah diubah
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal mengupdate rekam medis di database.");
                gagal.showAndWait();
            }
        });
    }

}
