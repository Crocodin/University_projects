package com.ubb.dto;

import javafx.util.Pair;
import java.util.List;

public interface ObjectFilterDTO  {
    public Pair<String, List<Object>> toSql();
}
