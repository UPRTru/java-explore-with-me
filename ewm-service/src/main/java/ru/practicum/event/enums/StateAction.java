package ru.practicum.event.enums;

import ru.practicum.exception.ConflictException;

public enum StateAction {
    SEND_TO_REVIEW, CANCEL_REVIEW, PUBLISH_EVENT, REJECT_EVENT;

    public static State getState(String stateAction) {
        try {
            switch (valueOf(stateAction)) {
                case SEND_TO_REVIEW:
                    return State.PENDING;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    return State.CANCELED;
                case PUBLISH_EVENT:
                    return State.PUBLISHED;
                default:
                    throw new ConflictException("Событие не опубликовано");
        }
        } catch (IllegalArgumentException e) {
            throw new ConflictException("Событие не опубликовано");
        }
    }
}
