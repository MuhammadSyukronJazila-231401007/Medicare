package com.example.demo1.Controller;

import com.example.demo1.Model.Kamar;
import com.example.demo1.Model.Pasien;
import com.example.demo1.Model.RawatInap;
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

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.demo1.Model.Pasien.getPasienRawatInap;

public class RawatInapController {
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

    // Variabel untuk TableView
    @FXML
    private TableView<RawatInap> tabelRawat;

    // Variabel untuk kolom-kolom tabel
    @FXML
    private TableColumn<RawatInap, String> colNo;
    @FXML
    private TableColumn<RawatInap, String> colNamaPasien;
    @FXML
    private TableColumn<RawatInap, String> colTanggalMasuk;
    @FXML
    private TableColumn<RawatInap, String> colTanggalKeluar;
    @FXML
    private TableColumn<RawatInap, String> colRuangan;
    @FXML
    private TableColumn<RawatInap, String> colStatus;
    @FXML
    private TableColumn<RawatInap, Void> colAksi;

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
        tambah.setOnAction(e -> tampilkanDialogTambahRawatInap());
    }

    private void isiTabel() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelRawat.getItems().indexOf(cellData.getValue()) + 1).asString());

        // Menghubungkan kolom dengan properti dari objek RawatInap
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colTanggalMasuk.setCellValueFactory(new PropertyValueFactory<>("tglMasuk"));
        colTanggalKeluar.setCellValueFactory(new PropertyValueFactory<>("tglKeluar"));
        colRuangan.setCellValueFactory(new PropertyValueFactory<>("namaRuangan"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        Tabel.setColumnCenter(colNamaPasien);
        Tabel.setColumnCenter(colTanggalMasuk);
        Tabel.setColumnCenter(colTanggalKeluar);
        Tabel.setColumnCenter(colRuangan);
        Tabel.setColumnCenter(colStatus);
        Tabel.setColumnCenter(colNo);

        // Aksi kolom (Edit dan Hapus)
        colAksi.setCellFactory(col -> new TableCell<RawatInap, Void>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnEdit, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                // Tombol Edit
                btnEdit.setOnAction(event -> {
                    RawatInap rawatInap = getTableView().getItems().get(getIndex());
                    tampilkanDialogEditRawatInap(rawatInap, tabelRawat);
                });

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    RawatInap rawatInap = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data rawat inap?");
                    alert.setContentText("Rawat Inap Pasien: " + rawatInap.getNamaPasien());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (rawatInap.hapusRawatInap()) {
                            getTableView().getItems().remove(rawatInap);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data rawat inap.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah RawatInap
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data rawat inap ke dalam tabel
        tabelRawat.setItems(FXCollections.observableArrayList(RawatInap.ambilData()));
    }

    private void tampilkanDialogTambahRawatInap() {
        Dialog<RawatInap> dialog = new Dialog<>();
        dialog.setTitle("Tambah Rawat Inap");
        dialog.setHeaderText("Masukkan data rawat inap");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih pasien yang sedang rawat inap
        ComboBox<String> pasienComboBox = new ComboBox<>();
        pasienComboBox.setItems(FXCollections.observableArrayList(getPasienRawatInap()));
        pasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih nama ruangan dari tabel kamar
        ComboBox<String> ruanganComboBox = new ComboBox<>();
        ruanganComboBox.setItems(FXCollections.observableArrayList(Kamar.getNamaRuanganKamarInap()));
        ruanganComboBox.setPromptText("Pilih Ruangan");

        // ComboBox untuk memilih status
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Rawat Inap", "Sudah Keluar");
        statusComboBox.setValue("Rawat Inap");

        // Ambil tanggal saat ini dengan format lengkap (tanggal + jam)
        String tglMasuk = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        final String[] tglKeluar = {""};

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0); grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Ruangan:"), 0, 1); grid.add(ruanganComboBox, 1, 1);
        grid.add(new Label("Status:"), 0, 2); grid.add(statusComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Ambil data yang diinputkan
                    String pasien = pasienComboBox.getValue();
                    String ruangan = ruanganComboBox.getValue();
                    String status = statusComboBox.getValue();

                    // Tentukan tanggal keluar jika status "Sudah Keluar"
                    if (status.equals("Sudah Keluar")) {
                        tglKeluar[0] = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    // Generate unique ID untuk Rawat Inap
                    String idRawatInap = Hash.generateUniqueNoResep(); // Sesuaikan dengan logika untuk generate ID

                    // Membuat objek RawatInap dengan data yang dimasukkan
                    return new RawatInap(idRawatInap, pasien, ruangan, tglMasuk, tglKeluar[0], status);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<RawatInap> hasil = dialog.showAndWait();
        hasil.ifPresent(rawatInapBaru -> {
            boolean sukses = rawatInapBaru.tambahRawatInap(); // Menambahkan ke database
            if (sukses) {
                Pasien.updateStatusPasien(rawatInapBaru);
                tabelRawat.getItems().add(rawatInapBaru); // Menambahkan ke dalam tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan data rawat inap ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEditRawatInap(RawatInap rawatInapLama, TableView<RawatInap> tabelRawat) {
        Dialog<RawatInap> dialog = new Dialog<>();
        dialog.setTitle("Edit Rawat Inap");
        dialog.setHeaderText("Edit data rawat inap");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox untuk memilih pasien yang sedang rawat inap (rawat_inap = 1)
        ComboBox<String> pasienComboBox = new ComboBox<>();
        pasienComboBox.setItems(FXCollections.observableArrayList(getPasienRawatInap()));  // Ambil pasien yang sedang rawat inap
        pasienComboBox.setValue(rawatInapLama.getNamaPasien());  // Set nilai pasien yang sedang di-edit
        pasienComboBox.setPromptText("Pilih Pasien");

        // ComboBox untuk memilih nama ruangan dari tabel kamar
        ComboBox<String> ruanganComboBox = new ComboBox<>();
        ruanganComboBox.setItems(FXCollections.observableArrayList(Kamar.getNamaRuanganKamarInap()));  // Ambil nama ruangan kamar inap
        ruanganComboBox.setValue(rawatInapLama.getNamaRuangan());  // Set nilai ruangan yang sedang di-edit
        ruanganComboBox.setPromptText("Pilih Ruangan");

        // ComboBox untuk memilih status
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Rawat Inap", "Sudah Keluar");
        statusComboBox.setValue(rawatInapLama.getStatus());  // Set nilai status yang sedang di-edit

        // Ambil tanggal saat ini dengan format lengkap (tanggal + jam) sebagai default untuk tgl_keluar
        String tglMasuk = rawatInapLama.getTglMasuk();  // Ambil nilai tgl_masuk yang sudah ada
        final String[] tglKeluar = {rawatInapLama.getTglKeluar()};  // Ambil nilai tgl_keluar yang sudah ada atau kosong jika belum keluar

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Pasien:"), 0, 0);
        grid.add(pasienComboBox, 1, 0);
        grid.add(new Label("Ruangan:"), 0, 1);
        grid.add(ruanganComboBox, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statusComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Ambil data yang diinputkan
                    String pasien = pasienComboBox.getValue();
                    String ruangan = ruanganComboBox.getValue();
                    String status = statusComboBox.getValue();

                    // Tentukan tanggal keluar jika status "Sudah Keluar"
                    if (status.equals("Sudah Keluar") && tglKeluar[0].isEmpty()) {
                        tglKeluar[0] = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    // Membuat objek RawatInap dengan data yang dimasukkan
                    return new RawatInap(rawatInapLama.getIdRawatInap(), pasien, ruangan, tglMasuk, tglKeluar[0], status);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<RawatInap> hasil = dialog.showAndWait();
        hasil.ifPresent(rawatInapBaru -> {
            boolean sukses = rawatInapBaru.editRawatInap();  // Panggil method untuk update data di database
            if (sukses) {
                // Jika status sudah berubah menjadi "Sudah Keluar", update status rawat_inap pasien ke 0
                if ("Sudah Keluar".equals(rawatInapBaru.getStatus())) {
                    Pasien.updateStatusPasien(rawatInapBaru); // Update status rawat_inap pada tabel pasien menjadi 0
                }

                int index = tabelRawat.getItems().indexOf(rawatInapLama);
                tabelRawat.getItems().set(index, rawatInapBaru); // Update data di tabel
                tabelRawat.refresh(); // Refresh tabel untuk menampilkan data yang sudah diubah

            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal mengedit data rawat inap di database.");
                gagal.showAndWait();
            }
        });
    }
}
