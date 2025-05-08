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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PembayaranController {
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
    private TableView<Pembayaran> tabelPembayaran;

    // Deklarasi TableColumn dengan fx:id sesuai yang ada di FXML
    @FXML
    private TableColumn<Pembayaran, String> colNo;
    @FXML
    private TableColumn<Pembayaran, String> colNamaPasien;
    @FXML
    private TableColumn<Pembayaran, String> colTanggal;
    @FXML
    private TableColumn<Pembayaran, String> colJenisLayanan;
    @FXML
    private TableColumn<Pembayaran, String> colTotal;
    @FXML
    private TableColumn<Pembayaran, String> colMetode;
    @FXML
    private TableColumn<Pembayaran, String> colStatus;
    @FXML
    private TableColumn<Pembayaran, Void> colAksi;

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
        tambah.setOnAction(e -> tampilkanDialogTambahPembayaran());
    }

    private void isiTabel() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelPembayaran.getItems().indexOf(cellData.getValue()) + 1).asString());

        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalBayar"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metodePembayaran"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPembayaran"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colJenisLayanan.setCellValueFactory(new PropertyValueFactory<>("jenisLayanan"));

        Tabel.setColumnCenter(colNamaPasien);
        Tabel.setColumnCenter(colTotal);
        Tabel.setColumnCenter(colMetode);
        Tabel.setColumnCenter(colTanggal);
        Tabel.setColumnCenter(colStatus);
        Tabel.setColumnCenter(colNo);

        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btnHapus);
                hbox.setAlignment(Pos.CENTER);

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    Pembayaran pembayaran = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data pembayaran?");
                    alert.setContentText("Pembayaran ID: " + pembayaran.getIdPembayaran());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (pembayaran.deletePembayaran()) {
                            getTableView().getItems().remove(pembayaran);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data pembayaran.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah Pembayaran
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data pembayaran ke dalam tabel
        tabelPembayaran.setItems(FXCollections.observableArrayList(Pembayaran.ambilData()));
    }

    private void tampilkanDialogTambahPembayaran() {
        Dialog<Pembayaran> dialog = new Dialog<>();
        dialog.setTitle("Tambah Pembayaran");
        dialog.setHeaderText("Masukkan data pembayaran");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> namaPasienComboBox = new ComboBox<>();
        namaPasienComboBox.setItems(FXCollections.observableArrayList(Pasien.ambilNamaPasien()));
        namaPasienComboBox.setPromptText("Pilih Pasien");

        ComboBox<String> jenisLayananComboBox = new ComboBox<>();
        jenisLayananComboBox.setPromptText("Pilih Layanan");

        ComboBox<String> metodePembayaranComboBox = new ComboBox<>();
        metodePembayaranComboBox.setItems(FXCollections.observableArrayList("Tunai", "Transfer", "Kredit"));
        metodePembayaranComboBox.setPromptText("Pilih Metode Pembayaran");

        Label totalLabel = new Label("Total Biaya:");
        TextField totalField = new TextField();
        totalField.setEditable(false);

        // Update jenis layanan berdasarkan pasien
        namaPasienComboBox.setOnAction(event -> {
            String selectedPasien = namaPasienComboBox.getValue();
            if (selectedPasien != null) {
                List<String> tunggakan = getTunggakanPasien(selectedPasien);
                jenisLayananComboBox.setItems(FXCollections.observableArrayList(tunggakan));
                jenisLayananComboBox.getSelectionModel().clearSelection();
                totalField.clear();
            }
        });

        // Otomatis isi totalField saat memilih layanan
        jenisLayananComboBox.setOnAction(event -> {
            String layanan = jenisLayananComboBox.getValue();
            if (layanan != null) {
                int indexBiaya = layanan.lastIndexOf("Biaya:");
                if (indexBiaya != -1) {
                    String biayaStr = layanan.substring(indexBiaya + 6).trim().replace(",", "");
                    totalField.setText(biayaStr);
                }
            }
        });

        grid.add(new Label("Nama Pasien:"), 0, 0);
        grid.add(namaPasienComboBox, 1, 0);
        grid.add(new Label("Jenis Layanan:"), 0, 1);
        grid.add(jenisLayananComboBox, 1, 1);
        grid.add(new Label("Metode Pembayaran:"), 0, 2);
        grid.add(metodePembayaranComboBox, 1, 2);
        grid.add(totalLabel, 0, 3);
        grid.add(totalField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinWidth(800);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String idPembayaran = Hash.generateUniqueNoResep();
                    String namaPasien = namaPasienComboBox.getValue();
                    String jenisLayanan = jenisLayananComboBox.getValue();
                    String metodePembayaran = metodePembayaranComboBox.getValue();
                    String totalText = totalField.getText();

                    if (jenisLayanan == null || totalText.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Data Tidak Lengkap");
                        alert.setHeaderText(null);
                        alert.setContentText("Silakan pilih jenis layanan dan pastikan total biaya terisi.");
                        alert.showAndWait();
                        return null;
                    }

                    double biayaLayanan = 0.0;
                    try {
                        int indexBiaya = jenisLayanan.lastIndexOf("Biaya:");
                        if (indexBiaya != -1) {
                            String biayaStr = jenisLayanan.substring(indexBiaya + 6).trim().replace(",", "");
                            biayaLayanan = Double.parseDouble(biayaStr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Format Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Gagal membaca biaya dari jenis layanan.");
                        alert.showAndWait();
                        return null;
                    }

                    double totalBayar = Double.parseDouble(totalText);
                    if (totalBayar < biayaLayanan) {
                        Alert kurang = new Alert(Alert.AlertType.WARNING);
                        kurang.setTitle("Uang Tidak Cukup");
                        kurang.setHeaderText(null);
                        kurang.setContentText("Total biaya tidak mencukupi untuk membayar layanan tersebut.\n" +
                                "Biaya layanan: Rp" + biayaLayanan + "\n" +
                                "Uang yang tersedia: Rp" + totalBayar);
                        kurang.showAndWait();
                        return null;
                    }

                    String tanggalPembayaran = Date.valueOf(java.time.LocalDate.now()).toString();
                    String status = "Lunas";

                    return new Pembayaran(idPembayaran, namaPasien, totalBayar, metodePembayaran, tanggalPembayaran, status, jenisLayanan);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Pembayaran> hasil = dialog.showAndWait();
        hasil.ifPresent(pembayaranBaru -> {
            boolean sukses = pembayaranBaru.tambahPembayaran();
            if (sukses) {
                tabelPembayaran.getItems().add(pembayaranBaru);
                updateStatusLunas(pembayaranBaru.getNamaPasien(), pembayaranBaru.getJenisLayanan());
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan pembayaran ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void updateStatusLunas(String namaPasien, String jenisLayanan) {
        // Jika jenis layanan adalah pemeriksaan
        if (jenisLayanan.contains("Pemeriksaan")) {
            List<Pemeriksaan> pemeriksaanList = Pemeriksaan.ambilTunggakanPasien(namaPasien);
            for (Pemeriksaan pemeriksaan : pemeriksaanList) {
                if (!pemeriksaan.isLunas()) {
                    pemeriksaan.setLunas(true);  // Update status lunas menjadi true
                    pemeriksaan.updateLunas();  // Perbarui data pemeriksaan di database
                }
            }
        }

        // Jika jenis layanan adalah rawat inap
        if (jenisLayanan.contains("Rawat Inap")) {
            List<RawatInap> rawatInapList = RawatInap.ambilTunggakanPasien(namaPasien);
            for (RawatInap rawatInap : rawatInapList) {
                if (!rawatInap.isLunas()) {
                    rawatInap.setLunas(true);  // Update status lunas menjadi true
                    rawatInap.updateLunas();  // Perbarui data rawat inap di database
                }
            }
        }

        // Jika jenis layanan adalah resep
        if (jenisLayanan.contains("Resep")) {
            List<Resep> resepList = Resep.ambilTunggakanPasien(namaPasien);
            for (Resep resep : resepList) {
                if (!resep.isLunas()) {
                    resep.setLunas(true);  // Update status lunas menjadi true
                    resep.updateLunas();  // Perbarui data resep di database
                }
            }
        }
    }

    private List<String> getTunggakanPasien(String namaPasien) {
        List<String> tunggakan = new ArrayList<>();

        // Cek tunggakan pemeriksaan
        List<Pemeriksaan> pemeriksaanList = Pemeriksaan.ambilTunggakanPasien(namaPasien);
        for (Pemeriksaan pemeriksaan : pemeriksaanList) {
            if (!pemeriksaan.isLunas()) {
                tunggakan.add("Pemeriksaan - Tanggal: " + pemeriksaan.getTglDaftar() + ", Biaya: " + pemeriksaan.getBiaya());
            }
        }

        // Cek tunggakan rawat inap
        List<RawatInap> rawatInapList = RawatInap.ambilTunggakanPasien(namaPasien);
        for (RawatInap rawatInap : rawatInapList) {
            if (!rawatInap.isLunas()) {
                double biayaTotal = rawatInap.hitungBiayaTotal();
                tunggakan.add("Rawat Inap - Tanggal Masuk: " + rawatInap.getTglMasuk() + ", Tanggal Keluar: " + rawatInap.getTglKeluar() + ", Biaya: " + biayaTotal);
            }
        }

        // Cek tunggakan resep
        List<Resep> resepList = Resep.ambilTunggakanPasien(namaPasien);
        for (Resep resep : resepList) {
            if (!resep.isLunas()) {
                double biayaResep = resep.hitungBiayaObat();
                tunggakan.add("Resep - Tanggal: " + resep.getTanggal() + ", Biaya: " + biayaResep);
            }
        }

        // Jika tidak ada tunggakan, tampilkan info default
        if (tunggakan.isEmpty()) {
            tunggakan.add("Tidak ada tunggakan untuk pasien ini.");
        }

        return tunggakan;
    }

}
