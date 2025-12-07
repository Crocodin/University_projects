package com.ubb.utils.paging;

import lombok.Getter;
import java.util.List;

@Getter
public class Page<E> {
    private List<E> elementsOnPage;
    private int totalNumberOfElements;

    public Page(List<E> elementsOnPage, int totalNumberOfElements) {
        this.elementsOnPage = elementsOnPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }
}
