package ij.personal.helpy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Models.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<Topic> mClassTopics;
    private Context context;
    private int loggedStudentId;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        // each data item
        public TextView txtTopicTitle;
        public TextView txtTopicSubject;
        public CheckBox checkBoxRequest;
        public TextView txtRequestQty;

        // Constructor
        public TopicViewHolder(View v) {
            super(v);
            txtTopicTitle = v.findViewById(R.id.txtTopicTitle);
            txtTopicSubject = v.findViewById(R.id.txtTopicSubject);
            checkBoxRequest = v.findViewById(R.id.checkBoxRequest);
            txtRequestQty = v.findViewById(R.id.txtRequestQty);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public TopicAdapter(String[] myDataset) {
    public TopicAdapter(List<Topic> classTopics, Context context) {
        mClassTopics = classTopics;
        this.context = context;
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
        holder.txtTopicSubject.setText(String.valueOf(mClassTopics.get(position).getTopicSubjectName(context)));

        Topic topic = mClassTopics.get(position);
        List<Request> topicRequests = topic.getTopicRequests(context);
        int typeProposalCount = 0;
        loggedStudentId = 1;
        for (Request request: topicRequests){
            if (request.getType().equals("Proposition")){
                typeProposalCount += 1;
            }else{
                if(request.getIdStudent() == loggedStudentId){
                    holder.checkBoxRequest.setChecked(true);
                }
            }
        }
        holder.txtRequestQty.setText(String.valueOf(typeProposalCount));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mClassTopics.size();
    }
}