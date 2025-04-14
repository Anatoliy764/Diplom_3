package kz.yandex.practicum.qa.sb.pom;

import com.codeborne.selenide.Selenide;

public abstract class Layout {

    protected HeaderPom headerPom;

    public Layout() {
        this.headerPom = Selenide.page(HeaderPom.class);
    }

    public HeaderPom getHeader() {
        return headerPom;
    }
}
