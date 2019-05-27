package ij.personal.helpy.Contact_Activity.Propose_aide;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ij.personal.helpy.R;

public class ProposeAdapter  extends RecyclerView.Adapter<ProposeAdapter.MyViewHolder>{    //for log
    private static final String Tag = "DemandeAdapter";
    private ArrayList<String> mTextContact = new ArrayList<>();
    private Context mContext;

    public ProposeAdapter(Context mContext,ArrayList<String> mTextContact) {
        this.mTextContact = mTextContact;
        this.mContext = mContext;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView txtContact;
        Button btnContacter;
        RelativeLayout parentLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtContact = itemView.findViewById(R.id.txtdemande);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }


    @Override
    public ProposeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_propose, parent, false);

        ProposeAdapter.MyViewHolder holder = new ProposeAdapter.MyViewHolder(view);
        return  holder;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProposeAdapter.MyViewHolder holder, final int position) {

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag, "onClick: clicked on: " +mTextContact.get(position));
                Toast.makeText(mContext, mTextContact.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTextContact.size();
    }
}