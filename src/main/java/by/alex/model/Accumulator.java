package by.alex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class Accumulator {
    @Setter
    @Getter
    private int value;
    public void add(int value) {
        this.value += value;
    }
}
