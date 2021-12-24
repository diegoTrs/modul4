package com.diego.modul4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.util.Calendar;

public class ModifyActivity extends AppCompatActivity {
    private EditText edtNamaLengkap;
    private String namaLengkap;
    private EditText inputtglLahir;
    private String tglLahir;
    private DatePickerDialog.OnDateSetListener setListener;
    private Slider slider;
    private SeekBar seekBar;
    private TextView lbl_umur;
    private String nilaiUmur;
    private String umur;
    private Button btnUbah;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String gender;
    private RadioButton rblaki;
    private RadioButton rbperempuan;
    private CheckBox mPemeriksaanKesehatan, mKonsultasiobat, mPerawatan, mPemeriksaanrutin, mKeluhan, mLainnya;
    private String mResult = "";
    private String service;
    private int id;

    private DataHelper db;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        edtNamaLengkap = findViewById(R.id.input_namalengkap);
        inputtglLahir = findViewById(R.id.input_tgl_lahir);
        seekBar = findViewById(R.id.seekbar);
        lbl_umur = findViewById(R.id.lbl_umur);
        btnUbah = findViewById(R.id.ubah);
        radioGroup = findViewById(R.id.radioGroupGender);
        rblaki = findViewById(R.id.laki_laki);
        rbperempuan = findViewById(R.id.perempuan);
        mPemeriksaanKesehatan = findViewById(R.id.pemeriksaan_kesehatan);
        mKonsultasiobat = findViewById(R.id.konsultasi_obat);
        mPerawatan = findViewById(R.id.perawatan);
        mPemeriksaanrutin = findViewById(R.id.pemeriksaan_rutin);
        mKeluhan = findViewById(R.id.keluhan);
        mLainnya = findViewById(R.id.lainnya);

        db = new DataHelper(this);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        namaLengkap = intent.getExtras().getString("namaLengkap");
        tglLahir = intent.getExtras().getString("tglLahir");
        umur = intent.getExtras().getString("umur");
        gender = intent.getExtras().getString("gender");
        service = intent.getExtras().getString("service");

        edtNamaLengkap.setText(namaLengkap);
        inputtglLahir.setText(tglLahir);
        lbl_umur.setText("Umur : " + umur);
        setGenderSelected();
        setServiceSelected();
        seekBar.setProgress(Integer.parseInt(umur));

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        inputtglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ModifyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        inputtglLahir.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                umur = String.valueOf(i);
                lbl_umur.setText("Umur : " + umur);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaLengkap = edtNamaLengkap.getText().toString().trim();
                tglLahir = inputtglLahir.getText().toString().trim();
                gender = getGenderSelected();
                service = getServiceSelected();

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ModifyActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Daftarkan");
                builder.setMessage(
                        "Apakah anda sudah yakin dengan data anda ?\n\n" +
                                "Nama Lengkap : \n" + namaLengkap + "\n\n" +
                                "Tanggal Lahir : \n" + tglLahir + "\n\n" +
                                "Umur : \n" + umur + "\n\n" +
                                "Gender :\n" + gender + "\n\n" +
                                "Service Yang di Pilih :\n" +service + ""
                );

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Data anda berhasil terdaftarkan !", Toast.LENGTH_SHORT).show();

                        Intent layout2 = new Intent(ModifyActivity.this, MainActivity.class);

                        user = new User();
                        user.setId(id);
                        user.setNamaLengkap(namaLengkap);
                        user.setTglLahir(tglLahir);
                        user.setUmur(umur);
                        user.setGender(gender);
                        user.setService(service);

                        db.update(user);

                        startActivity(layout2);
                        finish();


                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

            }
        });

    }

    private void setServiceSelected() {
        if (service.contains("Pemeriksaan Kesehatan")){
            mPemeriksaanKesehatan.setChecked(true);
        }
        if (service.contains("Konsultasi Obat")){
            mKonsultasiobat.setChecked(true);
        }
        if (service.contains("Perawatan")){
            mPerawatan.setChecked(true);
        }
        if (service.contains("Pemeriksaan Rutin")){
            mPemeriksaanrutin.setChecked(true);
        }
        if (service.contains("Keluhan")){
            mKeluhan.setChecked(true);
        }
        if (service.contains("Lainnya")){
            mLainnya.setChecked(true);
        }
    }

    private String getServiceSelected(){
        String service = "";

        if (mPemeriksaanKesehatan.isChecked()) {
            service += mPemeriksaanKesehatan.getText().toString() + "\n";
        }
        if (mKonsultasiobat.isChecked()) {
            service += mKonsultasiobat.getText().toString() + "\n";
        }
        if (mPerawatan.isChecked()) {
            service += mPerawatan.getText().toString() + "\n";
        }
        if (mPemeriksaanrutin.isChecked()) {
            service += mPemeriksaanrutin.getText().toString() + "\n";
        }
        if (mKeluhan.isChecked()) {
            service += mKeluhan.getText().toString() + "\n";
        }
        if (mLainnya.isChecked()) {
            service += mLainnya.getText().toString() + "\n";
        }

        return service;

    }

    private void setGenderSelected(){
        if (gender.equals("Laki-Laki")) {
            rblaki.setChecked(true);
        } else if (gender.equals("Perempuan")){
            rbperempuan.setChecked(true);
        }
    }

    private String getGenderSelected(){
        String gender = "";

        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == rblaki.getId()){
            gender = "Laki - Laki";
        }
        else if (selectedId == rbperempuan.getId()){
            gender = "Perempuan";
        }

        return gender;
    }

}