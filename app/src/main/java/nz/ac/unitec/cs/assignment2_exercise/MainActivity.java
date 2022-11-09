package nz.ac.unitec.cs.assignment2_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.unitec.cs.assignment2_exercise.API.VolleyAPI;
import nz.ac.unitec.cs.assignment2_exercise.DataModule.Definition;
import nz.ac.unitec.cs.assignment2_exercise.DataModule.Meaning;
import nz.ac.unitec.cs.assignment2_exercise.DataModule.Word;
import nz.ac.unitec.cs.assignment2_exercise.RecyclerView.RVItemAdapter;

public class MainActivity extends AppCompatActivity {

    EditText etSearchBox;
    ImageButton imgBtnSearch, imgBtnLogout, imgBtnMinimize,imgBtnEnlarge;
    ScrollView scrollResult;
    TextView tvResultWord, tvResultDefinition, tvResultSynonym;
    RecyclerView recyclerView;
    Button btnStudy;

    final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> myWord = new HashMap<>();
    List<Map<String, Object>> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchBox = findViewById(R.id.et_main_search);
        imgBtnLogout = findViewById(R.id.img_btn_main_logout);
        imgBtnSearch = findViewById(R.id.img_btn_main_search);
        imgBtnMinimize = findViewById(R.id.img_btn_main_minimize);
        imgBtnEnlarge = findViewById(R.id.img_btn_main_enlarge);
        scrollResult = findViewById(R.id.scr_main_result);
        tvResultWord = findViewById(R.id.tv_main_result_word);
        tvResultDefinition = findViewById(R.id.tv_main_result_definitions);
        tvResultSynonym = findViewById(R.id.tv_main_result_synonyms);
        recyclerView = findViewById(R.id.rv_main);
        btnStudy = findViewById(R.id.btn_main_study);

        addEventListeners();
        loadDatabase();
    }

    private void loadDatabase() {
        db.collection(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        items.clear();
                        for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            Map<String,Object> word = new HashMap<>();
                            word.put("name", snapshot.get("name"));
                            word.put("date", snapshot.get("date"));
                            word.put("phonetic", snapshot.get("phonetic"));
                            word.put("weight", snapshot.get("weight"));
                            word.put("meaning", snapshot.get("meaning"));
                            word.put("synonym", snapshot.get("synonym"));
                            items.add(word);
                        }
                        setRecyclerView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // set up recycler view
    private void setRecyclerView() {
        items = itemsSorting(items);
        RVItemAdapter adapter = new RVItemAdapter(items);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    // sorting loaded data by date
    private List<Map<String, Object>> itemsSorting(List<Map<String, Object>> items) {
        List<Map<String, Object>> result = new ArrayList<>();
        int size = items.size();
        for(int i = 0; i < size; i++) {
            int pointer = 0;
            for(int j = 0; j < items.size(); j++) {
                if((long)items.get(pointer).get("date") < (long)items.get(j).get("date")) {
                    pointer = j;
                }
            }
            result.add(items.get(pointer));
            items.remove(pointer);
        }

        for(int i = result.size() ; i > getResources().getInteger(R.integer.word_store_size); i--) {
            db.collection(FirebaseAuth.getInstance().getUid())
                    .document(result.get(i).get("name").toString())
                    .delete();
            result.remove(i);
        }
        return result;
    }


    // event Listeners.
    private void addEventListeners() {

        // Start study activity
        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudyActivity.class);
                startActivity(intent);
            }
        });

        // logout and move back login activity
        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
            }
        });

        // start search
        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtSearch = etSearchBox.getText().toString().trim();
                searchOnline(txtSearch);
            }
        });

        imgBtnMinimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollResult.setVisibility(View.GONE);
                imgBtnMinimize.setVisibility(View.GONE);
                imgBtnEnlarge.setVisibility(View.VISIBLE);
            }
        });
        imgBtnEnlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollResult.setVisibility(View.VISIBLE);
                imgBtnMinimize.setVisibility(View.VISIBLE);
                imgBtnEnlarge.setVisibility(View.GONE);
            }
        });
    }

    private void searchOnline(String txtSearch) {
        String searchUrl = BASE_URL + txtSearch;
        VolleyAPI myVolley = new VolleyAPI(MainActivity.this, searchUrl);
        myVolley.getAPI();
        myVolley.setReadAPIListener(new VolleyAPI.readAPIListener() {
            @Override
            public void readAPISucceed(String response) {

                Type type = new TypeToken<ArrayList<Word>>() {}.getType();
                List<Word> words = new Gson().fromJson(response, type);
                saveSearchResult(words.get(0));

            }

            @Override
            public void readAPIFailed() {

            }
        });
    }

    private void saveSearchResult(Word word) {
        StringBuilder sbDefinition = new StringBuilder();
        StringBuilder sbSynonyms = new StringBuilder();
        myWord.put("name", word.getWord());
        myWord.put("date", Calendar.getInstance().getTimeInMillis());
        myWord.put("phonetic", word.getPhonetic());
        myWord.put("weight", 5);
        int i = 0;
        for(Meaning meaning: word.getMeanings()) {
            sbDefinition.append(meaning.getPartOfSpeech() + ": ");
            int j = 0;
            for(Definition definition: meaning.getDefinitions()) {
                sbDefinition.append(definition.getDefinition() + " ");
                j++;
                if(j > 1) break;
            }
            sbDefinition.append(System.lineSeparator() + System.lineSeparator());
            int z = 0;
            for(String synonym: meaning.getSynonyms()) {
                sbSynonyms.append(synonym + ", ");
                if(z > 1) break;
            }
            i++;
            if(i > 1) break;
        }
        myWord.put("meaning", sbDefinition.toString());
        myWord.put("synonym", sbSynonyms.toString());
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .document(etSearchBox.getText().toString())
                .set(myWord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        scrollResult.setVisibility(View.VISIBLE);
                        setResultScreen();
                        loadDatabase();
                    }
                }). addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to writing.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setResultScreen() {
        tvResultWord.setText(myWord.get("name").toString());
        tvResultDefinition.setText(myWord.get("meaning").toString());
        tvResultSynonym.setText(myWord.get("synonym").toString());
        scrollResult.setVisibility(View.VISIBLE);
        imgBtnMinimize.setVisibility(View.VISIBLE);
        imgBtnEnlarge.setVisibility(View.GONE);
    }
}