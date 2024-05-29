package com.zorro.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    private AndroidKeyStoreUtils keyStore;
    String afDevKey = "hfsjfkjksdfkdfakjfkdsfj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        TextView device = findViewById(R.id.device);
        TextView dataText = findViewById(R.id.data);
        Button encrypt = findViewById(R.id.encrypt);
        Button decrypt = findViewById(R.id.decrypt);
        TextView textEncrypt = findViewById(R.id.text_encrypt);
        TextView textDecrypt = findViewById(R.id.text_decrypt);
        dataText.setText("原始数据：" + afDevKey);
        Log.e("TAG", "onCreate: " + System.currentTimeMillis());
        device.setText("device:" + new NativeLib().call("ro.boot.vbmeta.digest", ""));
        Log.e("TAG", "onCreate: " + System.currentTimeMillis());
        keyStore = new AndroidKeyStoreUtils();
        encrypt.setOnClickListener(v -> {
                    textEncrypt.setText("加密后数据：" + keyStore.encrypt(dataText.getText().toString()));
                    textDecrypt.setText(getString(R.string.show_decrypt));
                }
        );
        decrypt.setOnClickListener(v -> {
            textDecrypt.setText(keyStore.decrypt(textEncrypt.getText().toString()));
        });
    }

}