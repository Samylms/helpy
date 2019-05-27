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


public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Topic> mClassTopics;
    private Context mContext;
    private int loggedStudentId;
    private int proposalRequestCount;

    public TopicAdapter(List<Topic> classTopics, Context mContext) {
        mClassTopics = classTopics;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topic_card, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.topic_header, parent, false);
            return new VHHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            //cast holder to VHItem and set data
            VHItem vhItem = (VHItem) holder;
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            vhItem.txtTopicTitle.setText(getItem(position).getTitle());
            vhItem.txtTopicSubject.setText(String.valueOf(getItem(position).getTopicSubjectName(mContext)));

            final Topic topic = getItem(position);
            proposalRequestCount = 0;
            loggedStudentId = 1;
            if (topic.getIdTopic() != 0) { // if idTopic = 0, we are with static data
                List<Request> topicRequests = topic.getTopicRequests(mContext);
                for (Request request : topicRequests) {
                    if (request.getType().equals("Proposition")) {
                        proposalRequestCount += 1;
                    } else {
                        if (request.getIdStudent() == loggedStudentId) {
                            vhItem.checkBoxRequest.setChecked(true);
                        }
                    }
                }
            }
            vhItem.txtRequestQty.setText(String.valueOf(proposalRequestCount));

            // add click listener to open ContactActivity
            vhItem.lytTopicCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (mContext, Propose_Activity.class);
                    intent.putExtra("idTopic", topic.getIdTopic());
                    mContext.startActivity(intent);
                }
            });
            vhItem.lytIconPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (mContext, Demande_Activity.class);
                    intent.putExtra("idTopic", topic.getIdTopic());
                    mContext.startActivity(intent);
                }
            });

            // handle adding or delete Request concerning the topic
            vhItem.checkBoxRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            VHHeader vhHeader = (VHHeader) holder;

        }
    }

    @Override
    public int getItemCount() {
        return mClassTopics.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private Topic getItem(int position) {
        return mClassTopics.get(position - 1);
    }

    class VHItem extends RecyclerView.ViewHolder {
        // each data item
        public TextView txtTopicTitle;
        public TextView txtTopicSubject;
        public CheckBox checkBoxRequest;
        public TextView txtRequestQty;
        public LinearLayout lytTopicCard;
        public FrameLayout lytIconPerson;

        public VHItem(View itemView) {
            super(itemView);
            txtTopicTitle = itemView.findViewById(R.id.txtTopicTitle);
            txtTopicSubject = itemView.findViewById(R.id.txtTopicSubject);
            checkBoxRequest = itemView.findViewById(R.id.checkBoxRequest);
            txtRequestQty = itemView.findViewById(R.id.txtRequestQty);
            lytTopicCard = itemView.findViewById(R.id.lytTopicCard);
            lytIconPerson = itemView.findViewById(R.id.lytIconPerson);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        public VHHeader(View itemView) {
            super(itemView);
        }
    }
}