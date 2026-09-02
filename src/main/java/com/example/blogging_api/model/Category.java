package com.example.blogging_api.model;

public enum Category {
    TECHNOLOGY("Technology"),
    SCIENCE("Science"),
    HEALTH("Health & Wellness"),
    EDUCATION("Education"),
    GENERAL("General"),
    SPORTS("Sports"),
    ENTERTAINMENT("Entertainment"),
    BUSINESS("Business");

    private String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}