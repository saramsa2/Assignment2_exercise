package nz.ac.unitec.cs.assignment2_exercise.DataModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meaning {
    @SerializedName("partOfSpeech")
    @Expose
    private String partOfSpeech;
    @SerializedName("definitions")
    @Expose
    private List<Definition> definitions = null;
    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;
    @SerializedName("antonyms")
    @Expose
    private List<Object> antonyms = null;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<Object> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<Object> antonyms) {
        this.antonyms = antonyms;
    }
}
