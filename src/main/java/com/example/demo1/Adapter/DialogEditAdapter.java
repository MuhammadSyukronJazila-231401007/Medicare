package com.example.demo1.Adapter;

import com.example.demo1.Model.Pengguna;
import javafx.scene.control.TableView;

public interface DialogEditAdapter {
    void tampilkanDialogEdit(Pengguna pengguna, TableView<Pengguna> tableUser);
}
