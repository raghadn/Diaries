package com.example.diaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewDiaryPage extends AppCompatActivity {

    Button done;
    String diaryKey,currentUser;
    DatabaseReference diaryRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_diary_page);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        diaryKey = getIntent().getExtras().get("diaryKey").toString();

        diaryRef= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("mydiary").child(diaryKey);

        //fields
        final TextView subject=(TextView)findViewById(R.id.page_subject);
        final TextView details=(TextView)findViewById(R.id.page_details);
        final TextView date=(TextView)findViewById(R.id.page_date);

        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    subject.setText(dataSnapshot.child("subject").getValue().toString());
                    details.setText(dataSnapshot.child("details").getValue().toString());
                    date.setText(dataSnapshot.child("date").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        done=(Button)findViewById(R.id.donebtn);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
