package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.MyWorks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class MyworksAdapter extends BaseExpandableListAdapter {

    private List<String> lstGroups;
    private HashMap<String, List<HireUsModel>> lstItemsGroups;
    private Context context;
    LayoutInflater dialoginflater;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    DatabaseReference professionalref = rootNode.getReference("Professionals");
    TextInputLayout userReview,popupchargedprice;
    TextView maintitle,popuptitle,popupDescp;
    RatingBar ratingBar;
    int currentrating = 0;
    ReviewModel reviewModel;
    SessionHolder sessionHolder;
    Button popupsendreview;
    int avgRate, dbpersonCount;

    public MyworksAdapter(Context context, List<String> lstGroups, HashMap<String, List<HireUsModel>> lstItemsGroups) {
        this.lstGroups = lstGroups;
        this.lstItemsGroups = lstItemsGroups;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return lstGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lstItemsGroups.get(getGroup(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lstGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lstItemsGroups.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
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
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.mywork_grouplist, null);
        }

        TextView workTitle   = convertView.findViewById(R.id.workgroup_title);
        TextView workNumber  = convertView.findViewById(R.id.workgroup_number);

        workTitle.setText((String) getGroup(groupPosition));
        workNumber.setText(String.valueOf(getChildrenCount(groupPosition)));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.mywork_item_list, null);
        }

        final TextView itemName     = convertView.findViewById(R.id.work_item_profname);
        TextView itemIssueDes       = convertView.findViewById(R.id.work_item_issuedesc);
        TextView itemDate           = convertView.findViewById(R.id.work_item_date);
        TextView itemResponsestat   = convertView.findViewById(R.id.work_item_response);
        TextView itemCategory       = convertView.findViewById(R.id.work_item_category);
        Button   btncancel          = convertView.findViewById(R.id.work_item_cancel);
        Button   btnreview          = convertView.findViewById(R.id.work_item_review);
        LinearLayout btnlayout      = convertView.findViewById(R.id.work_itemb_btnlayout);
        btnlayout.setVisibility(View.GONE);

        //creating session instance to get the current user data
        sessionHolder = new SessionHolder(context);
        HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
        final String currentID       = userdetails.get(SessionHolder.KEY_USERID);
        final String currentname     = userdetails.get(SessionHolder.KEY_FULLNAME);

        final HireUsModel hireUsModel = (HireUsModel) getChild(groupPosition, childPosition);
        String stat   = hireUsModel.getResponsestatus();
        String review = hireUsModel.getReviewed();

        if(stat != null && stat.equals("requested")){
            btnlayout.setVisibility(View.VISIBLE);
            btnreview.setVisibility(View.GONE);
        }else if(stat != null && stat.equals("Processing")){
            btnlayout.setVisibility(View.GONE);
            btncancel.setVisibility(View.GONE);
            btnreview.setVisibility(View.GONE);
        }else {
            btncancel.setVisibility(View.GONE);
            if(review != null && review.equals("reviewed")){
                btnlayout.setVisibility(View.GONE);
                btnreview.setVisibility(View.GONE);
            }else{
                btnlayout.setVisibility(View.VISIBLE);
                btnreview.setVisibility(View.VISIBLE);
            }
        }

        final MyWorks myWorks = new MyWorks();
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(hireUsModel.getUserID()).child(hireUsModel.getInnerId()).removeValue();
                //after removing the child, refresh the activity.
                Intent intent = new Intent(context,MyWorks.class);
                context.startActivity(intent);
                //this line will prevent going back to same activity where duplicate date is displayed
                ((MyWorks)context).finish();
                Toast.makeText(context, "Your Hired work has been removed", Toast.LENGTH_SHORT).show();
            }
        });

        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View dialogView   = dialoginflater.inflate(R.layout.popup_review, null);
                View titleView    = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                maintitle         = (TextView) titleView.findViewById(R.id.main_popup_header);

                userReview        = (TextInputLayout) dialogView.findViewById(R.id.popup_usertext);
                popuptitle        = (TextView) dialogView.findViewById(R.id.popup_title);
                popupsendreview   = (Button) dialogView.findViewById(R.id.popup_sendReview);
                popupchargedprice = (TextInputLayout) dialogView.findViewById(R.id.popup_userprice);
                popupDescp        = (TextView) dialogView.findViewById(R.id.popup_descp);
                popupDescp.setVisibility(View.GONE);
                //setting up the title
                maintitle.setText(R.string.yourreviews);
                popuptitle.setText("Review: " + hireUsModel.getProfessionalname());

                final Query profrefer = rootNode.getReference("Professionals").orderByChild("id").equalTo(hireUsModel.getProfessionalID());
                profrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for(DataSnapshot datas : dataSnapshot.getChildren()) {
                                 String rate        = datas.child("avgrating").getValue().toString();
                                 String personcount = datas.child("ratingcount").getValue().toString();
                                 avgRate      = Integer.parseInt(rate);
                                 dbpersonCount = Integer.parseInt(personcount);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(myWorks, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //setting up the rating bar
                ratingBar   = (RatingBar) dialogView.findViewById(R.id.popup_rating);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        int userrates = (int) rating;
                        currentrating = (int) ratingBar.getRating();

                        if(userrates == 1){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_one);
                        }
                        else if(userrates == 2){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_two);
                        }
                        else if(userrates == 3){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_three);
                        }
                        else if(userrates == 4){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_four);
                        }
                        else if(userrates == 5){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_five);
                        }
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCustomTitle(titleView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                popupsendreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         int inputCount = dbpersonCount + 1;
                         int inputRate = (avgRate + currentrating)/inputCount;
                        FirebaseDatabase ratingDB   = FirebaseDatabase.getInstance();
                        DatabaseReference ratingRef = ratingDB.getReference("Reviews");

                        if(currentrating == 0){
                            Toast.makeText(context, "Please rate the Professional first.", Toast.LENGTH_SHORT).show();
                        }else{
                            //saving the average rating and number of times professional has been rated in professional reference
                            professionalref.child(hireUsModel.getProfessionalID()).child("ratingcount").setValue(inputCount);
                            professionalref.child(hireUsModel.getProfessionalID()).child("avgrating").setValue(inputRate);

                            //saving the review in review reference
                            String userreview = userReview.getEditText().getText().toString();
                            String chargedpp = popupchargedprice.getEditText().getText().toString();
                            int finapp       = Integer.parseInt(chargedpp);
                            reviewModel = new ReviewModel(currentname,currentID,hireUsModel.getIssuedescription(),hireUsModel.getProfessionalname(),hireUsModel.getProfessionalID(),hireUsModel.getProfessionalcategory(),userreview,currentrating,finapp);
                            ratingRef.child(currentID).push().setValue(reviewModel);
                            Toast.makeText(context, "Your review has been noted. Thank you!", Toast.LENGTH_SHORT).show();

                            //updating the hirelist reference to denoted the hired person has been rated.
                            reference.child(hireUsModel.getUserID()).child(hireUsModel.getInnerId()).child("reviewed").setValue("reviewed");
                            Intent intent = new Intent(context,MyWorks.class);
                            context.startActivity(intent);

                            //this line will prevent going back to same activity where duplicate date is displayed
                            ((MyWorks)context).finish();
                            dialog.dismiss();
                        }
                     }
                });

            }
        });
        itemName.setText("To: " + hireUsModel.getProfessionalname());
        itemIssueDes.setText(hireUsModel.getIssuedescription());
        itemDate.setText("Date: " +hireUsModel.getRequesteddate());
        itemResponsestat.setText("Status: " + hireUsModel.getResponsestatus());
        itemCategory.setText("Category: " + hireUsModel.getProfessionalcategory());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // returns if the subitem (item of group) can be selected
        return true;
    }
}
