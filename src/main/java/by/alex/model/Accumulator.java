package by.alex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class Accumulator {
    @Setter
    @Getter
    private int value;
    private final int expectedValue;

    public void add(int value) {
        this.value += value;
    }

}
