package ij.personal.helpy.Contact_Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ij.personal.helpy.Models.Request;
import ij.personal.helpy.Models.Student;
import ij.personal.helpy.Prefs;
import ij.personal.helpy.R;


public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String BASE_URL = "http://54.37.157.172:3000";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Request> mTopicRequests;
    private Context mContext;
    private String type;
    private String topicName;

    public ContactAdapter(List<Request> topicRequests, Context mContext, String type, String topicName) {
        this.mContext = mContext;
        this.mTopicRequests = topicRequests;
        this.type = type;
        this.topicName = topicName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_card, parent, false);
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

            if (getItem(position).getType().equals("Proposition")) {
                // put card color
                vhItem.cardViewContact.setCardBackgroundColor(vhItem.cardViewContact.getContext().getColor(R.color.green));
                // put card text
                vhItem.txtType.setText(" propose son aide.");

            } else {
                // put card color
                vhItem.cardViewContact.setCardBackgroundColor(vhItem.cardViewContact.getContext().getColor(R.color.red));
            }

            // get Student Info
            final Student student;
            if (Prefs.isServerOK(mContext)) {
                student = this.getStudent(getItem(position).getIdStudent());
            } else {
                student = this.getFakeStudent(getItem(position).getIdStudent());
            }

            // put student info on the card
            vhItem.txtStudentName.setText(student.getFirstName() + " " + student.getLastName());
            // set visibility to contact icons
            if(student.getPrefPhone() == 0) vhItem.icPhone.setVisibility(View.GONE);
            if(student.getPrefSms() == 0) vhItem.icSms.setVisibility(View.GONE);
            if(student.getPrefMail() == 0) vhItem.icMail.setVisibility(View.GONE);
            if(student.getPrefAlertP() == 0) vhItem.icAlert.setVisibility(View.GONE);

            // todo: make intent to contact (by notification)
            vhItem.icPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+33" + student.getPhone(), null)));
                }
            });

            vhItem.icSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + student.getPhone()));
                    if (type.equals("Demande")){
                        intent.putExtra("sms_body", "Hello! J'ai besoin d'aide pour le sujet " + topicName + ". "
                                + Prefs.getStudentFirstName(mContext) + " " + Prefs.getStudentLastName(mContext));
                    }else{
                        intent.putExtra("sms_body", "Hello! Je peux t'aider pour le sujet " + topicName + ". "
                                + Prefs.getStudentFirstName(mContext) + " " + Prefs.getStudentLastName(mContext));
                    }
                    mContext.startActivity(intent);
                }
            });

            vhItem.icMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{student.getMail()});
                    if (type.equals("Demande")){
                        i.putExtra(Intent.EXTRA_SUBJECT, "Helpy - Demande d'aide");
                        i.putExtra(Intent.EXTRA_TEXT, "Hello! J'ai besoin d'aide pour le sujet " + topicName + ". "
                                + Prefs.getStudentFirstName(mContext) + " " + Prefs.getStudentLastName(mContext));
                    }else{
                        i.putExtra(Intent.EXTRA_SUBJECT, "Helpy - Proposition d'aide");
                        i.putExtra(Intent.EXTRA_TEXT, "Hello! J'ai peux t'aider pour le sujet " + topicName + ". "
                                + Prefs.getStudentFirstName(mContext) + " " + Prefs.getStudentLastName(mContext));
                    }
                    mContext.startActivity(i);
                }
            });

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            VHHeader vhHeader = (VHHeader) holder;
            if (type.equals("Demande")){
                vhHeader.txtHeaderTitle.setText("Ces élèves peuvent vous aider");
            }else{
                vhHeader.txtHeaderTitle.setText("Ces élèves ont besoin de vous");
            }

        }
    }

    @Override
    public int getItemCount() {
        return mTopicRequests.size() + 1;
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

    private Request getItem(int position) {
        return mTopicRequests.get(position - 1);
    }

    class VHItem extends RecyclerView.ViewHolder {
        // each data item
        public TextView txtStudentName;
        public TextView txtType;
        public CardView cardViewContact;
        public ImageView icPhone;
        public ImageView icSms;
        public ImageView icMail;
        public ImageView icAlert;


        public VHItem(View itemView) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtType = itemView.findViewById(R.id.txtType);
            cardViewContact = itemView.findViewById(R.id.cardViewContact);
            icPhone = itemView.findViewById(R.id.icPhone);
            icSms = itemView.findViewById(R.id.icSms);
            icMail = itemView.findViewById(R.id.icMail);
            icAlert = itemView.findViewById(R.id.icAlert);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView txtHeaderTitle;

        public VHHeader(View itemView) {
            super(itemView);
            txtHeaderTitle = itemView.findViewById(R.id.txtHeaderTitle);
        }
    }

    // API CALL
    public Student getStudent(int idStudent) {
        try {
            JsonObject jsonResponce = Ion.with(mContext)
                    .load(BASE_URL + "/eleve/" + String.valueOf(idStudent))
                    .asJsonObject()
                    .get();
            Log.d("DEBUG", "jsonResponce: OK");

            Student student = new Student(idStudent, "", "", "", "", 0,
                    0,0,0,0,0,0);
            student.setLastName(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("mail").getAsString());
            student.setLastName(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("nom").getAsString());
            student.setFirstName(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prenom").getAsString());
            student.setPhone(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("telephone").getAsInt());
            student.setPrefPhone(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefAppel").getAsInt());
            student.setPrefSms(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefMessage").getAsInt());
            student.setPrefMail(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefMail").getAsInt());
            student.setPrefAlertP(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefNotifPersonelles").getAsInt());
            student.setPrefAlertG(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("prefNotifGlobales").getAsInt());
            student.setIdClass(jsonResponce.get("eleve").getAsJsonArray().get(0).getAsJsonObject().get("ClasseidClasse").getAsInt());


            Log.d("debug", student.toString());
            return student;

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("DEBUG", "executionException");
            Log.d("DEBUG", e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("DEBUG", "InterruptionException");
            Log.d("DEBUG", e.toString());
        }
        return null;
    }

    public Student getFakeStudent(int idStudent) {
        Student student = new Student(idStudent, "jerome.isoard@u-psud.fr", "", "Jerome", "Isoard", 617534110, 0,1,1,0,0,1);
        switch (idStudent) {
            case 2:
                student = new Student(idStudent, "corentin.lauret@u-psud.fr", "", "Corentin", "Lauret", 617534110, 0,1,1,1,1,1);
                break;
            case 3:
                student = new Student(idStudent, "yvan.bourdin@u-psud.fr", "", "Yvan", "Bourdin", 624399465, 1,1,1,1,1,1);
                break;
            case 4:
                student = new Student(idStudent, "samy.lemaissi@u-psud.fr", "", "Samy", "Lemaissi", 617534110, 1,1,0,1,0,1);
                break;
            case 5:
                student = new Student(idStudent, "romain.duval2@u-psud.fr", "", "Romain", "Duval", 617534110, 0,0,1,1,1,1);
                break;
            case 6:
                student = new Student(idStudent, "jerome.isoard@u-psud.fr", "", "Jerome", "Isoard", 617534110, 0,1,1,0,0,1);
                break;
        }
        return student;
    }
}