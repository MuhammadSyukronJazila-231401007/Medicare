package com.example.demo1;

import com.example.demo1.Model.Pengguna;

public class PenggunaSekarang {
    public static Pengguna penggunaSekarang;

    public static void tambahPengguna(String id_pengguna, String nama, String username, String password, String no_hp, String email, String peran){
        penggunaSekarang = new Pengguna(id_pengguna, nama, username,  password,  no_hp,  email,  peran);
    }
}
