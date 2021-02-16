package com.example.unutmazchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.HashMap;

public class ChatroomActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private EditText input_roomName, input_roomDesc;
    private RadioGroup group;

    private RecyclerView recyclerView;
    private RoomListAdapter adapter;
    private FirebaseAuth auth;
    private String username;
    TextView tvUsername;
    private String newRoomIcon;
    ArrayList<Chatroom> chatrooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        adapter = new RoomListAdapter(chatrooms, username);
        adapter.setOnItemClickListener(new RoomListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println(chatrooms.get(position).getName());
                Intent intent = new Intent(ChatroomActivity.this, MainActivity.class);
                intent.putExtra("ROOM", chatrooms.get(position).getName());
                startActivity(intent);
                finish();
            }
        });
        tvUsername = findViewById(R.id.toolbarUsername);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username = user.getUsername();
                tvUsername.setText(username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        recyclerView = findViewById(R.id.chatroomList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        DatabaseReference roomsReference = FirebaseDatabase.getInstance().getReference("Chatrooms");
        roomsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot ds : snapshot.getChildren()){
                    Chatroom chatroom = ds.getValue(Chatroom.class);
                    if (!containsRoom(chatroom.getName())){
                        System.out.println("added");
                        chatrooms.add(0, chatroom);
                        adapter.notifyItemInserted(0);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean containsRoom(String name){
        for(Chatroom c : chatrooms){
            if (c.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void onCreateRoomClicked(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View roomPopup = getLayoutInflater().inflate(R.layout.popup, null);
        input_roomName = roomPopup.findViewById(R.id.roomNameInput);
        input_roomDesc = roomPopup.findViewById(R.id.roomDescInput);
        Button input_submit = roomPopup.findViewById(R.id.create);
        Button input_cancel = roomPopup.findViewById(R.id.cancel);
        group = roomPopup.findViewById(R.id.radioGroup);
        dialogBuilder.setView(roomPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = roomPopup.findViewById(group.getCheckedRadioButtonId());
                switch (radioButton.getId()){
                    case R.id.radio_general:
                        newRoomIcon = "icon_general";
                        break;
                    case R.id.radio_game:
                        newRoomIcon = "icon_game";
                        break;
                    case R.id.radio_family:
                        newRoomIcon = "icon_family";
                        break;
                    case R.id.radio_food:
                        newRoomIcon = "icon_food";
                        break;
                    case R.id.radio_school:
                        newRoomIcon = "icon_school";
                        break;
                    case R.id.radio_music:
                        newRoomIcon = "icon_music";
                        break;
                }
            }
        });

        input_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input_roomName.getText().toString();
                String desc = input_roomDesc.getText().toString();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("desc", desc);
                hashMap.put("creator", username);
                hashMap.put("iconName", newRoomIcon);
                DatabaseReference roomListRef = FirebaseDatabase.getInstance().getReference("Chatrooms");
                roomListRef.child(name).setValue(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ChatroomActivity.this, "Room created!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

        input_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}