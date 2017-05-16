package com.wolff.wardrobe.localdb;

/**
 * Created by wolff on 11.04.2017.
 */

public class DbSchema {
    public static final class WItemTable{
        public static final String TABLE_NAME = "witems";

        public static final class Cols{
            public static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String SEASON = "season";
            public static final String MIN_T = "min_temp";
            public static final String MAX_T = "max_temp";

        }
    }
}
