package com.lettherebelight.Entity;

public class PDF {
    private String fileName, description;
    private int fileId;

    public PDF(String fileName, String description, int fileId) {
        this.fileName = fileName;
        this.description = description;
        this.fileId = fileId;
    }

    public PDF (){}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
