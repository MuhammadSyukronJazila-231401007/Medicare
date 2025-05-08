package com.example.demo1.Controller;

import com.example.demo1.Model.*;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PemeriksaanController {
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

    // Deklarasikan TableView dan TableColumn di controller menggunakan @FXML
    @FXML
    private TableView<Pemeriksaan> tabelPemeriksaan;

    @FXML
    private TableColumn<Pemeriksaan, Integer> colNo;

    @FXML
    private TableColumn<Pemeriksaan, String> colNamaPasien;

    @FXML
    private TableColumn<Pemeriksaan, String> colDokter;

    @FXML
    private TableColumn<Pemeriksaan, String> colJadwal;

    @FXML
    private TableColumn<Pemeriksaan, String> colRuangan;

    @FXML
    private TableColumn<Pemeriksaan, Double> colBiaya;

    @FXML
    private TableColumn<Pemeriksaan, Void> colAksi;

    @FXML private Label labelTambah;
    @FXML private Label labelCari;
    @FXML private Button tambah;

    @FXML
    private ImageView logoutImage;

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        SideBar.logout(event);
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
        tambah.setOnAction(e -> tampilkanDialogTambahPemeriksaan());
    }

    private void isiTabel() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelPemeriksaan.getItems().indexOf(cellData.getValue()) + 1));

        // Menghubungkan kolom dengan properti dari objek Pemeriksaan
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colJadwal.setCellValueFactory(new PropertyValueFactory<>("jadwal"));
        colRuangan.setCellValueFactory(new PropertyValueFactory<>("namaRuangan"));
        colBiaya.setCellValueFactory(new PropertyValueFactory<>("biaya"));

        // Mengatur agar kolom-kolom ini berada di tengah
        Tabel.setColumnCenter(colNamaPasien);
        Tabel.setColumnCenter(colDokter);
        Tabel.setColumnCenter(colJadwal);
        Tabel.setColumnCenter(colRuangan);
        Tabel.setColumnCenter(colBiaya);
        Tabel.setColumnCenter(colNo);

        // Aksi kolom (Edit dan Hapus)
        colAksi.setCellFactory(col -> new TableCell<Pemeriksaan, Void>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnEdit, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                // Tombol Edit
                btnEdit.setOnAction(event -> {
                    Pemeriksaan pemeriksaan = getTableView().getItems().get(getIndex());
                    tampilkanDialogEditPemeriksaan(pemeriksaan, tabelPemeriksaan);
                });

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    Pemeriksaan pemeriksaan = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data pemeriksaan?");
                    alert.setContentText("Pemeriksaan Pasien: " + pemeriksaan.getNamaPasien());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (pemeriksaan.hapusPemeriksaan()) {
                            getTableView().getItems().remove(pemeriksaan);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data pemeriksaan.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah Pemeriksaan
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data pemeriksaan ke dalam tabel
        tabelPemeriksaan.setItems(FXCollections.observableArrayList(Pemeriksaan.ambilData()));
    }

    private void tampilkanDialogTambahPemeriksaan() {
        Dialog<Pemeriksaan> dialog = new Dialog<>();
        dialog.setTitle("Tambah Pemeriksaan");
        dialog.setHeaderText("Masukkan data pemeriksaan");

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
        dokterComboBox.setItems(FXCollections.observableArrayList(Dokter.getNamaDokter())); // Ambil nama dokter
        dokterComboBox.setPromptText("Pilih Dokter");

        // ComboBox untuk memilih nama ruangan (yang jenis_ruangan = 'Ruang Pemeriksaan')
        ComboBox<String> ruanganComboBox = new ComboBox<>();
        ruanganComboBox.setItems(FXCollections.observableArrayList(Kamar.getRuanganPemeriksaan())); // Ambil nama ruangan pemeriksaan
        ruanganComboBox.setPromptText("Pilih Ruangan");

        // Input untuk memilih tanggal dan waktu pemeriksaan (pastikan tidak sebelum hari ini)
        DatePicker tanggalPemeriksaan = new DatePicker();
        tanggalPemeriksaan.setValue(LocalDate.now());
        tanggalPemeriksaan.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now())); // Disable tanggal sebelum hari ini
            }
        });

        TextField waktuPemeriksaan = new TextField();
        waktuPemeriksaan.setPromptText("HH:mm"); // Format waktu yang diinginkan, misalnya "14:30"

        // Input untuk memasukkan biaya pemeriksaan
        TextField biayaPemeriksaan = new TextField();
        biayaPemeriksaan.setPromptText("Biaya Pemeriksaan");

        // Ambil tanggal sekarang sebagai tgl_daftar
        String tglDaftar = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Nama Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Ruangan:"), 0, 2); grid.add(ruanganComboBox, 1, 2);
        grid.add(new Label("Tanggal Pemeriksaan:"), 0, 3); grid.add(tanggalPemeriksaan, 1, 3);
        grid.add(new Label("Waktu Pemeriksaan:"), 0, 4); grid.add(waktuPemeriksaan, 1, 4);
        grid.add(new Label("Biaya Pemeriksaan:"), 0, 5); grid.add(biayaPemeriksaan, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Ambil data yang diinputkan
                    String pasien = pasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();
                    String ruangan = ruanganComboBox.getValue();
                    LocalDate tanggal = tanggalPemeriksaan.getValue();
                    String waktu = waktuPemeriksaan.getText();
                    String jadwal = tanggal + " " + waktu;
                    Double biaya = Double.parseDouble(biayaPemeriksaan.getText()); // Ambil biaya dari TextField dan konversi ke Double

                    if (!Dokter.isJadwalDokterValid(dokter, waktu)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Jadwal Tidak Valid");
                        alert.setHeaderText(null);
                        alert.setContentText("Waktu pemeriksaan tidak sesuai dengan jadwal dokter.");
                        alert.showAndWait();
                        return null;
                    }

                    // Generate unique ID untuk Pemeriksaan
                    String idPemeriksaan = Hash.generateUniqueNoResep();

                    // Membuat objek Pemeriksaan dengan data yang dimasukkan
                    return new Pemeriksaan(idPemeriksaan, dokter, pasien, tglDaftar, jadwal, ruangan, biaya); // Menggunakan biaya yang diinputkan
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pemeriksaan> hasil = dialog.showAndWait();
        hasil.ifPresent(pemeriksaanBaru -> {
            boolean sukses = pemeriksaanBaru.tambahPemeriksaan(); // Menambahkan ke database
            if (sukses) {
                tabelPemeriksaan.getItems().add(pemeriksaanBaru); // Menambahkan ke dalam tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan data pemeriksaan ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEditPemeriksaan(Pemeriksaan pemeriksaanLama, TableView<Pemeriksaan> tabelPemeriksaan) {
        Dialog<Pemeriksaan> dialog = new Dialog<>();
        dialog.setTitle("Edit Pemeriksaan");
        dialog.setHeaderText("Edit data pemeriksaan");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih pasien
        ComboBox<String> pasienComboBox = new ComboBox<>();
        pasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien()));
        pasienComboBox.setValue(pemeriksaanLama.getNamaPasien());
        pasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih dokter
        ComboBox<String> dokterComboBox = new ComboBox<>();
        dokterComboBox.setItems(FXCollections.observableArrayList(Dokter.getNamaDokter()));
        dokterComboBox.setValue(pemeriksaanLama.getNamaDokter());
        dokterComboBox.setPromptText("Pilih Dokter");

        // ComboBox untuk memilih ruangan
        ComboBox<String> ruanganComboBox = new ComboBox<>();
        ruanganComboBox.setItems(FXCollections.observableArrayList(Kamar.getRuanganPemeriksaan()));
        ruanganComboBox.setValue(pemeriksaanLama.getNamaRuangan());
        ruanganComboBox.setPromptText("Pilih Ruangan");

        // DatePicker & waktu pemeriksaan
        DatePicker tanggalPemeriksaan = new DatePicker();
        String[] splitDateTime = pemeriksaanLama.getJadwal().split(" ");
        tanggalPemeriksaan.setValue(LocalDate.parse(splitDateTime[0]));

        TextField waktuPemeriksaan = new TextField(splitDateTime.length > 1 ? splitDateTime[1] : "");
        waktuPemeriksaan.setPromptText("HH:mm");

        // Biaya Pemeriksaan
        TextField biayaPemeriksaan = new TextField(String.valueOf(pemeriksaanLama.getBiaya()));
        biayaPemeriksaan.setPromptText("Biaya Pemeriksaan");

        // Tambahkan ke grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Nama Dokter:"), 0, 1); grid.add(dokterComboBox, 1, 1);
        grid.add(new Label("Ruangan:"), 0, 2); grid.add(ruanganComboBox, 1, 2);
        grid.add(new Label("Tanggal Pemeriksaan:"), 0, 3); grid.add(tanggalPemeriksaan, 1, 3);
        grid.add(new Label("Waktu Pemeriksaan:"), 0, 4); grid.add(waktuPemeriksaan, 1, 4);
        grid.add(new Label("Biaya Pemeriksaan:"), 0, 5); grid.add(biayaPemeriksaan, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String pasien = pasienComboBox.getValue();
                    String dokter = dokterComboBox.getValue();
                    String ruangan = ruanganComboBox.getValue();
                    LocalDate tanggal = tanggalPemeriksaan.getValue();
                    String waktu = waktuPemeriksaan.getText();
                    String jadwal = tanggal + " " + waktu;
                    Double biaya = Double.parseDouble(biayaPemeriksaan.getText());

                    if (!Dokter.isJadwalDokterValid(dokter, waktu)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Jadwal Tidak Valid");
                        alert.setHeaderText(null);
                        alert.setContentText("Waktu pemeriksaan tidak sesuai dengan jadwal dokter.");
                        alert.showAndWait();
                        return null;
                    }

                    return new Pemeriksaan(
                            pemeriksaanLama.getIdPemeriksaan(),
                            dokter,
                            pasien,
                            pemeriksaanLama.getTglDaftar(),
                            jadwal,
                            ruangan,
                            biaya
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pemeriksaan> hasil = dialog.showAndWait();
        hasil.ifPresent(pemeriksaanBaru -> {
            boolean sukses = pemeriksaanBaru.editPemeriksaan(); // panggil method update
            if (sukses) {
                int index = tabelPemeriksaan.getItems().indexOf(pemeriksaanLama);
                tabelPemeriksaan.getItems().set(index, pemeriksaanBaru);
                tabelPemeriksaan.refresh();
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal mengedit data pemeriksaan di database.");
                gagal.showAndWait();
            }
        });
    }


}
