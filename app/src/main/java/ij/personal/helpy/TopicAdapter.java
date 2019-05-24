package ij.personal.helpy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ij.personal.helpy.Models.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private String[] mDataset;
    private List<Topic> mClassTopics;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        // each data item
        public TextView txtTopicTitle;
        public TextView txtTopicSubject;

        // Constructor
        public TopicViewHolder(View v) {
            super(v);
            txtTopicTitle = v.findViewById(R.id.txtTopicTitle);
            txtTopicSubject = v.findViewById(R.id.txtTopicSubject);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public TopicAdapter(String[] myDataset) {
    public TopicAdapter(List<Topic> classTopics) {
        mClassTopics = classTopics;
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
        holder.txtTopicSubject.setText(String.valueOf(mClassTopics.get(position).getIdSubject()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mClassTopics.size();
    }
}