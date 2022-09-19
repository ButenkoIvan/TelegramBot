import keyboards.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import settings.*;
import settings.Currency;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyInfoBot extends TelegramLongPollingBot {
    private static CurrencyInfoBot instance;
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

    private final Settings settings;
    private String value;
    private Menu menu;
    private final static Object monitor = new Object();


    private CurrencyInfoBot(String value, Settings settings) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.menu = new MenuUA(settings);
        this.value = value;
        this.settings = settings;
    }

    public static CurrencyInfoBot getInstance(String value, Settings settings) {
        if (instance == null) {
            instance = new CurrencyInfoBot(value, settings);
        }
        return instance;
    }

    @Override
    public String getBotUsername() {
//        return "TelegramBot";
//        return "@Me_TelegramBot";
        return "@Me_TelegramBot";
    }

    @Override
    public String getBotToken() {
//        return "5697468965:AAGGdbGZ0Owb7IRQldeatY51Aboe7kFEF88";
//        return "5697468965:AAGGdbGZ0Owb7IRQldeatY51Aboe7kFEF88";
        return "5697468965:AAGGdbGZ0Owb7IRQldeatY51Aboe7kFEF88";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.hasCallbackQuery()) {
            try {
                handleQuery(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        Setting userSettings;
        long chatId = message.getChatId();
        synchronized (monitor) {
            userSettings = settings.settingsAllUsers.get(chatId);
        }
        if (userSettings == null) {
            userSettings = new Setting(chatId, NumberOfDecimalPlaces.TWO, Banks.PRIVAT,
                    Currency.getSelectedCurrencyList(), NotificationTime.NINE, ZoneId.UTC_THREE, Language.UA);
            synchronized (monitor) {
                settings.settingsAllUsers.put(chatId, userSettings);
            }
        }
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity;
            commandEntity = message.getEntities().stream()
                    .filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                if (command.equals(Buttons.START.getNameEN())) {
                    printMessage(chatId, menu.keyboardLanguage(chatId),
                            "Будь ласка оберіть мову. Please select language.\n" +
                                    "Proszę wybrać język. Prosím vyberte jazyk.\n" +
                                    "Выбери пожалуйста язык.");
                }
            }
        } else {
            printMessage(chatId, Language.translate("Будь ласка впишіть /start або натисніть кнопку.",
                    userSettings.getSelectedLanguage()));
        }
    }

    private void handleQuery(CallbackQuery buttonQuery) throws TelegramApiException {
        Setting userSettings;
        long chatId = buttonQuery.getMessage().getChatId();
        synchronized (monitor) {
            userSettings = settings.settingsAllUsers.get(chatId);
        }
        if (userSettings == null) {
            userSettings = new Setting(chatId, NumberOfDecimalPlaces.TWO, Banks.PRIVAT,
                    Currency.getSelectedCurrencyList(), NotificationTime.NINE, ZoneId.UTC_THREE, Language.UA);
        }
        menu = getMenu(userSettings);

        checkLanguageStartMenu(buttonQuery, userSettings);
        checkMainMenu(buttonQuery, userSettings);
        checkBanksMenu(buttonQuery, userSettings);
        checkDecimalPlacesMenu(buttonQuery, userSettings);
        checkNotificationMenu(buttonQuery, userSettings);
        checkCurrencyMenu(buttonQuery, userSettings);
        checkZoneIdMenu(buttonQuery, userSettings);
        checkLanguageSettingsMenu(buttonQuery, userSettings);
    }

    private void saveSelectCurrency(CallbackQuery buttonQuery, Currency enumData, Setting userSettings) throws TelegramApiException {
        List<Currency> currentCurrencies = userSettings.getSelectedCurrency();
        if (currentCurrencies.contains(enumData)) {
            currentCurrencies.remove(enumData);
        } else {
            currentCurrencies.add(enumData);
        }
        updateMessage(buttonQuery, menu.keyboardCurrency(buttonQuery.getMessage().getChatId()));
    }

    private void saveSelectZoneId(CallbackQuery buttonQuery, ZoneId enumData, Setting userSettings)
            throws TelegramApiException {
        if (!userSettings.getZoneId().getNameZone().equals(enumData.getNameZone())) {
            userSettings.setZoneId(enumData);
            updateMessage(buttonQuery, menu.keyboardZoneId(buttonQuery.getMessage().getChatId()));
        }
    }

    private void saveSelectNumDecPlaces(CallbackQuery buttonQuery, NumberOfDecimalPlaces enumData, Setting userSettings)
            throws TelegramApiException {
        if (userSettings.getNumberOfDecimalPlaces() != enumData.getIntNumber()) {
            userSettings.setNumberOfDecimalPlaces(enumData);
            updateMessage(buttonQuery, menu.keyboardNumDecPlaces(buttonQuery.getMessage().getChatId()));
        }
    }

    private void saveSelectNotificationTime(CallbackQuery buttonQuery, NotificationTime enumData, Setting userSettings)
            throws TelegramApiException {
        if (userSettings.getNotificationTime().getTime() != enumData.getTime()) {
            userSettings.setNotificationTime(enumData);
            updateMessage(buttonQuery, menu.keyboardNotification(buttonQuery.getMessage().getChatId()));
        }
    }

    private void saveSelectBanks(CallbackQuery buttonQuery, Banks enumData, Setting userSettings)
            throws TelegramApiException {
        if (!userSettings.getSelectedBank().equals(enumData)) {
            userSettings.setSelectedBank(enumData);
            updateMessage(buttonQuery, menu.keyboardBanks(buttonQuery.getMessage().getChatId()));
        }
    }

    private void saveSelectLanguage(CallbackQuery buttonQuery, Language enumData, Setting userSettings)
            throws TelegramApiException {
        if (!userSettings.getSelectedLanguage().equals(enumData)) {
            userSettings.setSelectedLanguage(enumData);
            menu = getMenu(userSettings);
            updateMessage(buttonQuery, menu.keyboardLanguage(buttonQuery.getMessage().getChatId()));
        }
    }

    private void saveSelectLanguageSet(CallbackQuery buttonQuery, Language enumData, Setting userSettings)
            throws TelegramApiException {
        if (!userSettings.getSelectedLanguage().equals(enumData)) {
            userSettings.setSelectedLanguage(enumData);
            menu = getMenu(userSettings);
            updateMessage(buttonQuery, menu.keyboardLanguageSet(buttonQuery.getMessage().getChatId()));
        }
    }

    private void printMessage(Long chatID, InlineKeyboardMarkup keyboard, String text)
            throws TelegramApiException {
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatID)
                .replyMarkup(keyboard)
                .build());
    }

    public void printMessage(Long chatID, String messageText) throws TelegramApiException {
        execute(SendMessage.builder()
                .text(messageText)
                .chatId(chatID)
                .build());
    }

    private void updateMessage(CallbackQuery buttonQuery, InlineKeyboardMarkup keyboard)
            throws TelegramApiException {
        long chatId = buttonQuery.getMessage().getChatId();
        int messageId = buttonQuery.getMessage().getMessageId();
        execute(EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyboard)
                .build());
    }

    public void checkMainMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        long chatId = buttonQuery.getMessage().getChatId();
        Buttons button = Buttons.convertToEnum(buttonQuery.getData());
        if (button != null) {
            switch (button) {
                case START:
                    printMessage(chatId, menu.keyboardStart(),
                            Language.translate("Ласкаво просимо. Цей бот дозволить відслідкувати актуальні курси валют.",
                                    userSettings.getSelectedLanguage()));
                    break;
                case GET_INFO:
                    service.execute(new SaveSettings(settings));
                    printMessage(chatId, settings.getInfo(chatId));
                    printMessage(chatId, menu.keyboardStart(),
                            Language.translate("Щоб отримати інфо натисність кнопку",
                                    settings.settingsAllUsers.get(chatId).getSelectedLanguage()));
                    break;
                case SETTINGS:
                    printMessage(chatId, menu.keyboardSettings(settings.settingsAllUsers.get(chatId)),
                            Language.translate("Виберіть налаштування",
                                    settings.settingsAllUsers.get(chatId).getSelectedLanguage()));
                    break;
                case BACK_TO_START:
                    printMessage(chatId, menu.keyboardStart(),
                            Language.translate("Щоб отримати інфо натисність кнопку",
                                    settings.settingsAllUsers.get(chatId).getSelectedLanguage()));
                    break;
                case NUM_DECIMAL_PLACES:
                    updateMessage(buttonQuery, menu.keyboardNumDecPlaces(chatId));
                    break;
                case BANK:
                    updateMessage(buttonQuery, menu.keyboardBanks(chatId));
                    break;
                case CURRENCY:
                    updateMessage(buttonQuery, menu.keyboardCurrency(chatId));
                    break;
                case NOTIFICATION:
                    updateMessage(buttonQuery, menu.keyboardNotification(chatId));
                    break;
                case ZONEID:
                    updateMessage(buttonQuery, menu.keyboardZoneId(chatId));
                    break;
                case LANGUAGE:
                    updateMessage(buttonQuery, menu.keyboardLanguageSet(chatId));
                    break;
            }
        }
    }

    public void checkBanksMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        Banks bank = Banks.convertToEnum(buttonQuery.getData());
        if (bank != null) {
            switch (bank) {
                case PRIVAT:
                    saveSelectBanks(buttonQuery, Banks.PRIVAT, userSettings);
                    break;
                case NBU:
                    saveSelectBanks(buttonQuery, Banks.NBU, userSettings);
                    break;
                case MONO:
                    saveSelectBanks(buttonQuery, Banks.MONO, userSettings);
                    break;
            }
        }
    }

    public void checkDecimalPlacesMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        NumberOfDecimalPlaces numberOfDecimalPlace = NumberOfDecimalPlaces.convertToEnum(buttonQuery.getData());
        if (numberOfDecimalPlace != null) {
            switch (numberOfDecimalPlace) {
                case TWO:
                    saveSelectNumDecPlaces(buttonQuery, NumberOfDecimalPlaces.TWO, userSettings);
                    break;
                case THREE:
                    saveSelectNumDecPlaces(buttonQuery, NumberOfDecimalPlaces.THREE, userSettings);
                    break;
                case FOUR:
                    saveSelectNumDecPlaces(buttonQuery, NumberOfDecimalPlaces.FOUR, userSettings);
                    break;
            }
        }
    }

    public void checkNotificationMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        NotificationTime notificationTime = NotificationTime.convertToEnum(buttonQuery.getData());
        if (notificationTime != null) {
            switch (notificationTime) {
                case NINE:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.NINE, userSettings);
                    break;
                case TEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.TEN, userSettings);
                    break;
                case ELEVEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.ELEVEN, userSettings);
                    break;
                case TWELVE:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.TWELVE, userSettings);
                    break;
                case THIRTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.THIRTEEN, userSettings);
                    break;
                case FOURTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.FOURTEEN, userSettings);
                    break;
                case FIFTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.FIFTEEN, userSettings);
                    break;
                case SIXTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.SIXTEEN, userSettings);
                    break;
                case SEVENTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.SEVENTEEN, userSettings);
                    break;
                case EIGHTEEN:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.EIGHTEEN, userSettings);
                    break;
                case SWICH_OFF:
                    saveSelectNotificationTime(buttonQuery, NotificationTime.SWICH_OFF, userSettings);
                    break;
            }
        }
    }

    public void checkCurrencyMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        Currency currency = Currency.convertToEnum(buttonQuery.getData());
        if (currency != null) {
            switch (currency) {
                case USD:
                    saveSelectCurrency(buttonQuery, Currency.USD, userSettings);
                    break;
                case EUR:
                    saveSelectCurrency(buttonQuery, Currency.EUR, userSettings);
                    break;
                case PLN:
                    saveSelectCurrency(buttonQuery, Currency.PLN, userSettings);
                    break;
                case BTC:
                    saveSelectCurrency(buttonQuery, Currency.BTC, userSettings);
                    break;
            }
        }
    }

    public void checkZoneIdMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        ZoneId zoneId = ZoneId.convertToEnum(buttonQuery.getData());
        if (zoneId != null) {
            switch (zoneId) {
                case UTC_ONE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_ONE, userSettings);
                    break;
                case UTC_TWO:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_TWO, userSettings);
                    break;
                case UTC_THREE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_THREE, userSettings);
                    break;
                case UTC_FOUR:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_FOUR, userSettings);
                    break;
                case UTC_FIVE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_FIVE, userSettings);
                    break;
                case UTC_SIX:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_SIX, userSettings);
                    break;
                case UTC_SEVEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_SEVEN, userSettings);
                    break;
                case UTC_EIGHT:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_EIGHT, userSettings);
                    break;
                case UTC_NINE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_NINE, userSettings);
                    break;
                case UTC_TEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_TEN, userSettings);
                    break;
                case UTC_ELEVEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_ELEVEN, userSettings);
                    break;
                case UTC_TWELVE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_TWELVE, userSettings);
                    break;
                case UTC_MINUS_ONE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_ONE, userSettings);
                    break;
                case UTC_MINUS_TWO:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_TWO, userSettings);
                    break;
                case UTC_MINUS_THREE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_THREE, userSettings);
                    break;
                case UTC_MINUS_FOUR:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_FOUR, userSettings);
                    break;
                case UTC_MINUS_FIVE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_FIVE, userSettings);
                    break;
                case UTC_MINUS_SIX:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_SIX, userSettings);
                    break;
                case UTC_MINUS_SEVEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_SEVEN, userSettings);
                    break;
                case UTC_MINUS_EIGHT:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_EIGHT, userSettings);
                    break;
                case UTC_MINUS_NINE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_NINE, userSettings);
                    break;
                case UTC_MINUS_TEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_TEN, userSettings);
                    break;
                case UTC_MINUS_ELEVEN:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_ELEVEN, userSettings);
                    break;
                case UTC_MINUS_TWELVE:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_MINUS_TWELVE, userSettings);
                    break;
                case UTC_ZERO:
                    saveSelectZoneId(buttonQuery, ZoneId.UTC_ZERO, userSettings);
                    break;
            }
        }
    }

    private void checkLanguageStartMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        long chatId = buttonQuery.getMessage().getChatId();
        Language language = Language.convertToEnum(buttonQuery.getData());
        if (language != null) {
            switch (language) {
                case UA:
                    saveSelectLanguage(buttonQuery, Language.UA, userSettings);
                    break;
                case EN:
                    saveSelectLanguage(buttonQuery, Language.EN, userSettings);
                    break;
                case PL:
                    saveSelectLanguage(buttonQuery, Language.PL, userSettings);
                    break;
                case CZ:
                    saveSelectLanguage(buttonQuery, Language.CZ, userSettings);
                    break;
                case RU:
                    printMessage(chatId, "Русский военный корабль, иди на ***. " +
                            "СЛАВА УКРАЇНІ! \uD83C\uDDFA\uD83C\uDDE6");
                    break;
            }
        }
    }

    private void checkLanguageSettingsMenu(CallbackQuery buttonQuery, Setting userSettings) throws TelegramApiException {
        long chatId = buttonQuery.getMessage().getChatId();
        Language language = Language.convertToEnumSet(buttonQuery.getData());
        if (language != null) {
            switch (language) {
                case UA:
                    saveSelectLanguageSet(buttonQuery, Language.UA, userSettings);
                    break;
                case EN:
                    saveSelectLanguageSet(buttonQuery, Language.EN, userSettings);
                    break;
                case PL:
                    saveSelectLanguageSet(buttonQuery, Language.PL, userSettings);
                    break;
                case CZ:
                    saveSelectLanguageSet(buttonQuery, Language.CZ, userSettings);
                    break;
                case RU:
                    printMessage(chatId, "Русский военный корабль, иди на ***. " +
                            "СЛАВА УКРАЇНІ! \uD83C\uDDFA\uD83C\uDDE6");
                    break;
            }
        }
    }

    private Menu getMenu(Setting userSettings) {
        menu = userSettings.getSelectedLanguage() == Language.EN ? new MenuEN(settings) :
                userSettings.getSelectedLanguage() == Language.CZ ? new MenuCZ(settings) :
                        userSettings.getSelectedLanguage() == Language.PL ? new MenuPL(settings) :
                                userSettings.getSelectedLanguage() == Language.UA ? new MenuUA(settings) : menu;
        return menu;
    }
}
