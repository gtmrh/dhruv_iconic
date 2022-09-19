package com.rkvitsolutions.dhruviconic.Filter;

import java.util.List;

public class FollowupFilter {
    public static Integer INDEX_CALLCODE = 0;
    public static Integer INDEX_SOURCE = 1;
    public static Integer INDEX_STATUS = 2;
    public static Integer INDEX_CLIENTTYPE = 3;
    public static Integer INDEX_Project = 4;

    private String name;
    private List<String> values;
    private List<String> selected;

    public FollowupFilter(String name, List<String> values, List<String> selected) {
        this.name = name;
        this.values = values;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }
}