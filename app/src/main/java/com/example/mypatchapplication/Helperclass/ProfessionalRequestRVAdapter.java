package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Common.Professional.ProfessionalRequestList;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.AddUserReviews;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class ProfessionalRequestRVAdapter extends RecyclerView.Adapter<ProfessionalRequestRVAdapter.RequestHistoryHolder>{
    private List<HireUsModel> historyList;
    Context context;
    LayoutInflater dialoginflater;
    TextView popuptitle, maintitle,popupDescp;
    TextInputLayout updateReview;
    Button sendUpdate;
    StorageReference userImageRef;
    TextInputLayout proRespond;
    Button  btnSendRespond;
    SessionHolder sessionHolder;
    FirebaseDatabase rootNode         = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");


    public ProfessionalRequestRVAdapter(List<HireUsModel> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfessionalRequestRVAdapter.RequestHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professional_request_adapterlayout, parent, false);
        ProfessionalRequestRVAdapter.RequestHistoryHolder requestHistoryHolder = new ProfessionalRequestRVAdapter.RequestHistoryHolder(view);
        return requestHistoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfessionalRequestRVAdapter.RequestHistoryHolder holder, int position) {
        final HireUsModel hireUsModel   = historyList.get(position);
        holder.username.setText(hireUsModel.getUsername());

        holder.address.setText(hireUsModel.getUseraddress());
        holder.number.setText(hireUsModel.getUsernumber());
        holder.issuedescription.setText("Issue Description: "+hireUsModel.getIssuedescription());
        holder.req_date.setText(hireUsModel.getRequesteddate());

        userImageRef = FirebaseStorage.getInstance().getReference(hireUsModel.getUserID());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.userimage);
            }
        });


        holder.btnrespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoginflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View dialogView = dialoginflater.inflate(R.layout.popup_respond_request, null);
                View titleView  = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                maintitle       = (TextView) titleView.findViewById(R.id.main_popup_header);

                //creating session instance to get the current user data
                sessionHolder = new SessionHolder(context);
                HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
                final String currentID       = userdetails.get(SessionHolder.KEY_USERID);
                final String currentname     = userdetails.get(SessionHolder.KEY_FULLNAME);

                proRespond           = (TextInputLayout) dialogView.findViewById(R.id.popup_usertext);
                btnSendRespond = (Button) dialogView.findViewById(R.id.popup_sendRespond);
                //setting up the title
                maintitle.setText("Your Respond");

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCustomTitle(titleView);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                btnSendRespond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String respond= proRespond.getEditText().getText().toString();

                        if(respond == ""){
                            Toast.makeText(context, "Please write a response message.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Your respond has been noted. Thank you!", Toast.LENGTH_SHORT).show();
                            reference.child(hireUsModel.getUserID()).child(hireUsModel.getInnerId()).child("responsemessage").setValue(respond);
                            reference.child(hireUsModel.getUserID()).child(hireUsModel.getInnerId()).child("responsestatus").setValue("adminresponse");
                            Intent intent = new Intent(context, ProfessionalRequestList.class);
                            context.startActivity(intent);
                            //this line will prevent going back to same activity where duplicate date is displayed
                            ((ProfessionalRequestList)context).finish();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public static class RequestHistoryHolder extends RecyclerView.ViewHolder {

        TextView username,  issuedescription, address,number,req_date;
        AppCompatButton btnrespond;
        ImageView userimage;

        public RequestHistoryHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_fullname);
            address = itemView.findViewById(R.id.tv_address);
            number = itemView.findViewById(R.id.tv_number);
            req_date = itemView.findViewById(R.id.tv_requested_date);
            userimage     = itemView.findViewById(R.id.search_image);
            issuedescription = itemView.findViewById(R.id.tv_issue_description);

            btnrespond       = itemView.findViewById(R.id.cat_respond);


        }
    }
}
