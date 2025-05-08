package com.example.demo1.Utils;

import com.example.demo1.Model.Pasien;
import com.example.demo1.Model.Pengguna;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class Tabel {

    public static <S, T> void setColumnCenter(TableColumn<S, T> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }


}
