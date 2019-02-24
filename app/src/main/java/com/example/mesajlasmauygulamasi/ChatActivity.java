package com.example.mesajlasmauygulamasi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    String username, otherName;
    TextView chatUserName;
    ImageView backImage, sendImage;
    EditText chatEditText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView chatRecylerView;
    MesajAdapter mesajAdapter;
    List<MesajModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        loadMesaj();
    }

    public void tanimla() {
        username = getIntent().getExtras().getString("username");
        otherName = getIntent().getExtras().getString("othername");
        list=new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        //Log.i("deneme",username+"--"+otherName);
        chatUserName = findViewById(R.id.chatUserName);
        backImage = findViewById(R.id.backImage);
        sendImage = findViewById(R.id.sendImage);
        chatEditText = findViewById(R.id.chatEditText);
        chatUserName.setText(otherName);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(ChatActivity.this, MainActivity.class);
                ıntent.putExtra("kadi", username);
                startActivity(ıntent);
            }
        });

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = chatEditText.getText().toString();
                chatEditText.setText("");
                mesajGonder(mesaj);
            }
        });

        chatRecylerView=findViewById(R.id.chatRecylerView);
        chatRecylerView.setLayoutManager(new GridLayoutManager(ChatActivity.this,1));

        mesajAdapter=new MesajAdapter(ChatActivity.this,list,ChatActivity.this,username);
        chatRecylerView.setAdapter(mesajAdapter);

    }

    public void mesajGonder(String text) {
        final String key = reference.child("Mesajlar").child(username).child(otherName).push().getKey();
        final Map messageMap = new HashMap();
        messageMap.put("text", text);
        messageMap.put("from", username);
        reference.child("Mesajlar").child(username).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reference.child("Mesajlar").child(otherName).child(username).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    public void loadMesaj() {
        reference.child("Mesajlar").child(username).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MesajModel mesajModel = dataSnapshot.getValue(MesajModel.class);
               // Log.i("mesajlar", mesajModel.toString());
                list.add(mesajModel);
                mesajAdapter.notifyDataSetChanged();//eger bir güncelleme varsa adapter da da güncelleme yap

                chatRecylerView.scrollToPosition(list.size()-1);//pozisyonu ayarlama
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
