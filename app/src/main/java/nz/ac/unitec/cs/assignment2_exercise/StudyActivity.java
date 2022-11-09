package nz.ac.unitec.cs.assignment2_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StudyActivity extends AppCompatActivity {
    TextView tvQuestion;
    RadioGroup radioGroup;
    RadioButton[] radioAnswer;
    Button btnSubmit, btnFinish;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Map<String, Object>> myQuestions = new ArrayList<>();
    ProgressDialog dialog;

    final int WEIGHT_MAX = 5;
    final int QUIZ_SIZE = 10;
    int questionNumber = 0;
    int quizScore = 0;
    int[] answers = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        tvQuestion = findViewById(R.id.tv_quiz_question);
        radioAnswer = new RadioButton[4];
        radioGroup = findViewById((R.id.radio_group));
        radioAnswer[0] = findViewById(R.id.radio_btn_quiz_answer_1);
        radioAnswer[1] = findViewById(R.id.radio_btn_quiz_answer_2);
        radioAnswer[2] = findViewById(R.id.radio_btn_quiz_answer_3);
        radioAnswer[3] = findViewById(R.id.radio_btn_quiz_answer_4);
        btnSubmit = findViewById(R.id.btn_quiz_submit);
        btnFinish = findViewById(R.id.btn_quiz_back);

        setLoadingDialog();
        addEventListeners();
        playQuestions(WEIGHT_MAX);
    }

    // set loading dialog.
    private void setLoadingDialog() {
        dialog = new ProgressDialog(StudyActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Connecting Server");
    }

    // make questions for study
    private void playQuestions(int weight) {
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("weight", weight)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot word: task.getResult()) {
                                myQuestions.add(word.getData());
                            }
                            if(weight > 0 && myQuestions.size() < QUIZ_SIZE) {
                                playQuestions(weight -1);
                            } else {
                                while(myQuestions.size() > QUIZ_SIZE) {
                                    myQuestions.remove(myQuestions.size()-1);
                                }
                                dialog.dismiss();
                                if(myQuestions.size() < 4)
                                {
                                    Toast.makeText(StudyActivity.this, "Data is too small", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    setQuestion();
                                }
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(StudyActivity.this, "Failed loading data", Toast.LENGTH_SHORT).show();
                            btnSubmit.setEnabled(false);
                        }
                    }
                });
        dialog.show();
    }

    private void setQuestion() {
        tvQuestion.setText("Q"+ (questionNumber + 1)+ ". " +myQuestions.get(questionNumber).get("meaning").toString());

        answers[0] = questionNumber;
        for(int i = 1; i < 4; i++)
        {
            boolean checker = false;
            do {
                checker = false;
                answers[i] = (int) Math.floor(Math.random() * myQuestions.size());
                if(answers[i] == myQuestions.size()) answers[i] = myQuestions.size()-1;
                for(int j = 0; j < i; j++) {
                    if(answers[i] == answers[j]) {
                        checker = true;
                    }
                }
            } while(checker);
        }
        for (int i = 0; i < answers.length; i++) {
            int randomIndexToSwap = new Random().nextInt(answers.length);
            int temp = answers[randomIndexToSwap];
            answers[randomIndexToSwap] = answers[i];
            answers[i] = temp;
        }
        for(int i = 0; i < 4; i++) {
            radioAnswer[i].setText(myQuestions.get(answers[i]).get("name").toString());
        }
    }


    private void addEventListeners() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(StudyActivity.this, "Pleas, Select an answer.", Toast.LENGTH_SHORT).show();
                } else {
                    int currentWeight = Integer.valueOf(myQuestions.get(questionNumber).get("weight").toString());
                    RadioButton selected = findViewById(radioGroup.getCheckedRadioButtonId());
                    if(selected.getText().toString().equals(myQuestions.get(questionNumber).get("name").toString())) {
                        if(currentWeight > 0) {
                            currentWeight --;
                            quizScore++;
                        }
                        Toast.makeText(StudyActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(StudyActivity.this);
                        alertDBuilder.setTitle("Incorrect")
                                .setMessage("Corect answer is " + myQuestions.get(questionNumber).get("name") )
                                .setCancelable(false)
                                .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDBuilder.create();
                        alertDialog.show();
                        if(currentWeight <5) {
                            currentWeight++;
                        }
                    }
                    selected.setChecked(false);
                    myQuestions.get(questionNumber).put("weight", currentWeight);
                    db.collection(FirebaseAuth.getInstance().getUid())
                            .document(myQuestions.get(questionNumber).get("name").toString())
                            .set(myQuestions.get(questionNumber));
                }
                questionNumber++;
                if(questionNumber >= myQuestions.size()) {

                    AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(StudyActivity.this);
                    alertDBuilder.setTitle("Your Score")
                            .setMessage("Your score is "+ quizScore + "/" + myQuestions.size() )
                            .setCancelable(false)
                            .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDBuilder.create();
                    alertDialog.show();
                } else {
                    setQuestion();
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}