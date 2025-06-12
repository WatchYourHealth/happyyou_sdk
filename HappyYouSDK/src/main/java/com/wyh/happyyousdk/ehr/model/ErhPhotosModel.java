package com.wyh.happyyousdk.ehr.model;

import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;

import java.util.ArrayList;
import java.util.List;

public class ErhPhotosModel {
    List<String> keys;
    ArrayList<List<DiaryListDataResponse>> values;

    public ErhPhotosModel(List<String> keys, ArrayList<List<DiaryListDataResponse>> values) {
        this.keys = keys;
        this.values = values;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public ArrayList<List<DiaryListDataResponse>> getValues() {
        return values;
    }

    public void setValues(ArrayList<List<DiaryListDataResponse>> values) {
        this.values = values;
    }
}
