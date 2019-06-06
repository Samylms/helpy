package ij.personal.helpy.TopicList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ij.personal.helpy.Contact_Activity.ContactActivity;

import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Models.Topic;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;


public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Topic> mClassTopics;
    private Context mContext;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VHItem) {
            //cast holder to VHItem and set data
            VHItem vhItem = (VHItem) holder;
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            vhItem.txtTopicTitle.setText(getItem(position).getTitle());
            vhItem.txtTopicSubject.setText(String.valueOf(getItem(position).getTopicSubjectName(mContext)));

            final Topic topic = getItem(position);
            proposalRequestCount = 0;

            // get request only if server is OK
            if (Prefs.isServerOK(mContext)) {
                List<Request> topicRequests = topic.getTopicRequests(mContext);
                // count the number of proposition on this topic
                for (Request request : topicRequests) {
                    // test selon intent
                    if (!TopicListActivity.proposition) {
                        if (request.getType().equals("Proposition")) {
                            proposalRequestCount += 1;

                            // check the box if student has a request on this topic
                        } else if (request.getIdStudent() == Prefs.getStudentId(mContext)) {
                            vhItem.checkBoxRequestRed.setChecked(true);
                        }
                    } else {
                        if (request.getType().equals("demande")) {
                            proposalRequestCount += 1;

                            // check the box if student has a request on this topic
                        } else if (request.getIdStudent() == Prefs.getStudentId(mContext)) {
                            vhItem.checkBoxRequestRed.setChecked(true);
                        }
                    }
                }
                vhItem.txtRequestQtyGreen.setText(String.valueOf(proposalRequestCount));
            } else {
                vhItem.txtRequestQtyGreen.setText("3");
                vhItem.txtRequestQtyRed.setText("4");
            }

            vhItem.icPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContactActivity.class);
                    intent.putExtra("idTopic", topic.getIdTopic());
                    intent.putExtra("topicTitle", topic.getTitle());
                    if (TopicListActivity.proposition) {
                        intent.putExtra("type", "proposition");
                    } else {
                        intent.putExtra("type", "demande");
                    }
                    mContext.startActivity(intent);
                }
            });

            if (TopicListActivity.proposition) {
                vhItem.icPerson.setImageResource(R.drawable.ic_persons_red);
                vhItem.checkBoxRequestRed.setVisibility(View.GONE);
                vhItem.checkBoxRequestGreen.setVisibility(View.VISIBLE);
//                vhItem.txtRequestQtyGreen.setBackgroundResource(R.drawable.button_red);
            }
            vhItem.txtRequestQtyRed.setVisibility(View.GONE);
            vhItem.txtRequestQtyGreen.setVisibility(View.GONE);

            // display icon_person only if count > 1
            if (Prefs.isServerOK(mContext)){
                // todo: handle display
            }else{
                if (position == 2 || position == 3){
                    vhItem.icPerson.setVisibility(View.GONE);
                }
            }

            // handle adding or delete Request concerning the topic
            vhItem.checkBoxRequestRed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // todo: add
                        if (Prefs.isServerOK(mContext)) {
                            topic.addRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), "Demande");
                        }
                        //Toast
                        Toast.makeText(mContext, "Vous êtes ajouté comme demandeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                    } else {
                        // todo: delete
                        if (Prefs.isServerOK(mContext)) {

                        }
                        //Toast
                        Toast.makeText(mContext, "Votre demande d'aide est supprimée.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            vhItem.checkBoxRequestGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // todo: add
                        if (Prefs.isServerOK(mContext)) {
                            topic.addRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), "Demande");
                        }
                        //Toast
                        Toast.makeText(mContext, "Vous êtes ajouté comme proposeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                    } else {
                        // todo: delete
                        if (Prefs.isServerOK(mContext)) {

                        }
                        //Toast
                        Toast.makeText(mContext, "Votre proposition d'aide est supprimée.", Toast.LENGTH_LONG).show();
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
        public CheckBox checkBoxRequestRed;
        public CheckBox checkBoxRequestGreen;
        public TextView txtRequestQtyGreen;
        public TextView txtRequestQtyRed;
        public LinearLayout lytTopicCard;
        public FrameLayout lytIconPerson;
        public ImageView icPerson;

        public VHItem(View itemView) {
            super(itemView);
            txtTopicTitle = itemView.findViewById(R.id.txtTopicTitle);
            txtTopicSubject = itemView.findViewById(R.id.txtTopicSubject);
            checkBoxRequestRed = itemView.findViewById(R.id.checkBoxRequest);
            checkBoxRequestGreen = itemView.findViewById(R.id.checkBoxRequestGreen);
            txtRequestQtyGreen = itemView.findViewById(R.id.txtRequestQtyGreen);
            txtRequestQtyRed = itemView.findViewById(R.id.txtRequestQtyRed);
            lytTopicCard = itemView.findViewById(R.id.lytTopicCard);
            lytIconPerson = itemView.findViewById(R.id.lytIconPerson);
            icPerson = itemView.findViewById(R.id.icPersonGreen);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        public VHHeader(View itemView) {
            super(itemView);
        }
    }
}