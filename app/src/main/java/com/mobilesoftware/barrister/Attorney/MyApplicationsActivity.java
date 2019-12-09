package com.mobilesoftware.barrister.Attorney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilesoftware.barrister.Applications;
import com.mobilesoftware.barrister.R;

public class MyApplicationsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private DatabaseReference LegalRef;
    private FirebaseUser mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applications);


        mAuth = FirebaseAuth.getInstance().getCurrentUser();


        mToolbar = findViewById(R.id.applications_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView =  findViewById(R.id.applications_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LegalRef = FirebaseDatabase.getInstance().getReference().child("ClientLegalNeedsTarget");
        //child(mAuth.getUid());
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Applications> options =
                new FirebaseRecyclerOptions.Builder<Applications>()
                        .setQuery(LegalRef,Applications.class)
                        .build();

        FirebaseRecyclerAdapter<Applications,ApplicationsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Applications, ApplicationsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ApplicationsViewHolder holder, int position, @NonNull Applications model) {
                        holder.ClientName.setText(model.getNameOfClient());
                        holder.LegalProblem.setText(model.getTitleOfLegal());
                    }

                    @NonNull
                    @Override
                    public ApplicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_applications,parent,false);
                        return new ApplicationsViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ApplicationsViewHolder extends RecyclerView.ViewHolder{
        TextView LegalProblem,ClientName;

        public ApplicationsViewHolder(@NonNull View itemView) {
            super(itemView);
            LegalProblem = itemView.findViewById(R.id.legal_problem);
            ClientName = itemView.findViewById(R.id.client_name);

        }
    }
}
