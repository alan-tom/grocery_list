package com.example.mygrocerylist.Activities.UI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mygrocerylist.Activities.Activities.DetailsActivity;
import com.example.mygrocerylist.Activities.Activities.ListActivity;
import com.example.mygrocerylist.Activities.Data.DataBaseHandler;
import com.example.mygrocerylist.Activities.Model.GroceryItem;
import com.example.mygrocerylist.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<GroceryItem> groceryItemList;
    private DataBaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<GroceryItem> groceryItemList) {
        this.context = context;
        this.groceryItemList = groceryItemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        GroceryItem groceryItem = groceryItemList.get(i);
        viewHolder.groceryName.setText(groceryItem.getName());
        viewHolder.groceryQty.setText(groceryItem.getQuantity());
        viewHolder.groceryDate.setText(groceryItem.getDate());
        viewHolder.groceryID = groceryItem.getID();
    }

    @Override
    public int getItemCount() {
        return groceryItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView groceryName;
        private TextView groceryQty;
        private TextView groceryDate;
        private Button groceryEdit;
        private Button groceryDelete;
        private int groceryID;


        public ViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            groceryName = (TextView) itemView.findViewById(R.id.groceryName);
            groceryQty = (TextView) itemView.findViewById(R.id.groceryQuantityID);
            groceryDate = (TextView) itemView.findViewById(R.id.groceryDateID);
            groceryEdit = (Button) itemView.findViewById(R.id.groceryEditID);
            groceryDelete = (Button) itemView.findViewById(R.id.groceryDeleteID);

            db = new DataBaseHandler(context);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemID = getAdapterPosition();
                    GroceryItem grocer = groceryItemList.get(itemID);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("Name", grocer.getName());
                    intent.putExtra("Quantity", grocer.getQuantity());
                    context.startActivity(intent);
                }
            });

            groceryEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPopUp();
                }
            });
            groceryDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteAlert();
                }
            });
        }
        public void createPopUp(){
            dialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup_layout, null);

            TextView txt1 = (TextView) view.findViewById(R.id.enterText);
            final EditText txt2 = (EditText) view.findViewById(R.id.enterName);
            final EditText txt3 = (EditText) view.findViewById(R.id.enterQuantity);
            Button save = (Button) view.findViewById(R.id.saveButton);

            txt1.setText("Modify the Grocery Item");
            txt2.setText(groceryName.getText());
            txt3.setText(groceryQty.getText());

            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();


            final GroceryItem groceryItem = groceryItemList.get(getAdapterPosition());


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = txt2.getText().toString();
                    String quant = txt3.getText().toString();
                    groceryItem.setName(name);
                    groceryItem.setQuantity(quant);
                    db.updateGrocery(groceryItem);
                    dialog.dismiss();
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
        public void DeleteAlert(){
            dialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);
            Button mButNo = view.findViewById(R.id.butNo);
            Button mButYes = view.findViewById(R.id.butYes);

            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            mButNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            mButYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteGrocery(groceryID);
                    groceryItemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
