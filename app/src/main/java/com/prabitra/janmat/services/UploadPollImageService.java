package com.prabitra.janmat.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.prabitra.janmat.ImageResizer;
import com.prabitra.janmat.Models.PollsPostModel;
import com.prabitra.janmat.UploadPollActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UploadPollImageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        PollsPostModel pollsPostModel=new PollsPostModel();
        if (intent.hasExtra("pollsPostModel")) {
            pollsPostModel = new Gson().fromJson(intent.getStringExtra("pollsPostModel"), PollsPostModel.class);
        } else {
            return START_STICKY;
        }

        ArrayList<String> photos=new ArrayList<>();
        photos=pollsPostModel.getPhotos();

        try {
            UploadImages(pollsPostModel,photos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return START_STICKY;
    }

    private void UploadImages(PollsPostModel pollsPostModel, ArrayList<String> photos) throws IOException {
        int count=0;
        ArrayList<String> uploadedImage= new ArrayList<>();
        for(String image:photos){
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image));
//            bitmap = ImageResizer.reduceBitmapSize(bitmap, 240000);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();

            String Phonenum= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();
            StorageReference storageReference= FirebaseStorage.getInstance().getReference("Uploads").child(Phonenum).child(pollsPostModel.getPollId()).child(count+"");
            count++;
            storageReference.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String uriStr = uri.toString();
                            uploadedImage.add(uriStr);

                            if(uploadedImage.size()==photos.size()){
                                // All Images Uploaded
                                FinishUpload(pollsPostModel, uploadedImage);
                            }
                        }
                    });
                }
            });

        }
    }

    private void FinishUpload(PollsPostModel pollsPostModel, ArrayList<String> uploadedImage) {
        pollsPostModel.setPhotos(uploadedImage);
        String pollid=pollsPostModel.getPollId();
        FirebaseFirestore.getInstance().document("Polls"+"/"+pollid).set(pollsPostModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UploadPollImageService.this, "Poll Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPollImageService.this, "Poll Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
