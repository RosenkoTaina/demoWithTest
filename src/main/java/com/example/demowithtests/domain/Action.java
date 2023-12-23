package com.example.demowithtests.domain;

import lombok.Getter;

@Getter
public enum Action {
    CREATE(1),
    UPDATE(2),
    DELETE(3);

    private final int value;

    Action(int value) {
        this.value = value;
    }

    public static Action fromValue(int value) {
        for (Action action : Action.values()) {
            if (action.value == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
