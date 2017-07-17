package com.yzc.mongo.entity;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @param <T>
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Items<T> {

    private Long version = null;

    private Long count = null;

    private Boolean isEnd = null;

    private Collection<T> items = new ArrayList<T>();

    public Items(Collection<T> items) {
        this.setItems(items);
    }

    public Items(Collection<T> items, Long count) {
        this.setItems(items);
        this.setCount(count);
    }

    public void setItems(Collection<T> items) {
        if (items != null) {
            this.items = items;
        }
    }
}
