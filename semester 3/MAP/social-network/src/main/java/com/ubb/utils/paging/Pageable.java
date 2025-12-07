package com.ubb.utils.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pageable {
    private int pageNumber;
    private int pageSize;

    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public void setPageNumber(int pageNumber) {
        if  (pageNumber < 0) return ;
        this.pageNumber = pageNumber;
    }
}
