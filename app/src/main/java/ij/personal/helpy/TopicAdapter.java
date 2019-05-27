package ij.personal.helpy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ij.personal.helpy.Contact_Activity.Demande_aide.Demande_Activity;
import ij.personal.helpy.Contact_Activity.Propose_aide.Propose_Activity;
import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Models.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<Topic> mClassTopics;
    private Context mContext;
    private int loggedStudentId;
    private int totalProposalRequest;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        // each data item
        public TextView txtTopicTitle;
        public TextView txtTopicSubject;
        public CheckBox checkBoxRequest;
        public TextView txtRequestQty;
        public LinearLayout lytTopicCard;
        public FrameLayout lytIconPerson;

        // Constructor
        public TopicViewHolder(View v) {
            super(v);
            txtTopicTitle = v.findViewById(R.id.txtTopicTitle);
            txtTopicSubject = v.findViewById(R.id.txtTopicSubject);
            checkBoxRequest = v.findViewById(R.id.checkBoxRequest);
            txtRequestQty = v.findViewById(R.id.txtRequestQty);
            lytTopicCard = v.findViewById(R.id.lytTopicCard);
            lytIconPerson = v.findViewById(R.id.lytIconPerson);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public TopicAdapter(String[] myDataset) {
    public TopicAdapter(List<Topic> classTopics, Context mContext) {
        mClassTopics = classTopics;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_card, parent, false);

        TopicViewHolder vh = new TopicViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtTopicTitle.setText(mClassTopics.get(position).getTitle());
        holder.txtTopicSubject.setText(String.valueOf(mClassTopics.get(position).getTopicSubjectName(mContext)));

        final Topic topic = mClassTopics.get(position);
        totalProposalRequest = 0;
        loggedStudentId = 1;
        if (topic.getIdTopic() != 0) { // if idTopic = 0, we are with static data
            List<Request> topicRequests = topic.getTopicRequests(mContext);
            for (Request request : topicRequests) {
                if (request.getType().equals("Proposition")) {
                    totalProposalRequest += 1;
                } else {
                    if (request.getIdStudent() == loggedStudentId) {
                        holder.checkBoxRequest.setChecked(true);
                    }
                }
            }
        }
        holder.txtRequestQty.setText(String.valueOf(totalProposalRequest));

        // add click listener to open ContactActivity
        holder.lytTopicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext, Propose_Activity.class);
                intent.putExtra("idTopic", topic.getIdTopic());
                mContext.startActivity(intent);
            }
        });
        holder.lytIconPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext, Demande_Activity.class);
                intent.putExtra("idTopic", topic.getIdTopic());
                mContext.startActivity(intent);
            }
        });

        // handle adding or delete Request concerning the topic
        holder.checkBoxRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // todo: add
                    if (topic.getIdTopic() == 0) {
                        topic.addRequestOnThisTopic(mContext, loggedStudentId, "Demande");
                    }
                }else{
                    // todo: delete
                    if (topic.getIdTopic() == 0){

                    }

                }
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mClassTopics.size();
    }
}