package com.example.android.psikopattest_elsa_ft_rizky.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.psikopattest_elsa_ft_rizky.R;
import com.example.android.psikopattest_elsa_ft_rizky.data.Options;
import com.example.android.psikopattest_elsa_ft_rizky.data.Question;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Question> questions;
    private boolean[] correctAnswers;

    public ResultsAdapter(Context context, ArrayList<Question> questions, boolean[] validateAnswers) {
        this.context = context;
        this.questions = questions;
        this.correctAnswers = validateAnswers;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_results_list_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        Question question = questions.get(position);
        String[] options = question.getOptions();
        Options optionsType = question.getOptionsType();

        StringBuilder correctAnswer = new StringBuilder();
        StringBuilder userAnswer = new StringBuilder();

        switch (optionsType) {
            case RADIOBUTTON:
                correctAnswer.append(options[question.getAnswerId().get(0)]);
                if (question.getUserSetAnswerId() != null && question.getUserSetAnswerId().size() > 0) {
                    if (correctAnswers[position]) {
                        userAnswer = correctAnswer;
                    } else {
                        userAnswer.append(options[question.getUserSetAnswerId().get(0)]);
                    }
                }
                break;

        }

        holder.questionTextView.setText(question.getQuestion());
        holder.correctAnswerTextView.setText(correctAnswer);
        holder.userAnswerTextView.setText(TextUtils.isEmpty(userAnswer) ? "Unanswered" : userAnswer);

        //set warna text jawaban buat ResultView, hijau jika sama, merah untuk sebaliknya
        if (correctAnswers[position]) {
            holder.userAnswerTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.userAnswerTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTextView;
        private TextView userAnswerTextView;
        private TextView correctAnswerTextView;

        CardViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question_text_view);
            userAnswerTextView = itemView.findViewById(R.id.user_answer);
            correctAnswerTextView = itemView.findViewById(R.id.correct_answer);
        }
    }


}
