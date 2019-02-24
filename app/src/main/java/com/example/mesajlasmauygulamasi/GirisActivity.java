package com.example.mesajlasmauygulamasi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GirisActivity extends AppCompatActivity {
    EditText kullaniciAdiEditText;
    Button kayitOlButon;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        FirebaseApp.initializeApp(GirisActivity.this);
        tanimla();
    }
    public void tanimla()
    {
        kullaniciAdiEditText=findViewById(R.id.kullaniciAdiEditText);
        kayitOlButon=findViewById(R.id.kayitOlButon);
        firebaseDatabase=FirebaseDatabase.getInstance();

        reference=firebaseDatabase.getReference();

        kayitOlButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String username=kullaniciAdiEditText.getText().toString();
               kullaniciAdiEditText.setText("");
               ekle(username);
            }
        });

    }
    public void ekle(final String kadi)
    {
        reference.child("Kullanıcılar").child(kadi).child("kullaniciadi").setValue(kadi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Başarı ile Giriş Yaptınız",Toast.LENGTH_LONG).show();
                    Intent ıntent=new Intent(GirisActivity.this,MainActivity.class);
                    ıntent.putExtra("kadi",kadi);
                    startActivity(ıntent);
                }

            }
        });
    }
}
