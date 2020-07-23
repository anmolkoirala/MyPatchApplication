package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
import com.example.mypatchapplication.Helperclass.Model.UserHelperClass;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.UserHolder> implements Filterable {
    private List<UserHelperClass> listData;
    List<UserHelperClass> myuserlistfull;
    LayoutInflater dialoginflater;
    Context context;
    StorageReference userImageRef;

    TextInputLayout name,address,contact,notes;

    NestedScrollView nestedSV;
    SessionHolder sessionHolder;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    String responsestat, responsemsg,reqdate,review;
    ImageView alertBack;

    public UserRVAdapter(List<UserHelperClass> listData, Context context) {
        this.listData = listData;
        this.context = context;
        myuserlistfull = new ArrayList<>(listData);
    }

    @NonNull
    @Override
    public UserRVAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        UserRVAdapter.UserHolder userHolder = new UserRVAdapter.UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserRVAdapter.UserHolder holder, int position) {
        final UserHelperClass ld = listData.get(position);

        holder.fullname.setText(ld.getFullname());
        holder.address.setText(ld.getAddress());
        holder.phone.setText(ld.getPhonenumber());
        holder.email.setText(ld.getEmail());

        userImageRef = FirebaseStorage.getInstance().getReference(ld.getId());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.userimage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    @Override
    public Filter getFilter() {
        return userFliter;
    }

    private Filter userFliter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<UserHelperClass> filiteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filiteredList.addAll(myuserlistfull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (UserHelperClass useritem : myuserlistfull) {
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

    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView fullname, phone, email, address;
        ImageView userimage;


        public UserHolder(@NonNull View itemView) {
            super(itemView);

            userimage     = itemView.findViewById(R.id.search_image);
            fullname      = itemView.findViewById(R.id.tv_fullname);
            phone      = itemView.findViewById(R.id.tv_phonenumber);
            email      = itemView.findViewById(R.id.tv_useremail);
            address       = itemView.findViewById(R.id.tv_address);

        }
    }

}