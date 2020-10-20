package com.pabloliborra.uaplant.Routes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.Marker;
import com.pabloliborra.uaplant.Plants.ListPlantsFragment;
import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Plants.PlantDetailActivity;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.TabBarActivity;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapterList extends RecyclerView.Adapter<QuestionAdapterList.ViewHolder>{
    private Context context;
    private View listItem;
    private List<QuestionListItem> listQuestions;
    LinkedHashMap<QuestionListItem,DataQuestion> listDataQuestion;
    private boolean testCompleted = true;

    private AlertDialog.Builder builder;
    private TextView titleAlert;
    private TextView subtitleAlert;
    private Button acceptButtonAlert, closeButtonAlert;

    private Activity activity;
    private Plant plant;

    private class DataQuestion {
        public List<String> answers;
        public int drawableLayout;
        public int position;
        public boolean tocheck = false;
        public boolean correct = false;
        public String lastAnswer = "";
        public int answerSelected;

        public DataQuestion(List<String> answers, int drawableLayout, int position) {
            this.answers = answers;
            this.drawableLayout = drawableLayout;
            this.position = position;
        }
    }

    // RecyclerView recyclerView;
    public QuestionAdapterList(Context context, List<QuestionListItem> listQuestions, Activity activity) {
        this.context = context;
        this.listQuestions = listQuestions;
        this.listDataQuestion = new LinkedHashMap<QuestionListItem,DataQuestion>();
        this.activity = activity;
        this.plant = activity.getPlant(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem = layoutInflater.inflate(R.layout.item_list_question, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(this.listDataQuestion.containsKey(this.listQuestions.get(position)) == true) {
            List<String> answers = this.listDataQuestion.get(this.listQuestions.get(position)).answers;
            holder.title.setText(this.listQuestions.get(position).getTitle());
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, answers);
            holder.answers.setAdapter(ad);
            holder.answers.setSelection(this.listDataQuestion.get(this.listQuestions.get(position)).answerSelected);

            if(this.listDataQuestion.get(this.listQuestions.get(position)).tocheck == true) {
                if(this.listDataQuestion.get(this.listQuestions.get(position)).lastAnswer.equalsIgnoreCase(listQuestions.get(position).getTrueAnswer())) {
                    this.listDataQuestion.get(this.listQuestions.get(position)).drawableLayout = R.drawable.rounded_layout_green;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_green);
                } else {
                    this.listDataQuestion.get(this.listQuestions.get(position)).drawableLayout = R.drawable.rounded_layout_red;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_red);
                }
            } else {
                this.listDataQuestion.get(listQuestions.get(position)).drawableLayout = R.drawable.rounded_layout_transparent;
                holder.layout.setBackgroundResource(R.drawable.rounded_layout_transparent);
            }
        } else {
            List<String> answers = this.listQuestions.get(position).getAnswers();
            Collections.shuffle(answers);
            holder.title.setText(this.listQuestions.get(position).getTitle());
            answers.add(0,"");
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, answers);
            holder.answers.setAdapter(ad);

            DataQuestion dataQuestion = new DataQuestion(answers, R.drawable.rounded_layout_transparent, position);
            this.listDataQuestion.put(this.listQuestions.get(position), dataQuestion);
        }
        final int numQuestion = position;
        holder.answers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String answer = parent.getItemAtPosition(position).toString();
                listDataQuestion.get(listQuestions.get(numQuestion)).lastAnswer = answer;
                if(listDataQuestion.get(listQuestions.get(numQuestion)).answerSelected != position) {
                    listDataQuestion.get(listQuestions.get(numQuestion)).drawableLayout = R.drawable.rounded_layout_transparent;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_transparent);
                    listDataQuestion.get(listQuestions.get(numQuestion)).answerSelected = position;
                    listDataQuestion.get(listQuestions.get(numQuestion)).tocheck = false;
                }

                if(answer.equalsIgnoreCase(listQuestions.get(numQuestion).getTrueAnswer())) {
                    listDataQuestion.get(listQuestions.get(numQuestion)).correct = true;
                } else {
                    listDataQuestion.get(listQuestions.get(numQuestion)).correct = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //la logica del negocio
            }

        });
    }

    public void checkAnswers() {
        boolean result = true;
        for (Map.Entry<QuestionListItem,DataQuestion> entry : this.listDataQuestion.entrySet()) {
            QuestionListItem key = entry.getKey();
            DataQuestion value = entry.getValue();
            value.tocheck = true;
            this.listDataQuestion.put(key, value);

            if(value.correct == false) {
                result = false;
            }
        }

        if(result == true) {
            this.activity.setState(State.COMPLETE);
            Plant plant = this.activity.getPlant(context);
            System.out.println(plant.isUnlock());
            plant.setUnlock(true);
            System.out.println(plant.isUnlock());

            AppDatabase.getDatabaseMain(this.context).daoApp().updatePlant(plant);
            AppDatabase.getDatabaseMain(this.context).daoApp().updateActivity(this.activity);
            this.createDialogTestCompleted();
        } else {
            this.createDialogTestError();
        }
    }

    @Override
    public int getItemCount() {
        return this.listQuestions.size();
    }

    private void createDialogTestError() {
        builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        ViewGroup viewGroup = listItem.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(listItem.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

        this.titleAlert = dialogView.findViewById(R.id.titleAlert);
        this.subtitleAlert = dialogView.findViewById(R.id.subtitleAlert);
        this.acceptButtonAlert = dialogView.findViewById(R.id.buttonAcceptAlert);
        this.acceptButtonAlert.setAlpha(0.3f);
        this.acceptButtonAlert.setEnabled(false);
        this.closeButtonAlert = dialogView.findViewById(R.id.buttonCloseAlert);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        this.titleAlert.setText("Test fallido");
        this.subtitleAlert.setText("Has fallado en alguna pregunta del test, corrige las respuestas que esten en rojo.");
        this.closeButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void createDialogTestCompleted() {
        builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        ViewGroup viewGroup = listItem.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(listItem.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

        this.titleAlert = dialogView.findViewById(R.id.titleAlert);
        this.subtitleAlert = dialogView.findViewById(R.id.subtitleAlert);
        this.acceptButtonAlert = dialogView.findViewById(R.id.buttonAcceptAlert);
        this.closeButtonAlert = dialogView.findViewById(R.id.buttonCloseAlert);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        this.titleAlert.setText("Test completado");
        this.subtitleAlert.setText("Has completado con éxito el test.\nYa puedes ver con detalle la planta oculta.\n\n" + '"' + this.plant.getScientific_name() + '"');
        this.closeButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        this.acceptButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent i = new Intent(context, TabBarActivity.class);
                i.putExtra(Constants.plantExtraTitle, plant);
                alertDialog.dismiss();
                context.startActivity(i);
            }
        });
        alertDialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Spinner answers;
        public RelativeLayout layout;
        public Question question;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleQuestion);
            this.answers = itemView.findViewById(R.id.spinnerAnswers);
            this.layout = itemView.findViewById(R.id.backgroundLayoutSpinner);

        }
    }
}
