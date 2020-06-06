package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile_of_Customer extends AppCompatActivity {
    TextView name, phone_number, email, gender;
    CircleImageView dpTasker;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_of__customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("View Profile");

        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        dpTasker = findViewById(R.id.dpTasker);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(user.getUid());
        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name_tasker = dataSnapshot.child("customerUsername").getValue().toString();
                    String email_id = dataSnapshot.child("email").getValue().toString();
                    phone = dataSnapshot.child("customerPhonenumber").getValue().toString();
                    String gend = dataSnapshot.child("customerGender").getValue().toString();
                    String image = dataSnapshot.child("profileimage").getValue().toString();

                    phone_number.setText(phone);
                    email.setText(email_id);
                    gender.setText(gend);
                    name.setText(name_tasker);
                    Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(dpTasker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewProfile_of_Customer.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}