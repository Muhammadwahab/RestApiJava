package com.example.restapilearning.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class PaginatedItems<T> {
    @JsonProperty("customItemsName")
    private List<T> items;
    private int page;
    private int pageSize;
    private long totalItems;

    // Constructors
    public PaginatedItems() {
        // Default constructor
    }

    public PaginatedItems(List<T> items, int page, int pageSize, long totalItems) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    // Getters and Setters
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
