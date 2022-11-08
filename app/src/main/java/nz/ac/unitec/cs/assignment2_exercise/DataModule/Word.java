package nz.ac.unitec.cs.assignment2_exercise.DataModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Word {

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("phonetic")
    @Expose(deserialize = false)
    private String phonetic;

    @SerializedName("phonetics")
    @Expose
    private List<Phonetic> phonetics = null;

    @SerializedName("meanings")
    @Expose
    private List<Meaning> meanings = null;

    @SerializedName("license")
    @Expose(deserialize = false)
    private License license;

    @SerializedName("sourceUrls")
    @Expose(deserialize = false)
    private List<String> sourceUrls = null;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public List<String> getSourceUrls() {
        return sourceUrls;
    }

    public void setSourceUrls(List<String> sourceUrls) {
        this.sourceUrls = sourceUrls;
    }
}
