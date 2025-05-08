package com.example.demo1.Controller;

import com.example.demo1.Model.Obat;
import com.example.demo1.Model.Pasien;
import com.example.demo1.Model.Pengguna;
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
import com.example.demo1.Model.Resep;

import java.util.Optional;

public class ResepController {
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

    @FXML
    private TableView<Resep> tabelResep;

    @FXML
    private TableColumn<Resep, Integer> colNo;

    @FXML
    private TableColumn<Resep, String> colNoResep;

    @FXML
    private TableColumn<Resep, String> colTanggal;

    @FXML
    private TableColumn<Resep, String> colNamaPasien;

    @FXML
    private TableColumn<Resep, String> colDokter;

    @FXML
    private TableColumn<Resep, String> colObat;

    @FXML
    private TableColumn<Resep, Void> colAksi;

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
        tambah.setOnAction(e -> tampilkanDialogTambahResep());
    }

    private void isiTabel() {
        // Menampilkan nomor urut
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelResep.getItems().indexOf(cellData.getValue()) + 1));

        // Menghubungkan kolom dengan properti dari objek Resep
        colNoResep.setCellValueFactory(new PropertyValueFactory<>("noResep"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("dokter"));
        colObat.setCellValueFactory(new PropertyValueFactory<>("obat"));

        // Menambahkan penataan kolom (seperti tengah-tengah)
        Tabel.setColumnCenter(colNoResep);
        Tabel.setColumnCenter(colTanggal);
        Tabel.setColumnCenter(colNamaPasien);
        Tabel.setColumnCenter(colDokter);
        Tabel.setColumnCenter(colObat);
        Tabel.setColumnCenter(colNo);

        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnEdit, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                // Tombol Edit
                btnEdit.setOnAction(event -> {
                    Resep resep = getTableView().getItems().get(getIndex());
                    tampilkanDialogEditResep(resep, getTableView());
                });

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    Resep resep = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data resep?");
                    alert.setContentText("Resep: " + resep.getNoResep());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (resep.hapusResep()) {
                            getTableView().getItems().remove(resep);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data resep.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah Resep
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data resep ke dalam tabel
        tabelResep.setItems(FXCollections.observableArrayList(Resep.ambilDataResep()));
    }

    private void tampilkanDialogTambahResep() {
        Dialog<Resep> dialog = new Dialog<>();
        dialog.setTitle("Tambah Resep");
        dialog.setHeaderText("Masukkan data resep");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih pasien (diambil dari tabel pasien)
        ComboBox<String> pasienComboBox = new ComboBox<>();
        pasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien())); // Ambil nama pasien
        pasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih dokter (diambil dari pengguna dengan peran "Dokter")
        ComboBox<String> dokterComboBox = new ComboBox<>();
        dokterComboBox.setItems(FXCollections.observableArrayList(Pengguna.getNamaDokter())); // Ambil nama dokter
        dokterComboBox.setPromptText("Pilih Dokter");

        // ListView untuk memilih obat (multi-select)
        ListView<String> obatListView = new ListView<>();
        obatListView.setItems(FXCollections.observableArrayList(Obat.ambilNamaObat())); // Ambil nama obat dari tabel Obat
        obatListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        obatListView.setPrefHeight(150);  // Set height supaya bisa menampilkan banyak obat

        // Ambil tanggal saat ini sebagai string
        String tanggal = java.time.LocalDate.now().toString();

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Obat:"), 0, 2); grid.add(obatListView, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String noResep = Hash.generateUniqueNoResep();  // Menghasilkan No Resep unik 7 digit dari UUID
                    String namaPasien = pasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();

                    // Mendapatkan daftar obat yang dipilih (menjadi String dipisah koma)
                    String obat = String.join(", ", obatListView.getSelectionModel().getSelectedItems());

                    // Membuat objek Resep dengan data yang dimasukkan
                    return new Resep(noResep, tanggal, namaPasien, dokter, obat);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Resep> hasil = dialog.showAndWait();
        hasil.ifPresent(resepBaru -> {
            boolean sukses = resepBaru.tambahResep();
            if (sukses) {
                tabelResep.getItems().add(resepBaru); // Tambahkan resep baru ke tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan resep ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEditResep(Resep resep, TableView<Resep> tabelResep) {
        Dialog<Resep> dialog = new Dialog<>();
        dialog.setTitle("Edit Resep");
        dialog.setHeaderText("Edit data resep");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih pasien (diambil dari tabel pasien)
        ComboBox<String> pasienComboBox = new ComboBox<>();
        pasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien())); // Ambil nama pasien
        pasienComboBox.setValue(resep.getNamaPasien()); // Set nilai default sesuai data resep
        pasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih dokter (diambil dari pengguna dengan peran "Dokter")
        ComboBox<String> dokterComboBox = new ComboBox<>();
        dokterComboBox.setItems(FXCollections.observableArrayList(Pengguna.getNamaDokter())); // Ambil nama dokter
        dokterComboBox.setValue(resep.getDokter()); // Set nilai default sesuai data resep
        dokterComboBox.setPromptText("Pilih Dokter");

        // ListView untuk memilih obat (multi-select)
        ListView<String> obatListView = new ListView<>();
        obatListView.setItems(FXCollections.observableArrayList(Obat.ambilNamaObat())); // Ambil nama obat dari tabel Obat
        obatListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        obatListView.setPrefHeight(150);  // Set height supaya bisa menampilkan banyak obat

        // Set obat yang sudah dipilih sesuai data resep
        String[] obatArray = resep.getObat().split(", ");
        for (String obat : obatArray) {
            System.out.println(obat);
            if (obatListView.getItems().contains(obat)) {
                obatListView.getSelectionModel().select(obat);
            }
        }

        // Ambil tanggal saat ini sebagai string
        String tanggal = java.time.LocalDate.now().toString();

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Obat:"), 0, 2); grid.add(obatListView, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String noResep = resep.getNoResep();  // No Resep tetap sama
                    String namaPasien = pasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();

                    // Mendapatkan daftar obat yang dipilih (menjadi String dipisah koma)
                    String obat = String.join(", ", obatListView.getSelectionModel().getSelectedItems());

                    // Membuat objek Resep dengan data yang dimasukkan
                    return new Resep(noResep, tanggal, namaPasien, dokter, obat);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Resep> hasil = dialog.showAndWait();
        hasil.ifPresent(resepBaru -> {
            boolean sukses = resepBaru.editResep();  // Panggil fungsi untuk edit resep
            if (sukses) {
                tabelResep.getItems().set(tabelResep.getItems().indexOf(resep), resepBaru); // Update resep di tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal mengupdate resep di database.");
                gagal.showAndWait();
            }
        });
    }


}
