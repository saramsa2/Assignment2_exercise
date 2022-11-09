package nz.ac.unitec.cs.assignment2_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {
    TextView tvQuestion;
    RadioButton[] radioAnswer;
    Button btnSubmit, btnFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        tvQuestion = findViewById(R.id.tv_quiz_question);
        radioAnswer = new RadioButton[4];
        radioAnswer[0] = findViewById(R.id.radio_btn_quiz_answer_1);
        radioAnswer[1] = findViewById(R.id.radio_btn_quiz_answer_2);
        radioAnswer[2] = findViewById(R.id.radio_btn_quiz_answer_3);
        radioAnswer[3] = findViewById(R.id.radio_btn_quiz_answer_4);
        btnSubmit = findViewById(R.id.btn_quiz_submit);
        btnFinish = findViewById(R.id.btn_quiz_back);

        addEventListeners();
        makeQuestions();
    }

    private void makeQuestions() {
    }

    private void loadQuestions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        selectQuestions(queryDocumentSnapshots);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudyActivity.this, "Failed loading data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectQuestions(QuerySnapshot queryDocumentSnapshots) {
        for(int i = 5; i > 0; i++ ) {

        }
    }

    private void addEventListeners() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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