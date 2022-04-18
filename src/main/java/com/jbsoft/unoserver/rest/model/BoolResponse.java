package com.jbsoft.unoserver.rest.model;

public class BoolResponse {
    private boolean success;

    public BoolResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
