package com.encoria.api.model.moments;

public enum MediaType {
    IMAGE, VIDEO;

    @Override
    public String toString() {
        return this.name();
    }
}
