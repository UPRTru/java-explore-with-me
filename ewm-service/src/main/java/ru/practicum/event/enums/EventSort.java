package ru.practicum.event.enums;

public enum EventSort {
    EVENT_DATE, VIEWS;

    public static String getSortField(String string) {
        if (string != null && string.equals(EVENT_DATE.toString())) {
            return "eventDate";
        }
        if (string != null && string.equals(VIEWS.toString())) {
            return "views";
        }
        return "id";
    }
}
