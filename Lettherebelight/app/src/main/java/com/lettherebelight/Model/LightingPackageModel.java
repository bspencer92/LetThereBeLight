package com.lettherebelight.Model;

public class LightingPackageModel {
    private int id, status;
    private String partName;

    public LightingPackageModel() {

    }

    public LightingPackageModel(int id, int status, String partName) {
        this.id = id;
        this.status = status;
        this.partName = partName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
