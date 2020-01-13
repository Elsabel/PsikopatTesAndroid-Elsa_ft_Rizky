package com.example.android.psikopattest_elsa_ft_rizky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.psikopattest_elsa_ft_rizky.data.Options;
import com.example.android.psikopattest_elsa_ft_rizky.data.Question;
import com.example.android.psikopattest_elsa_ft_rizky.adapter.ResultsAdapter;

import java.util.ArrayList;

import static com.example.android.psikopattest_elsa_ft_rizky.MainActivity.QUESTIONS;

public class ResultsActivity extends AppCompatActivity {

    private ArrayList<Question> questions;
    private boolean[] validateAnswers;
    private int score;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //mengambil pertanyaan dari MainActivity
        questions = (ArrayList<Question>) getIntent().getSerializableExtra(QUESTIONS);
        validateAnswers = validateAnswers();

        //set nilai setelah melakukan validasi jawaban
        for (boolean b : validateAnswers) {
            if (b) score++;
        }

        //Fungsi untuk nampilkan skor
        String message = getString(R.string.score_text)
                .concat(String.valueOf(score))
                .concat(getString(R.string.score_text_append))
                .concat(String.valueOf(questions.size()));
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        ResultsAdapter resultsAdapter = new ResultsAdapter(this, questions, validateAnswers);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultsAdapter);

    }

    private boolean[] validateAnswers() {
        validateAnswers = new boolean[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Options opType = question.getOptionsType();

            switch (opType) {
                case RADIOBUTTON:
                    if (question.getUserSetAnswerId() != null && question.getUserSetAnswerId().size() > 0) {                        //compare the user answer with the correct answer
                        if (question.getAnswerId().get(0).equals(question.getUserSetAnswerId().get(0))) {
                            validateAnswers[i] = true;
                        }
                    }
                    break;
            }
        }
        return validateAnswers;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
