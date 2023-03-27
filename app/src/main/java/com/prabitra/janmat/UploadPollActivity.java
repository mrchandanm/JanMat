package com.prabitra.janmat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.prabitra.janmat.Adapter.DisplayUploadImageViewPagerAdapter;
import com.prabitra.janmat.Models.PollsModel;
import com.prabitra.janmat.Models.PollsPostModel;
import com.prabitra.janmat.services.UploadPollImageService;

import java.util.ArrayList;

public class UploadPollActivity extends AppCompatActivity {
    ImageView backbtn;
    EditText pollQuestion;
    EditText option1,option2,option3,option4,option5,option6;
    ImageView option3Delete,option4Delete,option5Delete,option6Delete;
    LinearLayout option3layout,option4layout,option5layout,option6layout;
    TextView addOptions;
    TextView uploadPoll;
    String userName;
    TextView addyoutubevideobtn,addpollImagesbtn;
    EditText youtbevideolink;
    ViewPager viewPager;
    ArrayList<String> strImageArrayList=new ArrayList<>();
    ProgressDialog progressDialog;
    final int IMAGE_REQUEST_CODE=2,CROP_IMAGE_REQUEST_CODE=3;



    int i=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_poll);

        backbtn=findViewById(R.id.backbtn);
        pollQuestion=findViewById(R.id.pollQuestion);
        option1=findViewById(R.id.pollOption1);
        option2=findViewById(R.id.pollOption2);
        option3=findViewById(R.id.pollOption3);
        option4=findViewById(R.id.pollOption4);
        option5=findViewById(R.id.pollOption5);
        option6=findViewById(R.id.pollOption6);
        addOptions=findViewById(R.id.addOptions);
        uploadPoll=findViewById(R.id.uploadPoll);

        option3Delete=findViewById(R.id.option3Delete);
        option4Delete=findViewById(R.id.option4Delete);
        option5Delete=findViewById(R.id.option5Delete);
        option6Delete=findViewById(R.id.option6Delete);

        option3layout=findViewById(R.id.option3layout);
        option4layout=findViewById(R.id.option4layout);
        option5layout=findViewById(R.id.option5layout);
        option6layout=findViewById(R.id.option6layout);

        addyoutubevideobtn=findViewById(R.id.uploadPollAddYoutubeVideosBtn);
        youtbevideolink=findViewById(R.id.uploadPollYoutubeVideoLink);
        addpollImagesbtn=findViewById(R.id.uploadPollAddImagesBtn);
        viewPager=findViewById(R.id.uploadPollAddImageViewPager);

        progressDialog=new ProgressDialog(UploadPollActivity.this);
        progressDialog.setTitle("Uploading");
        AddPollOptions();
        DeletePollOptions();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addyoutubevideobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(youtbevideolink.getVisibility()!=View.VISIBLE){
                    youtbevideolink.setVisibility(View.VISIBLE);
                    addyoutubevideobtn.setText("Cancel");
                }
                else{
                    youtbevideolink.setVisibility(View.GONE);
                    addyoutubevideobtn.setText("Video Link");
                }
            }
        });

        addpollImagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadPollActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    GetImages();
                } else {
                    ActivityCompat.requestPermissions(UploadPollActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });



        uploadPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UploaoPoll();

                Upload();
//                StartImageService();
            }
        });
// ******************************OnCreate*****************************************************
    }

    private void Upload() {
        if(pollQuestion.getText().toString().isEmpty()) {
            pollQuestion.setError("Please Enter  Question");
            return;
        }
        if(option1.getText().toString().isEmpty()){
            option1.setError("Please Enter Option 1");
            return;
        }
        if(option2.getText().toString().isEmpty()){
            option2.setError("Please Enter Option 2");
            return;
        }
        if(option3layout.getVisibility()==View.VISIBLE && option3.getText().toString().isEmpty()){
            option3.setError("Please Enter Option 3");
            return;
        }
        if(option4layout.getVisibility()==View.VISIBLE && option4.getText().toString().isEmpty()){
            option4.setError("Please Enter Option 3");
            return;
        }
        if(option5layout.getVisibility()==View.VISIBLE && option5.getText().toString().isEmpty()){
            option5.setError("Please Enter Option 3");
            return;
        }
        if(option6layout.getVisibility()==View.VISIBLE && option6.getText().toString().isEmpty()){
            option6.setError("Please Enter Option 3");
            return;
        }
        if(youtbevideolink.getVisibility()==View.VISIBLE && youtbevideolink.getText().toString().isEmpty()){
            youtbevideolink.setError("Paste Youtube Link Here");
            return;
        }
        if(strImageArrayList.size()==0){
            FinishUpload();
        }
        else{
            progressDialog.show();
            StartImageService();
        }

    }

    private void FinishUpload() {
        String phonenumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseFirestore.getInstance().document("UserDetails" + "/" + phonenumber).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetails userDetails= documentSnapshot.toObject(UserDetails.class);
                userName=userDetails.getName();
                PollsPostModel pollsPostModel=getPollsPostModel();
                String pollid=pollsPostModel.getPollId();
                FirebaseFirestore.getInstance().document("Polls"+"/"+pollid).set(pollsPostModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadPollActivity.this, "Poll Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadPollActivity.this, "Poll Upload failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private void StartImageService() {
        String phonenumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseFirestore.getInstance().document("UserDetails" + "/" + phonenumber).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDetails userDetails= documentSnapshot.toObject(UserDetails.class);
                userName=userDetails.getName();
                PollsPostModel pollsPostModel=getPollsPostModel();
                Intent intent=new Intent(UploadPollActivity.this, UploadPollImageService.class);
                intent.putExtra("pollsPostModel", new Gson().toJson(pollsPostModel));
                startService(intent);
                progressDialog.dismiss();
                finish();
            }
        });

    }

    private void GetImages() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(imageIntent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==IMAGE_REQUEST_CODE && data!=null){
            strImageArrayList= new ArrayList<>();
            if (data.getData() != null) {
                // Single image selected
                Uri uri = data.getData();
                strImageArrayList.add(uri.toString());
            } else if (data.getClipData() != null) {
                // Multiple images selected

                if (data.getClipData().getItemCount() > 5) {
                    Toast.makeText(this, "Maximum 5 Images Allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < data.getClipData().getItemCount(); ++i) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    strImageArrayList.add(uri.toString());
                }
            }

            viewPager.setVisibility(View.VISIBLE);
            DisplayUploadImageViewPagerAdapter viewPagerAdapter=new DisplayUploadImageViewPagerAdapter(UploadPollActivity.this,strImageArrayList);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    private void UploaoPoll() {


        String phonenumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseFirestore.getInstance().document("UserDetails" + "/" + phonenumber).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            UserDetails userDetails= documentSnapshot.toObject(UserDetails.class);
                            userName=userDetails.getName();

                            PollsPostModel pollsPostModel=getPollsPostModel();
                            String time=String.valueOf(pollsPostModel.getTime());
                            String pollid=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString() + time;
                            FirebaseFirestore.getInstance().document("Polls"+"/"+pollid).set(pollsPostModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UploadPollActivity.this, "Poll Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                   finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadPollActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });



    }

    private PollsPostModel getPollsPostModel(){
        String youtubevideolinkstr=youtbevideolink.getText().toString();
        String youtubevideoid="";
        String title="";
        long time=System.currentTimeMillis();

        String pollid=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString() + String.valueOf(time);
        title = pollQuestion.getText().toString();

        ArrayList<PollsModel> pollsModels;
        pollsModels=new ArrayList<>();

//        if(option1.getText().toString().isEmpty()){
//            option1.setError("Please Enter Option 1");
//        }
//        if(option2.getText().toString().isEmpty()){
//            option2.setError("Please Enter Option 2");
//        }
//        if(option3layout.getVisibility()==View.VISIBLE && option3.getText().toString().isEmpty()){
//            option3.setError("Please Enter Option 3");
//        }
//        if(option4layout.getVisibility()==View.VISIBLE && option4.getText().toString().isEmpty()){
//            option4.setError("Please Enter Option 3");
//        }
//        if(option5layout.getVisibility()==View.VISIBLE && option5.getText().toString().isEmpty()){
//            option5.setError("Please Enter Option 3");
//        }
//        if(option6layout.getVisibility()==View.VISIBLE && option6.getText().toString().isEmpty()){
//            option6.setError("Please Enter Option 3");
//        }

        pollsModels.add(new PollsModel(option1.getText().toString(),"0",0,false));
        pollsModels.add(new PollsModel(option2.getText().toString(),"0",0,false));
        if(option3layout.getVisibility()==View.VISIBLE){
            pollsModels.add(new PollsModel(option3.getText().toString(),"0",0,false));
        }
        if(option4layout.getVisibility()==View.VISIBLE){
            pollsModels.add(new PollsModel(option4.getText().toString(),"0",0,false));
        }
        if(option5layout.getVisibility()==View.VISIBLE){
            pollsModels.add(new PollsModel(option5.getText().toString(),"0",0,false));
        }
        if(option6layout.getVisibility()==View.VISIBLE){
            pollsModels.add(new PollsModel(option6.getText().toString(),"0",0,false));
        }

//        if(youtbevideolink.getVisibility()==View.VISIBLE && youtbevideolink.getText().toString().isEmpty()){
//            youtbevideolink.setError("Paste Youtube Link Here");
//        }
        if(!youtubevideolinkstr.isEmpty() && youtbevideolink.getVisibility()==View.VISIBLE){
            String[] youtubevideoidarr = youtubevideolinkstr.split("/",-2);
            youtubevideoid = youtubevideoidarr[3];
        }
//        Log.d("phone", "getPollsPostModel: "+phonenumber);


        PollsPostModel pollsPostModel=new PollsPostModel(userName,pollid,title,time,pollsModels,youtubevideoid,strImageArrayList);

        return pollsPostModel;
    }


    private void AddPollOptions() {
        addOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==3){
                    option3layout.setVisibility(View.VISIBLE);
                    i++;
                } else if (i==4) {
                    option3Delete.setVisibility(View.GONE);
                    option4layout.setVisibility(View.VISIBLE);
                    i++;
                } else if (i==5) {
                    option4Delete.setVisibility(View.GONE);
                    option5layout.setVisibility(View.VISIBLE);
                    i++;
                } else if (i==6) {
                    option5Delete.setVisibility(View.GONE);
                    option6layout.setVisibility(View.VISIBLE);
                    i++;
                } else if (i>6) {
                    Toast.makeText(UploadPollActivity.this, "You can add only six options", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void  DeletePollOptions() {
        option6Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=6;
                option6layout.setVisibility(View.GONE);
                option5Delete.setVisibility(View.VISIBLE);
            }
        });

        option5Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=5;
                option5layout.setVisibility(View.GONE);
                option4Delete.setVisibility(View.VISIBLE);
            }
        });
        option4Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=4;
                option4layout.setVisibility(View.GONE);
                option3Delete.setVisibility(View.VISIBLE);
            }
        });
        option3Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=3;
                option3layout.setVisibility(View.GONE);
            }
        });
    }

    public String getUserName() {
        String phonenumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseFirestore.getInstance().document("UserDetails" + "/" + phonenumber).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            UserDetails userDetails= documentSnapshot.toObject(UserDetails.class);
                            userName=userDetails.getName();
                        }

                    }
                });
        return userName;
    }
}