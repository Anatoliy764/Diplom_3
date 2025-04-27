package kz.yandex.practicum.qa.sb.pom.constructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Tab {
    BUNS("Булки"),
    SAUCES("Соусы"),
    FILLINGS("Начинки");

    private final String label;

    public static Tab valueOfLabel(String label) {
        return Arrays.stream(values()).filter(t -> t.getLabel().equals(label)).findFirst().orElse(null);
    }
}
