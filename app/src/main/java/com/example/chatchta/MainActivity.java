package com.example.chatchta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int MAX_MESSAGE_LENGTH = 100;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    EditText mEditTextMessage;
    Button mSendButton;
    RecyclerView mMessagesRecycler;

    ArrayList<String> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mMessagesRecycler = findViewById(R.id.messages_recycler);

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));


        final DataAdapter dataAdapter = new DataAdapter(this, messages);

        mMessagesRecycler.setAdapter(dataAdapter);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = mEditTextMessage.getText().toString();

                if (msg.equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите сообщение.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(msg.length() > MAX_MESSAGE_LENGTH){
                    Toast.makeText(getApplicationContext(), "Сообщение превысило 150 символов.", Toast.LENGTH_SHORT).show();
                    return;
                }

                myRef.push().setValue(msg);
                mEditTextMessage.setText("");

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        })

    }
}