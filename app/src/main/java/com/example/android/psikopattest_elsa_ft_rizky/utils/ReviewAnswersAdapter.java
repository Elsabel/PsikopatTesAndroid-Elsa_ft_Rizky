package com.example.android.psikopattest_elsa_ft_rizky.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.psikopattest_elsa_ft_rizky.R;
import com.example.android.psikopattest_elsa_ft_rizky.data.Question;

import java.util.List;

public class ReviewAnswersAdapter extends ArrayAdapter<Question> {


    public ReviewAnswersAdapter(@NonNull Context context, List<Question> questions) {
        super(context, 0, questions);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_review_list_item, parent, false);
        }

        final Question question = getItem(position);
        TextView questionTextView = convertView.findViewById(R.id.review_question_textview);
        TextView answerTextView = convertView.findViewById(R.id.review_answer_textview);

        if (question != null) {
            questionTextView.setText(question.getQuestion());
            answerTextView.setText(question.getAnswer());
        }

        return convertView;
    }
}
