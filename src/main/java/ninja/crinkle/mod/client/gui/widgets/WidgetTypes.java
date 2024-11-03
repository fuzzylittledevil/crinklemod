package ninja.crinkle.mod.client.gui.widgets;

public enum WidgetTypes {
    button(Button.Builder.class);
//    checkbox,
//    combobox,
//    label,
//    list,
//    panel,
//    progressbar,
//    slider,
//    textbox,
//    toggle,
//    icon,
//    image,
//    radio,
//    iconButton;

    private final Class<? extends AbstractWidget.AbstractBuilder<?>> widgetClass;

    WidgetTypes(Class<? extends AbstractWidget.AbstractBuilder<?>> widgetClass) {
        this.widgetClass = widgetClass;
    }

    public static WidgetTypes fromString(String string) {
        for (WidgetTypes type : values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return null;
    }
}
