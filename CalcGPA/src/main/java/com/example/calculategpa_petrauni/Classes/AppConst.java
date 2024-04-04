package com.example.calculategpa_petrauni.Classes;

import com.example.calculategpa_petrauni.BuildConfig;

public interface AppConst {

    /**
     * Shared Preference variables
     */
    String name = "settings";

    String _language = "language";
    String _theme = "theme";
    String _grids = "grids";
    String _version = "v"+ BuildConfig.VERSION_CODE;

    /**
     * Database references
     */
    String reports = "Reports";

    /**
     * Passing data flags
     */
    String isMark = "isMark";
}
