package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.R;
import com.example.taskerfyp.SendOffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterTasker extends RecyclerView.Adapter<MyAdapterTasker.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    String id;

    public MyAdapterTasker(Context c, ArrayList<Post> p) {
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public MyAdapterTasker.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_tasker, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.username.setText(posts.get(position).getCurrent_user_name());
        holder.budget.setText("Budget: " + posts.get(position).getBudget() + " Rs");
        holder.deadline.setText("Deadline: " + posts.get(position).getDeadline() + " day(s)");
        holder.prof_title.setText("Title: " + posts.get(position).getTitle());
        holder.task_description.setText("Description: \n" + posts.get(position).getDescription());
        holder.task_time.setText(posts.get(position).getTime());
        holder.task_date.setText(posts.get(position).getDate());

        holder.btnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = posts.get(position).getId();
                String post_id = posts.get(position).getPostId();
                Intent intent = new Intent(context, SendOffer.class);
                intent.putExtra("Post_krny_waly_ki_id", id);
                intent.putExtra("post_ki_id", post_id);
                context.startActivity(intent);
            }
        });

        FirebaseUser userC = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference onClickRef = FirebaseDatabase.getInstance().getReference("Offers").child(posts.get(position).getId()).child(userC.getUid());
        onClickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("onClick")) {
                    if (dataSnapshot.child("onClick").getValue().equals("1")) {
                        holder.btnSendOffer.setText("Offer Sent");
                        holder.btnSendOffer.setBackgroundColor(Color.LTGRAY);
                        holder.btnSendOffer.setEnabled(false);
                    }
                } else
                    Toast.makeText(context, "Not Clicked Yet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ////////
        DatabaseReference UsersRef;
        id = posts.get(position).getId();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(id);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(holder.profile_image);
                    } else {
                        Toast.makeText(context, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username, budget, deadline, prof_title, task_time, task_date, task_description;
        private CircleImageView profile_image;
        private Button btnSendOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile);
            username = itemView.findViewById(R.id.username);
            budget = itemView.findViewById(R.id.budget);
            deadline = itemView.findViewById(R.id.deadline);
            prof_title = itemView.findViewById(R.id.prof_title);
            task_time = itemView.findViewById(R.id.task_time);
            task_date = itemView.findViewById(R.id.task_date);
            task_description = itemView.findViewById(R.id.task_description);
            btnSendOffer = itemView.findViewById(R.id.btnSendOffer);
        }
    }
}