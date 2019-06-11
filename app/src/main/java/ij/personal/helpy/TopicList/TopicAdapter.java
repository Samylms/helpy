package ij.personal.helpy.TopicList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ij.personal.helpy.Contact_Activity.ContactActivity;

import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Models.Topic;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;


public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String BASE_URL = "http://54.37.157.172:3000";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public List<Topic> mClassTopics;
    private Context mContext;
    private int oppositRequestCount;
    private int requestCount;
    private String type;
    public TopicListActivity mTopicListActivity;

    public TopicAdapter(List<Topic> classTopics, Context mContext, String type, TopicListActivity parentActivity) {
        mClassTopics = classTopics;
        this.mContext = mContext;
        this.type = type;
        this.mTopicListActivity = parentActivity;
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
            oppositRequestCount = 0;
            requestCount = 0;

            // get request only if server is OK
            if (Prefs.isServerOK(mContext)) {
                List<Request> topicRequests = topic.getTopicRequests(mContext);
                requestCount = topicRequests.size();
                // count the number of proposition on this topic
                for (Request request : topicRequests) {
                    // test selon intent
                    if (type.equals("Demande")) {
                        if (request.getType().equals("Proposition") && request.getIdStudent() != Prefs.getStudentId(mContext)) {
                            oppositRequestCount += 1;

                            // check the box if student has a request on this topic
                        } else if (request.getType().equals("Demande") && request.getIdStudent() == Prefs.getStudentId(mContext)) {
                            vhItem.checkBoxRequestRed.setChecked(true);
                        }
                    } else {
                        if (request.getType().equals("Demande") && request.getIdStudent() != Prefs.getStudentId(mContext)) {
                            oppositRequestCount += 1;

                            // check the box if student has a request on this topic
                        } else if (request.getType().equals("Proposition") && request.getIdStudent() == Prefs.getStudentId(mContext)) {
                            vhItem.checkBoxRequestGreen.setChecked(true);
                        }
                    }
                }
                vhItem.txtRequestQtyRed.setText(String.valueOf(oppositRequestCount));
                vhItem.txtRequestQtyGreen.setText(String.valueOf(oppositRequestCount));
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
                    intent.putExtra("type", type);
                    mContext.startActivity(intent);
                }
            });

            if (type.equals("Proposition")) {
                vhItem.icPerson.setImageResource(R.drawable.ic_persons_red);
                vhItem.checkBoxRequestRed.setVisibility(View.GONE);
                vhItem.checkBoxRequestGreen.setVisibility(View.VISIBLE);
//                vhItem.txtRequestQtyGreen.setBackgroundResource(R.drawable.button_red);
            }
            vhItem.txtRequestQtyRed.setVisibility(View.GONE);
            vhItem.txtRequestQtyGreen.setVisibility(View.GONE);

            // display icon_person only if count > 0
            if (Prefs.isServerOK(mContext)){
                if(oppositRequestCount == 0)
                    vhItem.icPerson.setVisibility(View.GONE);
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
                        if (Prefs.isServerOK(mContext)) {
                            addRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), topic.getIdTopic());
                        }else{
                            Toast.makeText(mContext, "Vous êtes ajouté comme demandeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Prefs.isServerOK(mContext)) {
                            deleteRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), topic.getIdTopic());
                            if(requestCount == 1){
                                mClassTopics.remove(position);
                                notifyItemRemoved(position);
                            }
                        }else {
                            //Toast
                            Toast.makeText(mContext, "Votre demande d'aide est supprimée.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            vhItem.checkBoxRequestGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (Prefs.isServerOK(mContext)) {
                            addRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), topic.getIdTopic());
                        }else{
                            Toast.makeText(mContext, "Vous êtes ajouté comme proposeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Prefs.isServerOK(mContext)) {
                            deleteRequestOnThisTopic(mContext, Prefs.getStudentId(mContext), topic.getIdTopic());
                            if(requestCount == 1){
                                mClassTopics.remove(position);
                                notifyItemRemoved(position);
                            }
                        }else {
                            //Toast
                            Toast.makeText(mContext, "Votre proposition d'aide est supprimée.", Toast.LENGTH_LONG).show();
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

    // API CALL
    public void addRequestOnThisTopic(Context context, int idStudent, int idTopic) {
        JsonObject json = new JsonObject();
        json.addProperty("sujetId", idTopic);
        json.addProperty("eleveId", idStudent);
        json.addProperty("description", "hello Coco! Help me pleeeeease!");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        json.addProperty("dateheure", currentDateandTime);
        json.addProperty("type", type);
        json.addProperty("flag", 1);

        Log.d("debug", json.toString());

        Ion.with(context)
                .load(BASE_URL + "/demande/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                            Toast.makeText(mContext, "Une erreur est survenue lors de l'ajout de la demande.", Toast.LENGTH_LONG).show();

                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
                            if(result.getAsJsonObject().get("code").getAsInt() == 0){
                                Log.d("debug", "demande non ajouté");
                                Toast.makeText(mContext, "La " + type.toLowerCase() + " n'a pas été ajoutée.", Toast.LENGTH_LONG).show();
                                notifyDataSetChanged();
                            }else{
                                Log.d("debug", "************** request added");
                                if (type.equals("Proposition")){
                                    Toast.makeText(mContext, "Vous êtes ajouté comme proposeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(mContext, "Vous êtes ajouté comme demandeur d'aide pour ce sujet.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    // API CALL
    public void deleteRequestOnThisTopic(Context context, int idStudent, int idTopic){
        Ion.with(context)
                .load("DELETE", BASE_URL + "/demande/" + String.valueOf(idStudent) + "/" + String.valueOf(idTopic))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d("DEBUG", e.toString());
                            Toast.makeText(mContext, "Une erreur est survenue lors de la suppression de la demande.", Toast.LENGTH_LONG).show();

                        }
                        if (result != null) {
                            Log.d("debug", result.getAsJsonObject().toString());
//                            if(result.getAsJsonObject().get("code").getAsInt() == 0){
//                                Log.d("debug", "demande non supprimée");
//                                Toast.makeText(mContext, "La " + type.toLowerCase() + " n'a pas été supprimée.", Toast.LENGTH_LONG).show();
//                                notifyDataSetChanged();
//                            }else{
                                Log.d("debug", "************** request deleted");
                                Toast.makeText(mContext, "Votre " + type.toLowerCase() + " d'aide est supprimée.", Toast.LENGTH_LONG).show();
//                            }
                        }
                    }
                });
    }
}