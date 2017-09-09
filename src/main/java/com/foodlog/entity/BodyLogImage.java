package com.foodlog.entity;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by rafa on 07/09/17.
 */
public class BodyLogImage implements Serializable{
    @NotNull
    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photo;

    public BodyLogImage(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getPhoto() {
        return photo;
    }
}
