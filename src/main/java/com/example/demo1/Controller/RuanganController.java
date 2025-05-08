package com.example.demo1.Controller;

import com.example.demo1.Model.Kamar;
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

import java.util.Optional;
import java.util.UUID;

public class RuanganController {
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
    private Label peran;

    @FXML private TableView<Kamar> tabelRuangan;
    @FXML private TableColumn<Kamar,Integer> colNo;
    @FXML private TableColumn<Kamar,String> colNama;
    @FXML private TableColumn<Kamar,String> colTipe;
    @FXML private TableColumn<Kamar,Integer> colKapasitas;
    @FXML private TableColumn<Kamar,Double> colHarga;
    @FXML private TableColumn<Kamar,String> colStatus;
    @FXML private TableColumn<Kamar,Void> colAksi;
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
        tambah.setOnAction(e -> tampilkanDialogTambahKamar());
    }

    private void isiTabel() {
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tabelRuangan.getItems().indexOf(cellData.getValue()) + 1));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaRuangan"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipeKamar"));
        colKapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("biayaPermalam"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKamar"));

        Tabel.setColumnCenter(colNama);
        Tabel.setColumnCenter(colTipe);
        Tabel.setColumnCenter(colKapasitas);
        Tabel.setColumnCenter(colHarga);
        Tabel.setColumnCenter(colStatus);
        Tabel.setColumnCenter(colNo);

        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox hbox = new HBox(5);

            {
                hbox.getChildren().addAll(btn, btnHapus);
                hbox.setAlignment(Pos.CENTER);

                btn.setOnAction(event -> {
                    Kamar kamar = getTableView().getItems().get(getIndex());
                    tampilkanDialogEdit(kamar, getTableView());
                });

                btnHapus.setOnAction(event -> {
                    Kamar kamar = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus data kamar?");
                    alert.setContentText("Ruangan: " + kamar.getNamaRuangan());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (kamar.deleteKamar()) {
                            getTableView().getItems().remove(kamar);
                            getTableView().refresh();
                        } else {
                            Alert gagal = new Alert(Alert.AlertType.ERROR);
                            gagal.setTitle("Gagal Menghapus");
                            gagal.setHeaderText(null);
                            gagal.setContentText("Terjadi kesalahan saat menghapus data kamar.");
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

        tabelRuangan.setItems(FXCollections.observableArrayList(Kamar.ambilDataKamar()));
    }

    private void tampilkanDialogTambahKamar() {
        Dialog<Kamar> dialog = new Dialog<>();
        dialog.setTitle("Tambah Kamar");
        dialog.setHeaderText("Masukkan data kamar");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField();
        TextField kapasitasField = new TextField();
        TextField biayaField = new TextField();

        ComboBox<String> jenisCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Kamar Inap", "Ruang Pemeriksaan", "IGD"
        ));

        ComboBox<String> tipeCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Kelas I", "Kelas II", "VIP"
        ));

        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Tersedia", "Terisi"
        ));

        jenisCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Kamar Inap".equals(newVal)) {
                biayaField.setDisable(false);
                biayaField.clear();
                tipeCombo.setDisable(false);
                tipeCombo.getSelectionModel().clearSelection();
            } else {
                biayaField.setDisable(true);
                biayaField.setText("0.0");
                tipeCombo.setDisable(true);
                tipeCombo.setValue("Tidak Berlaku");
            }
        });


        grid.add(new Label("Nama Ruangan:"), 0, 0); grid.add(namaField, 1, 0);
        grid.add(new Label("Kapasitas:"), 0, 1); grid.add(kapasitasField, 1, 1);
        grid.add(new Label("Biaya per Malam:"), 0, 2); grid.add(biayaField, 1, 2);
        grid.add(new Label("Jenis Ruangan:"), 0, 3); grid.add(jenisCombo, 1, 3);
        grid.add(new Label("Tipe Kamar:"), 0, 4); grid.add(tipeCombo, 1, 4);
        grid.add(new Label("Status Kamar:"), 0, 5); grid.add(statusCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String id = UUID.randomUUID().toString();
                    String tipe = "Kamar Inap".equals(jenisCombo.getValue()) ? tipeCombo.getValue() : "Tidak Berlaku";
                    double biaya = "Kamar Inap".equals(jenisCombo.getValue()) ? Double.parseDouble(biayaField.getText()) : 0.0;

                    return new Kamar(
                            id,
                            namaField.getText(),
                            Integer.parseInt(kapasitasField.getText()),
                            jenisCombo.getValue(),
                            tipe,
                            biaya,
                            statusCombo.getValue()
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Kamar> hasil = dialog.showAndWait();
        hasil.ifPresent(kamarBaru -> {
            boolean sukses = kamarBaru.tambahKamar();
            if (sukses) {
                tabelRuangan.getItems().add(kamarBaru);
            } else {
                Alert gagal = new Alert(Alert.AlertType.ERROR);
                gagal.setTitle("Gagal Menyimpan");
                gagal.setHeaderText(null);
                gagal.setContentText("Gagal menambahkan kamar ke database.");
                gagal.showAndWait();
            }
        });
    }

    private void tampilkanDialogEdit(Kamar kamar, TableView<Kamar> table) {
        Dialog<Kamar> dialog = new Dialog<>();
        dialog.setTitle("Edit Kamar");
        dialog.setHeaderText("Ubah data kamar");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField(kamar.getNamaRuangan());
        TextField kapasitasField = new TextField(String.valueOf(kamar.getKapasitas()));
        TextField biayaField = new TextField(String.valueOf(kamar.getBiayaPermalam()));

        ComboBox<String> jenisCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Kamar Inap", "Ruang Pemeriksaan", "IGD"
        ));
        jenisCombo.setValue(kamar.getJenisRuangan());

        ComboBox<String> tipeCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Standar", "VIP", "VVIP"
        ));
        tipeCombo.setValue(kamar.getTipeKamar());

        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Tersedia", "Terisi", "Dalam Perbaikan"
        ));
        statusCombo.setValue(kamar.getStatusKamar());

        // Atur field biaya & tipe jika bukan kamar inap
        if (!"Kamar Inap".equals(kamar.getJenisRuangan())) {
            biayaField.setDisable(true);
            tipeCombo.setDisable(true);
        }

        jenisCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Kamar Inap".equals(newVal)) {
                biayaField.setDisable(false);
                biayaField.clear();
                tipeCombo.setDisable(false);
                tipeCombo.getSelectionModel().clearSelection();
            } else {
                biayaField.setDisable(true);
                biayaField.setText("0.0");
                tipeCombo.setDisable(true);
                tipeCombo.setValue("Tidak Berlaku");
            }
        });

        grid.add(new Label("Nama Ruangan:"), 0, 0); grid.add(namaField, 1, 0);
        grid.add(new Label("Kapasitas:"), 0, 1); grid.add(kapasitasField, 1, 1);
        grid.add(new Label("Jenis Ruangan:"), 0, 2); grid.add(jenisCombo, 1, 2);
        grid.add(new Label("Tipe Kamar:"), 0, 3); grid.add(tipeCombo, 1, 3);
        grid.add(new Label("Biaya per Malam:"), 0, 4); grid.add(biayaField, 1, 4);
        grid.add(new Label("Status:"), 0, 5); grid.add(statusCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                try {
                    String tipe = "Kamar Inap".equals(jenisCombo.getValue()) ? tipeCombo.getValue() : "Tidak Berlaku";
                    double biaya = "Kamar Inap".equals(jenisCombo.getValue()) ? Double.parseDouble(biayaField.getText()) : 0.0;

                    return new Kamar(
                            kamar.getIdRuangan(), // ambil id dari data lama
                            namaField.getText(),
                            Integer.parseInt(kapasitasField.getText()),
                            jenisCombo.getValue(),
                            tipe,
                            biaya,
                            statusCombo.getValue()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });


        Optional<Kamar> hasil = dialog.showAndWait();
        hasil.ifPresent(kamarBaru -> {
            boolean sukses = kamarBaru.editKamar(); // Fungsi update di class Kamar
            if (sukses) {
                int index = tabelRuangan.getItems().indexOf(kamar);
                tabelRuangan.getItems().set(index, kamarBaru);
            } else {
                System.out.println("Gagal mengupdate kamar.");
            }
        });
    }

}
