package nz.ac.unitec.cs.assignment2_exercise.DataModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phonetic {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("sourceUrl")
    @Expose
    private String sourceUrl;
    @SerializedName("license")
    @Expose
    private License license;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
