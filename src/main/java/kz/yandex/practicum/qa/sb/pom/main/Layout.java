package kz.yandex.practicum.qa.sb.pom.main;

import com.codeborne.selenide.Selenide;

public abstract class Layout {

    protected HeaderPom headerPom;

    public Layout() {
        this.headerPom = Selenide.page(HeaderPom.class);
    }

    public HeaderPom getHeader() {
        return headerPom;
    }

    public boolean isDisplayed() {
        return headerPom.isDisplayed();
    }
}
