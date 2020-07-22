package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfessionalRVAdapter extends RecyclerView.Adapter<ProfessionalRVAdapter.ProfessionalHolder> implements Filterable {
    private List<ProfessionalModel> listData;
    List<ProfessionalModel> myprolistfull;
    LayoutInflater dialoginflater;
    Context context;
    StorageReference userImageRef;

    TextInputLayout name,address,contact,notes;

    AppCompatButton buttonaddhire;
    NestedScrollView nestedSV;
    DatePicker reservedDate;
    SessionHolder sessionHolder;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    HireUsModel hireUsModel;
    String responsestat, responsemsg,reqdate,review;
    ImageView alertBack;

    public ProfessionalRVAdapter(List<ProfessionalModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
        myprolistfull = new ArrayList<>(listData);
    }

    @NonNull
    @Override
    public ProfessionalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professional_list, parent, false);
        ProfessionalHolder professionalHolder = new ProfessionalHolder(view);
        return professionalHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfessionalHolder holder, int position) {
        final ProfessionalModel ld = listData.get(position);

        holder.fullname.setText(ld.getFullname());
        holder.address.setText(ld.getAddress());
        holder.category.setText(ld.getCategory());
        holder.prating.setRating(ld.getAvgrating());

        userImageRef = FirebaseStorage.getInstance().getReference(ld.getId());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(holder.userimage);
                }
        });


        holder.btnhire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mview) {

                dialoginflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View titleView = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                View dialogView = dialoginflater.inflate(R.layout.hire_professional, null);
                dialogBuilder.setView(dialogView);

                name         = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourname);
                contact      = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourcontact);
                address      = (TextInputLayout) dialogView.findViewById(R.id.hire_us_youraddress);
                notes        = (TextInputLayout) dialogView.findViewById(R.id.hire_us_tellmore);
                alertBack    = (ImageView) dialogView.findViewById(R.id.dialog_back);
                reservedDate = (DatePicker) dialogView.findViewById(R.id.hireus_date);
                reservedDate.setMinDate(System.currentTimeMillis() - 1000);

                buttonaddhire = (AppCompatButton) dialogView.findViewById(R.id.hire_us_sendbutton);
                nestedSV      = dialogView.findViewById(R.id.nestedScrollView);

                sessionHolder = new SessionHolder(context);
                HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
                final String currentID        = userdetails.get(SessionHolder.KEY_USERID);
                final String currentname      = userdetails.get(SessionHolder.KEY_FULLNAME);
                String currentaddress   = userdetails.get(SessionHolder.KEY_ADDRESS);
                String currentcontacts  = userdetails.get(SessionHolder.KEY_PHONENUMBER);

                dialogBuilder.setCustomTitle(titleView);


                name.getEditText().setText(currentname);
                address.getEditText().setText(currentaddress);
                contact.getEditText().setText(currentcontacts);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                buttonaddhire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String issuedesc    = notes.getEditText().getText().toString().trim();
                        String sendname     = name.getEditText().getText().toString().trim();
                        String sendaddress  = address.getEditText().getText().toString().trim();
                        String sendphone    = contact.getEditText().getText().toString().trim();
                        int day             = reservedDate.getDayOfMonth();
                        int month           = reservedDate.getMonth();
                        int year            = reservedDate.getYear();
                        reqdate      = day +"/"+month+"/"+year;
                        responsestat = "requested";
                        responsemsg  = "";
                        review  = "notreviewed";

                        hireUsModel = new HireUsModel(sendname,currentID,sendaddress,ld.getFullname(),ld.getCategory(),ld.getId(),sendphone,ld.getPhonenumber(),responsestat,issuedesc,reqdate,responsemsg,review);
                        reference.child(currentID).push().setValue(hireUsModel);

                        Toast.makeText(context, "Your Request has been sent. We will contact you soon.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                alertBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    @Override
    public Filter getFilter() {
        return professionalFliter;
    }

    private Filter professionalFliter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ProfessionalModel> filiteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filiteredList.addAll(myprolistfull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ProfessionalModel useritem : myprolistfull) {
                    if (useritem.getFullname().toLowerCase().contains(filterPattern)) {
                        filiteredList.add(useritem);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filiteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listData.clear();
            listData.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ProfessionalHolder extends RecyclerView.ViewHolder {
        TextView fullname, gender, category, address;
        AppCompatButton btnhire;
        ImageView userimage;
        RatingBar prating;


        public ProfessionalHolder(@NonNull View itemView) {
            super(itemView);

            btnhire       = itemView.findViewById(R.id.cat_hireme);
            userimage     = itemView.findViewById(R.id.search_image);
            fullname      = itemView.findViewById(R.id.tv_fullname);
            category      = itemView.findViewById(R.id.tv_category);
            address       = itemView.findViewById(R.id.tv_address);
            prating       =  itemView.findViewById(R.id.proflist_ratingbar);

        }
    }

}