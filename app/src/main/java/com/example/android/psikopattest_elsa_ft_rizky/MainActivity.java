package com.example.android.psikopattest_elsa_ft_rizky;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.psikopattest_elsa_ft_rizky.data.Options;
import com.example.android.psikopattest_elsa_ft_rizky.data.Question;
import com.example.android.psikopattest_elsa_ft_rizky.data.QuestionSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String QUESTION_NUMBER = "QUESTION_NUMBER";
    public static final String QUESTIONS = "QUESTIONS";

    private TextView questionTextView;
    private TextView numOfQuestionsTextView;
    private LinearLayout optionsLinearLayout;
    private ProgressBar progressBar;
    private TextView reviewTextView;
    private Button nextButton;
    private Button prevButton;

    private int qNumber;
    private int totalQuestions;
    private ArrayList<Question> questions;
    private boolean answered;

    private Options optionsType;
    private View optionsView;
    private Toast toast;

    /**
     * Set up a listener for the next button to display next Question
     * start a new activity displaying quiz results when all questions are done
     */
    private View.OnClickListener nextButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //save current set answer before displaying the next question
            saveUserAnswer();

            //if the current question is unanswered, alert the user as all questions are mandatory
            if (!answered) {
                alertQuestionUnanswered();
                return;
            }

            /*
            increment the question number and display the next question
            or display the results if it's the last question after confirming for submission from the user
             */
            qNumber++;
            if (qNumber < questions.size()) {
                displayQuestion();
            } else {
                qNumber--;
                displayConfirmAlert(getString(R.string.submit_confirm), false);
            }
        }
    };

    /**
     * Set up a listener for the previous button to display previous Question
     * display a toast message when button is clicked while in the first question
     */
    private View.OnClickListener prevButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //save current set answer before displaying the previous question
            saveUserAnswer();

            /*
            decrement the question number and display the previous question
            if this is the first question, display a toast message stating there are no previous questions
             */
            if (qNumber > 0) {
                qNumber--;
                displayQuestion();
            } else {
                alertNoPrevQuestions();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain references to all the views in the main activity
        questionTextView = findViewById(R.id.question_text);
        numOfQuestionsTextView = findViewById(R.id.questions_remaining);
        optionsLinearLayout = findViewById(R.id.linearLayout_Options);
        progressBar = findViewById(R.id.determinantProgressBar);

        //register the click events for the previous and next buttons
        prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(prevButtonClickListener);
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(nextButtonClickListener);

        //set up the listener for 'mark for review' option
        reviewTextView = findViewById(R.id.review_check);
        reviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarkerForReview(v);
            }
        });

        //implement the review button click functionality to display the questions marked for review
        ImageButton reviewButton = findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserAnswer();
                displayReviewQuestions();
            }
        });

        //get all the questions and determine the total number of questions
        questions = QuestionSet.getAllQuestions(this);
        totalQuestions = questions.size();

        //set progress bar max value
        progressBar.setMax(totalQuestions);

        displayQuestion();
    }

    /**
     * Display the questions from the set along with it's options, each question can
     * have different number of options and different type of views for the inputs
     */
    private void displayQuestion() {

        //remove previous question and it's corresponding options
        optionsLinearLayout.removeAllViews();

        //update the state of 'mark for review' TextView appropriately
        if (questions.get(qNumber).isMarkedForReview()) {
            reviewTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box, 0, 0, 0);
        } else {
            reviewTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_outline_blank, 0, 0, 0);
        }

        //display the current question number and total number of questions
        String text = (qNumber + 1) + "/" + totalQuestions;
        numOfQuestionsTextView.setText(text);

        //update the progress bar status
        progressBar.setProgress(qNumber);

        if (answered)
            answered = false;

        Question currentSet = questions.get(qNumber);
        questionTextView.setText(currentSet.getQuestion());

        //set the button for last question to be 'Submit', rather than 'Next'
        if (qNumber == questions.size() - 1) {
            nextButton.setText(R.string.submit);
        } else {
            nextButton.setText(R.string.nextQuestion);
        }

        displayOptions();

    }

    private void displayOptions() {

        //get the current question and it's options
        Question question = questions.get(qNumber);
        String[] options = question.getOptions();
        Options currentOptionsType = question.getOptionsType();

                RadioGroup radioGroup = new RadioGroup(this);
                for (int i = 0; i < options.length; i++) {
                    RadioButton button = new RadioButton(this);
                    button.setText(options[i]);
                    button.setId(i);
                    radioGroup.addView(button);

                }
                optionsLinearLayout.addView(radioGroup);

                //restore saved answers
                if (question.getUserSetAnswerId() != null && question.getUserSetAnswerId().size() > 0) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(question.getUserSetAnswerId().get(0));
                    radioButton.setChecked(true);
                }

                optionsView = radioGroup;
                   }

    private void saveUserAnswer() {

        if (qNumber < questions.size()) {
            Question currentQuestion = questions.get(qNumber);
            ArrayList<Integer> userSelectedAnswers = new ArrayList<>();

                    //Menyimpan nilai dari RadioButton
                    int selectedId = ((RadioGroup) optionsView).getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);

                    if (selectedRadioButton == null) {
                        return;
                    } else {
                        userSelectedAnswers.add(selectedId);
                        currentQuestion.setUserSetAnswerId(userSelectedAnswers);
                        answered = true;
                    }
        }
    }

    private void setMarkerForReview(View v) {
        if (!questions.get(qNumber).isMarkedForReview()) {
            questions.get(qNumber).setMarkedForReview(true);
            ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box, 0, 0, 0);
        } else {
            questions.get(qNumber).setMarkedForReview(false);
            ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_outline_blank, 0, 0, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveUserAnswer();

        outState.putInt(QUESTION_NUMBER, qNumber);
        outState.putSerializable(QUESTIONS, questions);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        qNumber = savedInstanceState.getInt(QUESTION_NUMBER);
        questions = (ArrayList<Question>) savedInstanceState.getSerializable(QUESTIONS);

        displayQuestion();
    }

    private void alertQuestionUnanswered() {
        cancelToast();

        toast = Toast.makeText(this, R.string.no_answer_error, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 258);
        toast.show();
    }

    private void alertNoPrevQuestions() {
        cancelToast();

        toast = Toast.makeText(MainActivity.this, R.string.no_prev_question, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 258);
        toast.show();
    }

    private void cancelToast() {
        if (toast != null)
            toast.cancel();
    }

    private void displayResults() {
        Intent intent = new Intent(MainActivity.this,
                ResultsActivity.class);
        intent.putExtra(QUESTIONS, questions);
        startActivity(intent);
        finish();
    }

    private void displayReviewQuestions() {
        Intent intent = new Intent(MainActivity.this, ReviewAnswersActivity.class);
        intent.putExtra(QUESTIONS, questions);
        startActivity(intent);
    }

    private void displayConfirmAlert(String message, final boolean isBackPressed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isBackPressed) {
                            finish();
                        } else {
                            displayResults();
                        }
                    }
                })
                .setNegativeButton(R.string.confirm_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            qNumber = intent.getIntExtra(QUESTION_NUMBER, 0);
            displayQuestion();
        }
    }

    @Override
    public void onBackPressed() {
        displayConfirmAlert(getString(R.string.exit_confirm), true);
    }
}
