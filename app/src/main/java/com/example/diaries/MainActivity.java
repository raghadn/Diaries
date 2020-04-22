package com.example.diaries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button add;
    ImageView logout;
    RecyclerView diarieslist;
    DatabaseReference diariesRef;
    String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();



        if (mAuth.getCurrentUser() == null) {
            finish();
            sendUserToStartActivity();
        }
        else {
            currentuser=mAuth.getCurrentUser().getUid().toString();
            add = (Button) findViewById(R.id.Addbtn);
            logout = (ImageView) findViewById(R.id.logoutbtn);


            diarieslist = (RecyclerView) findViewById(R.id.all_diaries);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            diarieslist.setLayoutManager(linearLayoutManager);


            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    sendUserToStartActivity();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setupintent = new Intent(MainActivity.this, AddMemory.class);
                    startActivity(setupintent);
                }
            });


            displayDiaries();
        }

    }
    private void sendUserToStartActivity(){
        Intent setupintent=new Intent(MainActivity.this,Login.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

    private void displayDiaries(){
        diariesRef=FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).child("mydiary");
        FirebaseRecyclerAdapter<Diaries,DiariesViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Diaries, DiariesViewHolder>(Diaries.class,R.layout.all_diary,DiariesViewHolder.class,diariesRef.orderByChild("date")) {
                    @Override
                    protected void populateViewHolder(DiariesViewHolder diariesViewHolder, Diaries diaries, int i) {
                        diariesViewHolder.setDay(diaries.getDay());
                        diariesViewHolder.setDetails(diaries.getDetails());
                        diariesViewHolder.setMonth(diaries.getMonth()+"/"+diaries.getYear());
                        diariesViewHolder.setSubject(diaries.getSubject());

                        final String diaryKey = getRef(i).getKey();
                        diariesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ClickRequest = new Intent(MainActivity.this, ViewDiaryPage.class);
                                ClickRequest.putExtra("diaryKey",diaryKey);
                                startActivity(ClickRequest);

                            }
                        });

                    }
                };

        diarieslist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class DiariesViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public DiariesViewHolder(View itemView){
            super(itemView);
            mView=itemView;
        }

        public void setDay(String day){
            TextView d=(TextView)mView.findViewById(R.id.display_day);
            d.setText(day);
        }

        public void setMonth(String month){
            TextView mo=(TextView)mView.findViewById(R.id.display_monthyear);
            mo.setText(month);
        }


        public void setSubject(String subject){
            TextView sub=(TextView)mView.findViewById(R.id.dispay_subject);
            //if(subject.length()>25)
              //  sub.setText(subject.substring(0,20));
            //else
                sub.setText(subject);
        }
        public void setDetails(String details){
            TextView det=(TextView)mView.findViewById(R.id.dispay_details);
           // if(details.length()>50)
            //det.setText(details.substring(0,45));
            //else
                det.setText(details);
        }

    }
}
