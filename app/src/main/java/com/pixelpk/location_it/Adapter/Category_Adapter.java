package com.pixelpk.location_it.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelpk.location_it.Helper.Category_Model;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.Location_Model;
import com.pixelpk.location_it.Helper.User;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.User.Add_New_Pin;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.Category_ViewHolder>
{
    List<Category_Model>category_models;
    ArrayList<Location_Model> arrayList_model;
    DatabaseHelper databaseHelper;
    Location_Model location_model = new Location_Model();
    Context context;

    public Category_Adapter(List<Category_Model> category_models, Context context)
    {
        this.category_models = category_models;
        this.context = context;
    }

    @Override
    public Category_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_layout, parent, false);
        Category_ViewHolder categoryViewHolder = new Category_ViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Category_ViewHolder holder, final int position)
    {
        Category_Model category_model = category_models.get(position);
//        Location_Model location_model = arrayList_model.get(position);

        holder.category_name.setText(category_model.getCategory_name());

        databaseHelper = new DatabaseHelper(context);

        holder.delete_btn.setOnClickListener(new View.OnClickListener()
        {;
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Category?")
                        .setMessage("Are you sure you want to Delete this Category")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                databaseHelper.delete_Category(category_model);
                                databaseHelper.delete_location(category_models.get(position).getCategory_name());
                                category_models.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                //deleting note

            }
        });

        holder.edit_button.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);
            }
        });

    }

    private void showDialog(int position)
    {
        final EditText category_name;
        ImageView cross;
        Button submit;
        Category_Model category_model = category_models.get(position);
//        Location_Model location_model = location_models.get(position);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        category_name =  dialog.findViewById(R.id.title);
        submit        =  dialog.findViewById(R.id.submit);
        cross         = dialog.findViewById(R.id.img_cancel_dialog);

        category_name.setText(category_models.get(position).getCategory_name());

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (category_name.getText().toString().isEmpty())
                {
                    category_name.setError("Please Enter Category name");
                }
                else
                {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Update Category?")
                            .setMessage("Are you sure you want to Update this Category")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    databaseHelper.update_category_new(category_name.getText().toString(), String.valueOf(category_models.get(position).getCategory_id()));
                                    databaseHelper.update_category_new_loc(category_name.getText().toString(),category_models.get(position).getCategory_name());
                                    category_models.get(position).setCategory_name(category_name.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                    dialog.cancel();
                    //notify list
                }

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return category_models.size();
    }

    public class Category_ViewHolder extends RecyclerView.ViewHolder
    {
        Button edit_button,delete_btn;

        TextView category_name;
        public Category_ViewHolder(View itemView)
        {
            super(itemView);

            category_name = itemView.findViewById(R.id.item_text);

            edit_button = itemView.findViewById(R.id.edit);

            delete_btn = itemView.findViewById(R.id.delete);

        }
    }
}