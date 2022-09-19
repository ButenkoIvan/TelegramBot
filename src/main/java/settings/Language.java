package settings;

public enum Language {
    EN("English", "EnglishSet", "English \uD83C\uDDFA\uD83C\uDDF8"),
    UA("Ukrainian", "UkrainianSet", "Українська \uD83C\uDDFA\uD83C\uDDE6"),
    PL("Polish", "PolishSet","Polski \uD83C\uDDF5\uD83C\uDDF1"),
    CZ ("Czech","CzechSet", "Čeština 🇨🇿"),
    RU ("Russian", "RussianSet","Русский \uD83C\uDDF7\uD83C\uDDFA");

    String langName;
    String langNameSet;
    String langFlag;

    Language(String langName, String langNameSet, String langFlag) {
        this.langName = langName;
        this.langFlag = langFlag;
        this.langNameSet = langNameSet;
    }

    public String getLangName() {
        return langName;
    }
    public String getLangNameSet() {
        return langNameSet;
    }
    public String getLangFlag() {
        return langFlag;
    }

    public static Language convertToEnum(String text) {
        for (Language lang : Language.values()) {
            if (lang.getLangName().equals(text)) {
                return lang;
            }
        }
        return null;
    }

    public static Language convertToEnumSet(String text) {
        for (Language lang : Language.values()) {
            if (lang.getLangNameSet().equals(text)) {
                return lang;
            }
        }
        return null;
    }

    public static String translate(String text, Language language) {
        switch (text){
            case "Курс купівлі ":
                switch (language){
                    case EN: return "Purchase fx rate ";
                    case PL: return "Kurs kupna ";
                    case CZ: return "Nákup ";
                    default: return text;
                }
            case "Курс продажу ":
                switch (language){
                    case EN: return "Sales fx rate ";
                    case PL: return "Kurs sprzedaży ";
                    case CZ: return "Prodej ";
                    default: return text;
                }
            case "немає купівлі":
                switch (language){
                    case EN: return "no purchase";
                    case PL: return "brak zakupu";
                    case CZ: return "žádný nákup";
                    default: return text;
                }
            case "немає продажу":
                switch (language){
                    case EN: return "no sale";
                    case PL: return "brak sprzedaży";
                    case CZ: return "žádný prodej";
                    default: return text;
                }
            case "Будь ласка впишіть /start або натисніть кнопку.":
                switch (language){
                    case EN: return "Please type /start or press the button.";
                    case PL: return "Proszę wpisz /start lub naciśnij klawisz.";
                    case CZ: return "Prosím napište /start nebo stiskněte tlačítko.";
                    default: return text;
                }
            case "Щоб отримати інфо натисність кнопку":
                switch (language){
                    case EN: return "For get info please press the button";
                    case PL: return "Aby uzyskać informację naciśnij klawisz";
                    case CZ: return "Pro získání informací stiskněte prosím tlačítko";
                    default: return text;
                }
            case "Виберіть налаштування":
                switch (language){
                    case EN: return "Please choose options";
                    case PL: return "Proszę wybrać preferowane ustawienia";
                    case CZ: return "Vyberte prosím preferovaná nastavení";
                    default: return text;
                }
            case "Ласкаво просимо. Цей бот дозволить відслідкувати актуальні курси валют.":
                switch (language){
                    case EN: return "Welcome. This bot will help you to follow up current fx rates.";
                    case PL: return "Witamy. Ten bot pomoże śledzić aktualne kursy walut.";
                    case CZ: return "Vítejte. Tento bot vám pomůže sledovat aktuální měnové kurzy.";
                    default: return text;
                }
            case "Підтвердити":
                switch (language){
                    case EN: return "Confirm";
                    case PL: return "Zatwierdzić";
                    case CZ: return "Potvrdit";
                    default: return text;
                }
        }
        return "";
    }
}
