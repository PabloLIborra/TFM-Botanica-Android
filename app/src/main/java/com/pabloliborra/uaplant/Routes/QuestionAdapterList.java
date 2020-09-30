package com.pabloliborra.uaplant.Routes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionAdapterList extends RecyclerView.Adapter<QuestionAdapterList.ViewHolder>{
    private Context context;
    private View listItem;
    private List<QuestionListItem> listQuestions;
    private List<ItemHolder> itemHolders;
    private boolean testCompleted = true;

    private AlertDialog.Builder builder;
    private TextView titleAlert;
    private TextView subtitleAlert;
    private Button acceptButtonAlert, closeButtonAlert;

    private Plant plant;

    // RecyclerView recyclerView;
    public QuestionAdapterList(Context context, List<QuestionListItem> listQuestions, Plant plant) {
        this.context = context;
        this.listQuestions = listQuestions;
        this.itemHolders = new ArrayList<>();
        this.plant = plant;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem= layoutInflater.inflate(R.layout.item_list_question, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(this.itemHolders != null && this.itemHolders.contains(holder.item) != false && this.itemHolders.get(position).position == position) {
            List<String> answers = this.itemHolders.get(position).answers;
            holder.title.setText(this.itemHolders.get(position).title);
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, answers);
            holder.answers.setAdapter(ad);
            holder.answers.setSelection(itemHolders.get(position).answerSelected);
            System.out.println("EXISTEEEE");

            if(itemHolders.get(position).tocheck == true) {
                if(position == 0) {
                    this.testCompleted = true;
                }
                if(itemHolders.get(position).lastAnswer.equalsIgnoreCase(listQuestions.get(position).getTrueAnswer())) {
                    itemHolders.get(position).drawableLayout = R.drawable.rounded_layout_green;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_green);
                } else {
                    itemHolders.get(position).drawableLayout = R.drawable.rounded_layout_red;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_red);
                    this.testCompleted = false;
                }
                if(position == this.itemHolders.size() - 1) {
                    if(this.testCompleted == true) {
                        this.createDialogTestCompleted();
                    } else {
                        this.createDialogTestError();
                    }
                }
            }
        } else {
            List<String> answers = this.listQuestions.get(position).getAnswers();
            Collections.shuffle(answers);
            holder.title.setText(this.listQuestions.get(position).getTitle());
            answers.add(0,"");
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, answers);
            holder.answers.setAdapter(ad);

            ItemHolder itemHolder = new ItemHolder(this.listQuestions.get(position).getTitle(), answers, R.drawable.rounded_layout_transparent, position);
            holder.item = itemHolder;
            this.itemHolders.add(itemHolder);
            System.out.println("NO EXISTEEEE");
        }
        System.out.println("HOLAAAAAAAAAAAA");
        final int numQuestion = position;
        holder.answers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(itemHolders.get(numQuestion).answerSelected != position) {
                    String answer = parent.getItemAtPosition(position).toString();
                    itemHolders.get(numQuestion).lastAnswer = answer;
                    itemHolders.get(numQuestion).drawableLayout = R.drawable.rounded_layout_transparent;
                    holder.layout.setBackgroundResource(R.drawable.rounded_layout_transparent);
                    itemHolders.get(numQuestion).answerSelected = position;
                    itemHolders.get(numQuestion).tocheck = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //la logica del negocio
            }

        });
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
            }
        });
        alertDialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Spinner answers;
        public RelativeLayout layout;
        public ItemHolder item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleQuestion);
            this.answers = itemView.findViewById(R.id.spinnerAnswers);
            this.layout = itemView.findViewById(R.id.backgroundLayoutSpinner);

        }
    }

    private class ItemHolder {
        public String title;
        public List<String> answers;
        public int drawableLayout;
        public int position;
        public boolean tocheck = false;
        public String lastAnswer = "";
        public int answerSelected = 0;

        public ItemHolder(String title, List<String> answers, int drawableLayout, int position) {
            this.title = title;
            this.answers = answers;
            this.drawableLayout = drawableLayout;
            this.position = position;
        }
    }
}
