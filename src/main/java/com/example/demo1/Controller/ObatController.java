package com.example.demo1.Controller;

import com.example.demo1.Model.Obat;
import com.example.demo1.PenggunaSekarang;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class ObatController {
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
    private TableView<Obat> tabelObat;

    @FXML
    private ImageView logoutImage;

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        SideBar.logout(event);
    }

    // TableColumns
    @FXML
    private TableColumn<Obat, Integer> colNo;
    @FXML
    private TableColumn<Obat, String> colNamaObat;
    @FXML
    private TableColumn<Obat, Double> colHarga;
    @FXML
    private TableColumn<Obat, Integer> colStok;
    @FXML
    private TableColumn<Obat, String> colSatuan;
    @FXML
    private TableColumn<Obat, String> colKadaluarsa;
    @FXML
    private TableColumn<Obat, Void> colAksi;

    @FXML private Button tambah;

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
        tambah.setOnAction(e -> tampilkanDialogTambahObat());
    }

    private void isiTabel() {
        // Menampilkan nomor urut
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelObat.getItems().indexOf(cellData.getValue()) + 1));

        // Menghubungkan kolom dengan properti dari objek Obat
        colNamaObat.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        colKadaluarsa.setCellValueFactory(new PropertyValueFactory<>("tglExpired"));

        // Menambahkan penataan kolom (seperti tengah-tengah)
        Tabel.setColumnCenter(colNamaObat);
        Tabel.setColumnCenter(colHarga);
        Tabel.setColumnCenter(colStok);
        Tabel.setColumnCenter(colSatuan);
        Tabel.setColumnCenter(colKadaluarsa);
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
                    Obat obat = getTableView().getItems().get(getIndex());
                    tampilkanDialogEditObat(obat, getTableView());
                });

                // Tombol Hapus
                btnHapus.setOnAction(event -> {
                    Obat obat = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data obat?");
                    alert.setContentText("Obat: " + obat.getNamaObat());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (obat.deleteObat()) {
                            getTableView().getItems().remove(obat);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data obat.");
                            gagal.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); // Pastikan item yang diterima adalah Obat
                setGraphic(empty ? null : hbox);
            }
        });

        // Mengisi data obat ke dalam tabel
        tabelObat.setItems(FXCollections.observableArrayList(Obat.ambilData()));
    }

    private void tampilkanDialogTambahObat() {
        Dialog<Obat> dialog = new Dialog<>();
        dialog.setTitle("Tambah Obat");
        dialog.setHeaderText("Masukkan data obat");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Definisikan field input
        TextField namaObatField = new TextField();
        TextField hargaField = new TextField();
        TextField stokField = new TextField();
        TextField satuanField = new TextField();

        // DatePicker untuk Kadaluarsa
        DatePicker kadaluarsaPicker = new DatePicker();
        kadaluarsaPicker.setValue(LocalDate.now()); // Set default tanggal sebagai hari ini

        // Batasi agar hanya tanggal hari ini atau setelahnya yang bisa dipilih
        kadaluarsaPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Obat:"), 0, 0); grid.add(namaObatField, 1, 0);
        grid.add(new Label("Harga:"), 0, 1); grid.add(hargaField, 1, 1);
        grid.add(new Label("Stok:"), 0, 2); grid.add(stokField, 1, 2);
        grid.add(new Label("Satuan:"), 0, 3); grid.add(satuanField, 1, 3);
        grid.add(new Label("Kadaluarsa:"), 0, 4); grid.add(kadaluarsaPicker, 1, 4);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String idObat = UUID.randomUUID().toString();
                    double harga = Double.parseDouble(hargaField.getText());
                    int stok = Integer.parseInt(stokField.getText());
                    String satuan = satuanField.getText();

                    // Ambil tanggal kadaluarsa yang dipilih dari DatePicker
                    java.sql.Date kadaluarsa = java.sql.Date.valueOf(kadaluarsaPicker.getValue());

                    return new Obat(
                            idObat,
                            namaObatField.getText(),
                            harga,
                            kadaluarsa,
                            stok,
                            satuan
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Obat> hasil = dialog.showAndWait();
        hasil.ifPresent(obatBaru -> {
            boolean sukses = obatBaru.tambahObat();
            if (sukses) {
                tabelObat.getItems().add(obatBaru); // Tambahkan obat baru ke tabel
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan obat ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEditObat(Obat obat, TableView<Obat> table) {
        Dialog<Obat> dialog = new Dialog<>();
        dialog.setTitle("Edit Obat");
        dialog.setHeaderText("Ubah data obat");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Definisikan field input dengan nilai default dari obat yang dipilih
        TextField namaObatField = new TextField(obat.getNamaObat());
        TextField hargaField = new TextField(String.valueOf(obat.getHarga()));
        TextField stokField = new TextField(String.valueOf(obat.getStok()));
        TextField satuanField = new TextField(obat.getSatuan());

        // DatePicker untuk Kadaluarsa
        DatePicker kadaluarsaPicker = new DatePicker();

        // Batasi agar hanya tanggal hari ini atau setelahnya yang bisa dipilih
        kadaluarsaPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });


        // Mengonversi java.sql.Date menjadi LocalDate secara manual
        long epochMilli = obat.getTglExpired().getTime(); // Ambil waktu dalam milidetik
        Instant instant = Instant.ofEpochMilli(epochMilli); // Konversi ke Instant
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate(); // Konversi ke LocalDate
        kadaluarsaPicker.setValue(localDate); // Setkan LocalDate ke DatePicker

        // Menambahkan elemen ke dalam grid
        grid.add(new Label("Nama Obat:"), 0, 0); grid.add(namaObatField, 1, 0);
        grid.add(new Label("Harga:"), 0, 1); grid.add(hargaField, 1, 1);
        grid.add(new Label("Stok:"), 0, 2); grid.add(stokField, 1, 2);
        grid.add(new Label("Satuan:"), 0, 3); grid.add(satuanField, 1, 3);
        grid.add(new Label("Kadaluarsa:"), 0, 4); grid.add(kadaluarsaPicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    // Ambil tanggal kadaluarsa yang dipilih dari DatePicker
                    java.sql.Date kadaluarsa = java.sql.Date.valueOf(kadaluarsaPicker.getValue());

                    // Mengembalikan objek Obat yang baru dengan data yang telah diperbarui
                    return new Obat(
                            obat.getIdObat(), // id obat tidak berubah
                            namaObatField.getText(),
                            Double.parseDouble(hargaField.getText()),
                            kadaluarsa,
                            Integer.parseInt(stokField.getText()),
                            satuanField.getText()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Obat> hasil = dialog.showAndWait();
        hasil.ifPresent(obatBaru -> {
            boolean sukses = obatBaru.editObat(); // Fungsi update di class Obat
            if (sukses) {
                int index = table.getItems().indexOf(obat); // Menemukan index obat yang akan diperbarui
                table.getItems().set(index, obatBaru); // Update tabel dengan data yang sudah diperbarui
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Mengupdate");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal mengupdate data obat ke database.");
                gagal.showAndWait();
            }
        });
    }

}
