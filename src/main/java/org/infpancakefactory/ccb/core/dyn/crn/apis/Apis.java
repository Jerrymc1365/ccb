package org.infpancakefactory.ccb.core.dyn.crn.apis;

import java.util.ArrayList;

public class Apis {

    /**
     * b: Branch
     * t: Transfer
     * f: Fake
     */


    public static boolean isExtraInfo(String string) {
        boolean ignore = false;
        for (int i = 0; i < string.length() && string.length() >= 3; i++) {
            if (string.charAt(i) == '$' && string.length() - 3 >= i) {
                switch (String.valueOf(string.charAt(i+1))) {
                    case "b","t","f": if (String.valueOf(string.charAt(i + 2)).equals(":")) return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<Station> getFakeStations(String string){
        ArrayList<Station> stations = new ArrayList<>();
        int nowStart = 0;
        int nowStatus = 0;
        for (int i = 0; i < string.length() && string.length() >= 3; i++) {
            if (string.charAt(i) == '$' && string.length() - 3 >= i) {
                switch (String.valueOf(string.charAt(i+1))) {
                    case "f": if (String.valueOf(string.charAt(i + 2)).equals(":")) {
                        nowStatus = 1;
                        nowStart = i + 3;
                    }
                }
            }
            if (nowStatus == 1 && string.charAt(i) == ',') {
                stations.add(new Station(string.substring(nowStart, i), "fake_station"));
                nowStart = i + 1;
            }
            if (nowStatus == 1 && string.charAt(i) == ';') {
                stations.add(new Station(string.substring(nowStart, i), "fake_station"));
                nowStart = i + 1;
                nowStatus = 0;
            }
        }
        return stations;
    }
    public static ArrayList<String> getStationTexts(String string) {
        ArrayList<String> strings = new ArrayList<>();

        int nowStart = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '|') {
                strings.add(string.substring(nowStart, i));
                nowStart = i + 1;
            }
            if (string.length() - 1 == i && nowStart == 0) {
                strings.add(string);
                break;
            }
            if (string.length() - 1 == i) {
                strings.add(string.substring(nowStart, i + 1));
                break;
            }
            if (string.charAt(i) == '$') {
                strings.add(string.substring(nowStart, i));
                break;
            }
        }

        return strings;
    }
    public static ArrayList<String> getGoTos(String string) {
        ArrayList<String> strings = new ArrayList<>();

        int nowStart = 0;
        int nowStatus = 0;
        for (int i = 0; i < string.length() && string.length() >= 3; i++) {
            if (string.charAt(i) == '$' && string.length() - 3 >= i) {
                switch (String.valueOf(string.charAt(i+1))) {
                    case "t": if (String.valueOf(string.charAt(i + 2)).equals(":")) {
                        nowStatus = 1;
                        nowStart = i + 3;
                    }
                }
            }
            if (nowStatus == 1 && string.charAt(i) == ',') {
                strings.add(string.substring(nowStart, i));
                nowStart = i + 1;
            }
            if (nowStatus == 1 && string.charAt(i) == ';') {
                strings.add(string.substring(nowStart, i));
                nowStart = i + 1;
                nowStatus = 0;
            }
        }

        return strings;
    }
}
