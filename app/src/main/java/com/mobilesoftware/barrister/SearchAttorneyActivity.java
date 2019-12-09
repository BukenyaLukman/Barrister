package com.mobilesoftware.barrister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAttorneyActivity extends AppCompatActivity {

    private RecyclerView SearchRecyclerView;
    private Toolbar mToolbar;
    private DatabaseReference SearchRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_attorney);

        mToolbar = findViewById(R.id.client_search_attorney);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search Attorney");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchRef = FirebaseDatabase.getInstance().getReference().child("AttorneyPersonalData");


        SearchRecyclerView = findViewById(R.id.recycler_view_search);
        SearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Search> options =
                new FirebaseRecyclerOptions.Builder<Search>()
                        .setQuery(SearchRef,Search.class)
                        .build();


        FirebaseRecyclerAdapter<Search,ClientViewHolder> adapter = new FirebaseRecyclerAdapter<Search, ClientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ClientViewHolder holder, int position, @NonNull final Search model) {
                holder.AttorneyName.setText(model.getFullName());
                holder.AttorneyAreaOfPractice.setText(model.getAreaOfPractice());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.AttorneyImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchAttorneyActivity.this, AttorneyDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
                return new ClientViewHolder(view);
            }
        };

        SearchRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class ClientViewHolder extends RecyclerView.ViewHolder {

        TextView AttorneyName;
        TextView AttorneyAreaOfPractice;
        CircleImageView AttorneyImageView;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            AttorneyName = itemView.findViewById(R.id.search_name_of_attorney);
            AttorneyAreaOfPractice = itemView.findViewById(R.id.search_area_of_practice);
            AttorneyImageView = itemView.findViewById(R.id.search_profile_image);
        }


    }

}
