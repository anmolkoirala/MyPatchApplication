package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalNearYouAdapter extends RecyclerView.Adapter<ProfessionalNearYouAdapter.NearYouHolder> implements Filterable {
    private List<ProfessionalModel> listData;
    List<ProfessionalModel> myprolistfull = new ArrayList<>();
    LayoutInflater dialoginflater;
    Context context;
    StorageReference userImageRef;

    TextInputLayout name,address,contact,notes;
    AppCompatButton buttonaddhire;
    NestedScrollView nestedSV;

    public ProfessionalNearYouAdapter(List<ProfessionalModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
        myprolistfull = new ArrayList<>(listData);
    }

    @NonNull
    @Override
    public NearYouHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professionalnearyou_list, parent, false);
        NearYouHolder nearYouHolder = new NearYouHolder(view);
        return nearYouHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NearYouHolder holder, int position) {
        final ProfessionalModel ld = listData.get(position);
        holder.fullname.setText(ld.getFullname());
        holder.address.setText(ld.getAddress());
        holder.category.setText(ld.getCategory());

        userImageRef = FirebaseStorage.getInstance().getReference(ld.getId());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.userimage);
            }
        });

        final String number = ld.getPhonenumber();

        holder.btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +number));
                context.startActivity(callIntent);
                Toast.makeText(context, number.toString(), Toast.LENGTH_SHORT).show();
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

    public static class NearYouHolder extends RecyclerView.ViewHolder {
        TextView fullname, gender, category, address;
        AppCompatButton btncall;
        ImageView userimage;

        public NearYouHolder(@NonNull View itemView) {
            super(itemView);

            btncall   = itemView.findViewById(R.id.pny_call);
            userimage = itemView.findViewById(R.id.pny_image);
            fullname  = itemView.findViewById(R.id.pny_name);
            category  = itemView.findViewById(R.id.pny_category);
            address   = itemView.findViewById(R.id.pny_address);
        }
    }
}
