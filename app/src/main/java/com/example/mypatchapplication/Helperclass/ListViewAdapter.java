package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mypatchapplication.Common.Search.SearchCategoryActivity;
import com.example.mypatchapplication.Common.onBoarding;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends BaseExpandableListAdapter {

    private List<String> lstGroups;
    private HashMap<String, List<ProfessionalModel>> lstItemsGroups;
    private Context context;
    TextInputLayout name, contact, address,notes;
    LayoutInflater dialoginflater;
    StorageReference userImageReference;
    AppCompatButton buttonaddhire;
    NestedScrollView nestedSV;

    SessionHolder sessionHolder;
    DatePicker reservedDate;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    HireUsModel hireUsModel;
    String responsestat, responsemsg,reqdate,review;
    ImageView listBack;

    public ListViewAdapter(Context context, List<String> groups, HashMap<String, List<ProfessionalModel>> itemsGroups) {
        // initialize class variables
        this.context = context;
        lstGroups = groups;
        lstItemsGroups = itemsGroups;
    }

    @Override
    public int getGroupCount() {
        // returns groups count
        return lstGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // returns items count of a group
        return lstItemsGroups.get(getGroup(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // returns a group
        return lstGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // returns a group item
        return lstItemsGroups.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // return the group id
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // returns the item id of group
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // returns if the ids are specific ( unique for each group or item)
        // or relatives
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // create main items (groups)
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_group_list, null);
        }

        TextView catTitle   = convertView.findViewById(R.id.allcategory_title);
        TextView catNumbers = convertView.findViewById(R.id.allcategories_number);
        ImageView catImage = convertView.findViewById(R.id.category_group_image);

        String gname = (String) getGroup(groupPosition);

        if (gname.equals("Cleaners")) {
            Glide.with(context).load(R.drawable.cleaners_cat).into(catImage);
        } else if (gname.equals("Electrician")) {
            Glide.with(context).load(R.drawable.electrician_cat).into(catImage);
        } else if (gname.equals("Plumbers")) {
            Glide.with(context).load(R.drawable.plumbers_cat).into(catImage);
        } else if (gname.equals("Painters")) {
            Glide.with(context).load(R.drawable.painters_cat).into(catImage);
        } else if (gname.equals("Roofers")) {
            Glide.with(context).load(R.drawable.roofers_cat).into(catImage);
        } else if (gname.equals("Pest Control")) {
            Glide.with(context).load(R.drawable.pests_cat).into(catImage);
        } else if (gname.equals("Tank Cleaners")) {
            Glide.with(context).load(R.drawable.poo_clearnes_cat).into(catImage);
        }

        catTitle.setText((String) getGroup(groupPosition));
        catNumbers.setText(String.valueOf(getChildrenCount(groupPosition)));


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // create the subitems (items of groups)
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_item_list, null);
        }

        TextView itemName             = convertView.findViewById(R.id.cat_item_name);
        TextView itemAddress          = convertView.findViewById(R.id.cat_item_address);
        RatingBar itemrating       =  convertView.findViewById(R.id.catlist_ratingbar);

        Button itemButton             = convertView.findViewById(R.id.cat_hireme);
        final ImageView userdispImage = convertView.findViewById(R.id.cat_item_image);
        final ProfessionalModel professionals = (ProfessionalModel) getChild(groupPosition, childPosition);

        //pop up
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mview) {
                dialoginflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View dialogView = dialoginflater.inflate(R.layout.hire_professional, null);
                View titleView = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                dialogBuilder.setView(dialogView);

                name    = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourname);
                contact = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourcontact);
                address = (TextInputLayout) dialogView.findViewById(R.id.hire_us_youraddress);
                notes   = (TextInputLayout) dialogView.findViewById(R.id.hire_us_tellmore);
                listBack    = (ImageView) dialogView.findViewById(R.id.dialog_back);
                reservedDate = (DatePicker) dialogView.findViewById(R.id.hireus_date);
                reservedDate.setMinDate(System.currentTimeMillis() - 1000);

                buttonaddhire = (AppCompatButton) dialogView.findViewById(R.id.hire_us_sendbutton);
                nestedSV = (NestedScrollView) dialogView.findViewById(R.id.nestedScrollView);

                dialogBuilder.setCustomTitle(titleView);

                sessionHolder = new SessionHolder(context);
                HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
                final String currentID     = userdetails.get(SessionHolder.KEY_USERID);
                final String currentname   = userdetails.get(SessionHolder.KEY_FULLNAME);
                String currentaddress      = userdetails.get(SessionHolder.KEY_ADDRESS);
                String currentcontacts  = userdetails.get(SessionHolder.KEY_PHONENUMBER);

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

                        hireUsModel = new HireUsModel(sendname,currentID,sendaddress,professionals.getFullname(),professionals.getCategory(),professionals.getId(),sendphone,professionals.getPhonenumber(),responsestat,issuedesc,reqdate,responsemsg,review);
                        reference.child(currentID).push().setValue(hireUsModel);

                        Toast.makeText(context, "Your Request has been sent. We will contact you soon.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                listBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        itemName.setText(professionals.getFullname());
        itemAddress.setText(professionals.getAddress());
        itemrating.setRating(professionals.getAvgrating());
        userImageReference = FirebaseStorage.getInstance().getReference(professionals.getId());

        userImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(userdispImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Glide.with(context).load(R.drawable.patchappprofile).into(userdispImage);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // returns if the subitem (item of group) can be selected
        return true;
    }



}