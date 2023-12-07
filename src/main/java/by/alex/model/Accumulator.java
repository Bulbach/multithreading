package by.alex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
@Setter
@Getter
public class Accumulator {

    private int value;

    public void add(int value) {
        this.value += value;
    }
}
