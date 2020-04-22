package com.example.diaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddMemory extends AppCompatActivity {
    TextView datetext;
    String day,month1,year1;
    private DatePickerDialog.OnDateSetListener mDatasetListner;
    EditText subj,det;
    Button add,cancel;
    String subject,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        datetext=(TextView)findViewById(R.id.date);
        subj=(EditText)findViewById(R.id.subject);
        det=(EditText)findViewById(R.id.details);
        add=(Button)findViewById(R.id.Addmemorybtn);
        cancel=(Button)findViewById(R.id.canceladdbtn);





        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(AddMemory.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDatasetListner,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
        mDatasetListner=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                day=dayOfMonth+"";
                year1=year+"";
                month1=month+"";
                String settext=dayOfMonth+"/"+month+"/"+year;
                datetext.setText(settext);
            }
        };


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subject=subj.getText().toString();
                details=det.getText().toString();

                if(datetext.getText().toString().equals("What day was it?")||details.isEmpty()||subject.isEmpty())
                    showMessage("Please fill all fields...");
                else{
                    if(subject.length()>50){
                        showMessage("Please select a shorter subject");
                    }
                    else {
                        addmemory(day, month1, year1, subject, details);
                    }
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void addmemory(final String day, final String month, final String year, final String subject, final String details){

        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        String savecurrentdate=currentDate.format(calfordate.getTime());

        Calendar calfortime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        String savecurrenttime=currentTime.format(calfortime.getTime());

        final String postrandomname=savecurrentdate+savecurrenttime;
        final DatabaseReference usersRef=FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        final String currentuser=mAuth.getCurrentUser().getUid();





        usersRef.child(currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap postmap=new HashMap();
                    postmap.put("subject",subject);
                    postmap.put("details",details);
                    postmap.put("day",day);
                    postmap.put("month",month);
                    postmap.put("year",year);
                    String d=day+"/"+month+"/"+year;
                    postmap.put("date",d);
                    String subkey=subject.replaceAll("[^a-zA-Z0-9_-]", "");

                    usersRef.child(currentuser).child("mydiary").child(subkey+postrandomname).updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                finish();
                                showMessage("Saved...");
                            }
                            else {
                                showMessage("Error...");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
}
