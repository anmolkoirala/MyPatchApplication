package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfessionalWorkHistoryRVAdapter extends RecyclerView.Adapter<ProfessionalWorkHistoryRVAdapter.WorkHistoryHolder> {
    private List<HireUsModel> historyList;
    Context context;
    LayoutInflater dialoginflater;
    TextView popuptitle, maintitle, popupDescp;
    TextInputLayout updateReview;
    Button sendUpdate;
    StorageReference userImageRef;
    TextInputLayout proRespond;
    Button btnSendRespond;
    SessionHolder sessionHolder;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");


    public ProfessionalWorkHistoryRVAdapter(List<HireUsModel> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfessionalWorkHistoryRVAdapter.WorkHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professional_work_history_adapterlayout, parent, false);
        ProfessionalWorkHistoryRVAdapter.WorkHistoryHolder workHistoryHolder = new ProfessionalWorkHistoryRVAdapter.WorkHistoryHolder(view);
        return workHistoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfessionalWorkHistoryRVAdapter.WorkHistoryHolder holder, final int position) {
        final HireUsModel hireUsModel = historyList.get(position);
        holder.username.setText(hireUsModel.getUsername());

        holder.address.setText(hireUsModel.getUseraddress());
        holder.number.setText(hireUsModel.getUsernumber());
        holder.issuedescription.setText("Issue Description: " + hireUsModel.getIssuedescription());
        holder.response.setText("Your Response: " + hireUsModel.getResponsemessage());
        holder.req_date.setText(hireUsModel.getRequesteddate());

        userImageRef = FirebaseStorage.getInstance().getReference(hireUsModel.getUserID());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.userimage);
            }
        });





    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public static class WorkHistoryHolder extends RecyclerView.ViewHolder {

        TextView username, issuedescription, address, number, req_date,response;
        ImageView userimage;

        public WorkHistoryHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_fullname);
            address = itemView.findViewById(R.id.tv_address);
            number = itemView.findViewById(R.id.tv_number);
            req_date = itemView.findViewById(R.id.tv_requested_date);
            userimage = itemView.findViewById(R.id.search_image);
            issuedescription = itemView.findViewById(R.id.tv_issue_description);
            response = itemView.findViewById(R.id.tv_your_response);



        }
    }
}