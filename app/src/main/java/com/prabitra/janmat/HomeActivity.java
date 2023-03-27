package com.prabitra.janmat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prabitra.janmat.Adapter.PollsPostAdapter;
import com.prabitra.janmat.Models.PollsModel;
import com.prabitra.janmat.Models.PollsPostModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    com.google.android.material.navigation.NavigationView NavigationView;
    ImageView opendrawer;
    LinearLayout logoutbtn;
    FirebaseAuth firebaseAuth;
    RecyclerView pollPostRecyclerview;
    PollsPostAdapter pollsPostAdapter;
    ArrayList<PollsPostModel> pollsPostModelslist;
    ArrayList<PollsModel> pollsModelslist;
    FloatingActionButton createPoll;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        drawerlayout=findViewById(R.id.Navigation_Drawer);
        NavigationView=findViewById(R.id.NavigationView);
        createPoll=findViewById(R.id.poll_upload);
        pollPostRecyclerview=findViewById(R.id.Poll_post_RecyclerView);

        opendrawer=findViewById(R.id.open_drawer);
        View sideView = NavigationView.getHeaderView(0);
        logoutbtn=sideView.findViewById(R.id.logoutbtn);
        opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerlayout.openDrawer(GravityCompat.START);
            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent ilogin = new Intent(HomeActivity.this, RegisterActivity.class);
                        startActivity(ilogin);
                        finish();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(HomeActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });
pd=new ProgressDialog(HomeActivity.this);
pd.setTitle("Fetching Data");
pd.show();
FirebaseFirestore.getInstance().collection("Polls").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

        if (value!=null) {
            pollsPostModelslist =new ArrayList<>();
            for (QueryDocumentSnapshot document : value) {
                PollsPostModel pollsPostModel=document.toObject(PollsPostModel.class);
                pollsPostModelslist.add(pollsPostModel);
            }
            SetRecyclerView(pollsPostModelslist);
            pd.dismiss();
        } else {
            Log.d("get", "Error getting documents: ", error);
            pd.dismiss();
        }
    }
});
//        .addOnFailureListener(new OnFailureListener() {
//    @Override
//    public void onFailure(@NonNull Exception e) {
//        pd.dismiss();
//        Toast.makeText(HomeActivity.this, "Failed to fetch data ", Toast.LENGTH_SHORT).show();
//    }
//});



        createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent icreatepoll= new Intent(HomeActivity.this,UploadPollActivity.class);
                startActivity(icreatepoll);
            }
        });




//**************************Oncreate*****************************
    }



    private void SetRecyclerView(ArrayList<PollsPostModel> pollsPostModelslist) {
        pollPostRecyclerview.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        pollsPostAdapter=new PollsPostAdapter(HomeActivity.this,pollsPostModelslist);
        pollPostRecyclerview.setAdapter(pollsPostAdapter);
        pollsPostAdapter.notifyDataSetChanged();

    }
}