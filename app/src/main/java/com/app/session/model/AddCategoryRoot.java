package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class AddCategoryRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    CategoryId body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoryId getBody() {
        return body;
    }

    public void setBody(CategoryId body) {
        this.body = body;
    }
}
