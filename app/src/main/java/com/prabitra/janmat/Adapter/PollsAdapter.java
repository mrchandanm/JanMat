package com.prabitra.janmat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prabitra.janmat.HomeActivity;
import com.prabitra.janmat.Models.PollsModel;
import com.prabitra.janmat.Models.PollsPostModel;
import com.prabitra.janmat.Models.UserPollsModel;
import com.prabitra.janmat.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PollsAdapter extends RecyclerView.Adapter<PollsAdapter.ViewHolder> {
    View view;
    ArrayList<PollsModel> pollList;
    Context mContext;
    String pollId;
    ArrayList<Integer> pollposition;
    int pollselected=-1,lastpollselected;
    double pollcount=0,selectedpollcount=0;
    double percentage;
    int i;
    String  userphonenum= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();



    public PollsAdapter(ArrayList<PollsModel> pollList, Context mContext,String pollId) {
        this.pollList = pollList;
        this.mContext = mContext;
        this.pollId=pollId;
    }


    @NonNull
    @Override
    public PollsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.cart_polls_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollsAdapter.ViewHolder holder, int position) {
        DecimalFormat decimalFormat=new DecimalFormat("#.##");
       holder.unpolledPoll.setText(pollList.get(position).getPollOptions());
        DocumentReference userpollref= FirebaseFirestore.getInstance().document("Polls"+"/"+pollId+"/"+"UserPolls"+"/"+userphonenum);

        userpollref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    UserPollsModel userPollsModel = documentSnapshot.toObject(UserPollsModel.class);
                    pollselected = userPollsModel.getPollselected();


                CollectionReference pollref = FirebaseFirestore.getInstance().collection("Polls" + "/" + pollId + "/" + "UserPolls");
                pollref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            pollcount = queryDocumentSnapshots.size();
                        }
             //*******************************************************************************
                        pollref.whereEqualTo("pollselected", pollselected).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots != null) {
                                    selectedpollcount = queryDocumentSnapshots.size();
                                }
                                if (pollcount != 0) {
                                    percentage = (selectedpollcount / pollcount) * 100;
                                }
                                if (holder.getAdapterPosition() == pollselected) {
                                    holder.pollLayout.setVisibility(View.VISIBLE);
                                    holder.unpolledPoll.setVisibility(View.GONE);
                                    holder.pollLayoutOfUnpolled.setVisibility(View.GONE);
                                    holder.pollOptiontxt.setText(pollList.get(holder.getAdapterPosition()).getPollOptions());
                                    holder.pollprogressbar.setProgress((int) percentage);
                                    holder.pollPercentage.setText((String.valueOf(decimalFormat.format(percentage)) + "%"));
                                } else {
                                    pollref.whereEqualTo("pollselected",holder.getAbsoluteAdapterPosition()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (queryDocumentSnapshots != null) {
                                                selectedpollcount = queryDocumentSnapshots.size();
                                            }
                                            if (pollcount != 0) {
                                                percentage = (selectedpollcount / pollcount) * 100;
                                            }
                                            holder.pollLayout.setVisibility(View.GONE);
                                            holder.unpolledPoll.setVisibility(View.GONE);
                                            holder.pollLayoutOfUnpolled.setVisibility(View.VISIBLE);
                                            holder.pollOptiontxtOfunpolled.setText(pollList.get(holder.getAbsoluteAdapterPosition()).getPollOptions());
                                            holder.progressBarOfUnpoled.setProgress((int) percentage);
                                            holder.pollpercentageOfunpolled.setText((String.valueOf(decimalFormat.format(percentage)) + "%"));
                                        }
                                    });

                                }
                            }
                        });





       //*******************************************************************************
                    }

                });
            }

            }
        });





//           if (pollList.get(position).getIspolled()) {
//               holder.pollLayout.setVisibility(View.VISIBLE);
//               holder.unpolledPoll.setVisibility(View.GONE);
//               holder.pollprogressbar.setProgress(pollList.get(position).getProgress());
//               holder.pollPercentage.setText(pollList.get(position).getPollPercentage());
//               holder.pollOption.setText(pollList.get(position).getPollOptions());
//
//           } else {
//               holder.pollLayout.setVisibility(View.GONE);
//               holder.unpolledPoll.setVisibility(View.VISIBLE);
//               holder.unpolledPoll.setText(pollList.get(position).getPollOptions());
//           }



    }

    private void setdata(int adapterPosition) {
    }


    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pollOptiontxt,unpolledPoll,pollOptiontxtOfunpolled;
        TextView pollPercentage,pollpercentageOfunpolled;
        ProgressBar pollprogressbar,progressBarOfUnpoled;
        CardView pollLayout,pollLayoutOfUnpolled;
        LinearLayout pollMainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pollOptiontxt=itemView.findViewById(R.id.pollOption);
            pollPercentage=itemView.findViewById(R.id.pollPercentage);
            pollprogressbar=itemView.findViewById(R.id.pollProgressbar);
            pollLayout=itemView.findViewById(R.id.pollLayout);
            unpolledPoll=itemView.findViewById(R.id.unpolledPollLayout);
            pollMainLayout=itemView.findViewById(R.id.pollMainLayout);

            pollpercentageOfunpolled=itemView.findViewById(R.id.pollPercentageOfUnpolled);
            progressBarOfUnpoled=itemView.findViewById(R.id.pollProgressbarOfUnpolled);
            pollLayoutOfUnpolled=itemView.findViewById(R.id.pollLayoutOfUnpolled);
            pollOptiontxtOfunpolled=itemView.findViewById(R.id.pollOptionOfUnpolled);


           pollMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(pollLayout.getVisibility()!=View.VISIBLE) {
//                        pollLayout.setVisibility(View.VISIBLE);
//                        unpolledPoll.setVisibility(View.GONE);
                        DocumentReference userpollref = FirebaseFirestore.getInstance().document("Polls" + "/" + pollId + "/" + "UserPolls" + "/" + userphonenum);
//                    pollList.get(getAdapterPosition()).setIspolled(!pollList.get(getAdapterPosition()).getIspolled());
                        UserPollsModel userPollsModel = new UserPollsModel(getAdapterPosition(), true);
                        userpollref.set(userPollsModel);
                        lastpollselected = pollselected;
                        pollselected = getAdapterPosition();
                        notifyItemChanged(getAdapterPosition());
//                        notifyItemChanged(lastpollselected);
                        notifyDataSetChanged();
//                    }
//                    else{
//                        pollLayout.setVisibility(View.GONE);
//                        unpolledPoll.setVisibility(View.VISIBLE);

//                        DocumentReference userpollref = FirebaseFirestore.getInstance().document("Polls" + "/" + pollId + "/" + "UserPolls" + "/" + userphonenum);
////                    pollList.get(getAdapterPosition()).setIspolled(!pollList.get(getAdapterPosition()).getIspolled());
//                        UserPollsModel userPollsModel = new UserPollsModel(getAdapterPosition(), true);
//                        userpollref.set(userPollsModel);
//                        lastpollselected = pollselected;
//                        pollselected = getAdapterPosition();
//                        notifyItemChanged(getAdapterPosition());
//                        notifyItemChanged(lastpollselected);

//                    }

                    Log.d("poll", "onClick: "+pollselected);
                }
            });
        }
    }
}
