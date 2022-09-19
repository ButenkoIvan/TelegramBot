package settings;

public enum Buttons {
    START("Старт", "/start", "/start", "/start"),
    GET_INFO("Отримати інфо", "Get info",
            "Otrzymać informację", "Získat informace"),
    SETTINGS("Налаштування", "Settings", "Ustawienia", "Nastavení"),
    NUM_DECIMAL_PLACES("Кількість знаків після коми", "Number of decimal places",
            "Liczba miejsc po przecinku", "Počet desetinných míst"),
    BANK("Банк", "Bank", "Bank", "Banka"),
    CURRENCY("Валюта", "Currency", "Waluta", "Měna"),
    NOTIFICATION("Час сповіщення", "Notification time",
            "Czas powiadomienia", "Čas oznámení"),
    ZONEID("Часовий пояс", "Time zone",
            "Strefa czasowa", "Časové pásmo"),
    LANGUAGE("Мова", "Language", "Język", "Jazyk"),
    BACK_TO_SETTINGS("↩️", "Settings", "Settings", "Settings"),
    BACK_TO_START("🏠️", "BACK_TO_START", "BACK_TO_START", "BACK_TO_START");

    private String buttonsNameUA;
    private String buttonsNameEN;
    private String buttonsNamePL;
    private String buttonsNameCZ;

    Buttons(String buttonsNameUA, String buttonsNameEN, String buttonsNamePL, String buttonsNameCZ) {
        this.buttonsNameUA = buttonsNameUA;
        this.buttonsNameEN = buttonsNameEN;
        this.buttonsNamePL = buttonsNamePL;
        this.buttonsNameCZ = buttonsNameCZ;
    }

    public String getNameUA() {
        return buttonsNameUA;
    }

    public String getNameEN() {
        return buttonsNameEN;
    }
    public String getNamePL() {
        return buttonsNamePL;
    }
    public String getNameCZ() {
        return buttonsNameCZ;
    }

    public static Buttons convertToEnum(String text) {
        for (Buttons button : Buttons.values()) {
            if (button.getNameEN().equals(text)) {
                return button;
            }
        }
        return null;
    }
}
