package com.github.atzcx.appverupdater.models;

public class UpdateModel {

    private String version;
    private String url;
    private String notes;

    public UpdateModel(){}

    public UpdateModel(String version, String url, String notes) {
        this.version = version;
        this.url = url;
        this.notes = notes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "UpdateModel{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
