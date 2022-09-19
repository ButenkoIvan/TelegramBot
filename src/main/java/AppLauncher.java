import keyboards.Menu;
import keyboards.MenuUA;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import serviceClasses.CurrencyDataBase;
import settings.Settings;

public class AppLauncher {
    public static void main(String[] args) throws TelegramApiException {
        CurrencyDataBase currencyDataBase = new CurrencyDataBase();
        Settings settings = new Settings(currencyDataBase);
        settings.load();


        Timer timer = new Timer(settings);
        Thread messageInTime = new Thread(timer);
        messageInTime.start();

        CurrencyInfoBot currencyInfoBot = CurrencyInfoBot.getInstance("currencyInfoBot", settings);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(currencyInfoBot);
    }
}
