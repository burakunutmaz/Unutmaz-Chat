package com.example.unutmazchatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private String currentRoom;
    private Date time = new Date();

    private String currentUsername;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference messagesDb;

    FirebaseUser user;
    DatabaseReference userReference;

    ArrayList<Message> messages = new ArrayList<>();
    EditText messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentRoom = getIntent().getStringExtra("ROOM");

        Toolbar toolbar = findViewById(R.id.messageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentRoom);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatroomActivity.class);
                startActivity(intent);
            }
        });

        adapter = new CustomAdapter(messages, currentUsername);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userReference = firebaseDatabase.getReference().child("Users").child(user.getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUsername = snapshot.child("username").getValue(String.class);
                adapter.setCurrentUsername(currentUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value.");
            }
        });

        messageInput = findViewById(R.id.messageInput);
        recyclerView = findViewById(R.id.recycleList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        // Read from the database
        messagesDb = firebaseDatabase.getReference("Chatrooms").child(currentRoom).child("Messages");
        messagesDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);

                    if (!containsId(message.getMessageId())){
                        messages.add(0, message);
                        adapter.notifyItemInserted(0);
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value.");
            }
        });
    }

    public boolean containsId(String messageId){
        boolean contains = false;

        for (Message m : messages){
            if (m.getMessageId().equals(messageId)){
                contains = true;
                break;
            }
        }

        return contains;
    }

    public void onSubmit(View view){
        String message = messageInput.getText().toString();
        String sentBy = currentUsername;
        if (message.length() > 0){
            String millis = String.valueOf(Calendar.getInstance().getTimeInMillis());
            String date = String.valueOf(Calendar.getInstance().getTime());
            String hour = (date.split(" "))[3].split(":")[0] + ":" + (date.split(" "))[3].split(":")[1];
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("data", message);
            hashMap.put("sentBy",  sentBy);
            hashMap.put("messageId", millis);
            hashMap.put("hour", hour);
            messageInput.setText("");
            messagesDb.child(String.valueOf(millis)).setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
