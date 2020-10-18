package com.app.session.utility;

import java.util.ArrayList;

public class SampleData {

    public static  int SAMPLE_DATA_ITEM_COUNT ;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);

        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            data.add("SAMPLE #");
        }

        return data;
    }

}
