package com.pabloliborra.uaplant.Utils;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStringConverter {
    @TypeConverter
    public String getString(List<String> str) {
        if (str == null)
            return null;
        StringBuilder pictures = new StringBuilder();
        for (String s : str) pictures.append(s).append(";");
        return pictures.toString();
    }

    @TypeConverter
    public List<String> setString(String str) {
        if (str == null)
            return null;
        return new ArrayList<>(Arrays.asList(str.split(";")));
    }
}
