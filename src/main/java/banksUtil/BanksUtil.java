package banksUtil;

import bankUtil.Privat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import serviceClasses.Bank;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BanksUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new Gson();

    private final String PRIVAT_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=11";
    private final String PRIVAT_PLZ_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=12";
    private final String NBU_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json";
    private final String MONOBANK_URL = "https://api.monobank.ua/bank/currency";


    static Type typePrivat = new TypeToken<List<Privat>>() {
    }.getType();
    static Type typeNBU = new TypeToken<List<banksUtil.NbuBank>>() {
    }.getType();
    static Type typeMono = new TypeToken<List<banksUtil.Monobank>>() {
    }.getType();

    public Bank getPrivatAPI() throws IOException, InterruptedException {
        final List<Privat> datePrivat = sendGetBank(URI.create(PRIVAT_URL),typePrivat);
        final List<Privat> datePrivatPlz = sendGetBank(URI.create(PRIVAT_PLZ_URL),typePrivat);
        for (Privat currency : datePrivatPlz) {
            if (currency.getCcy().equals("PLZ")) {
                datePrivat.add(currency);
                break;
            }
        }
        return getPrivat(datePrivat);
    }

    public Bank getMonoAPI() throws IOException, InterruptedException {
        final List<banksUtil.Monobank> dateMono = sendGetBank(URI.create(MONOBANK_URL),typeMono);
        return getMonobank(dateMono);
    }

    public Bank getNBUAPI() throws IOException, InterruptedException {
        final List<banksUtil.NbuBank> dateNBU = sendGetBank(URI.create(NBU_URL),typeNBU);
        return getNbu(dateNBU);
    }

    public <T> List<T> sendGetBank(URI uri,Type typeBank) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), typeBank);
    }

    public Bank getPrivat(List<Privat> date) {
        Bank bank = new Bank();
        for (Privat currency : date) {
            switch (currency.getCcy()) {
                case "USD":
                    bank.setUSD_buy(currency.getBuy());
                    bank.setUSD_sell(currency.getSale());
                    break;
                case "EUR":
                    bank.setEUR_buy(currency.getBuy());
                    bank.setEUR_sell(currency.getSale());
                    break;
                case "PLZ":
                    bank.setPLN_buy(currency.getBuy());
                    bank.setPLN_sell(currency.getSale());
                    break;
                case "BTC":
                    bank.setBTC_buy(currency.getBuy());
                    bank.setBTC_sell(currency.getSale());
                    break;
            }
        }
        return bank;
    }

    public Bank getMonobank(List<banksUtil.Monobank> monobankList) {
        Bank bank = new Bank();
        for (banksUtil.Monobank currency : monobankList) {
            if (currency.getCurrencyCodeA() == 840 && currency.getCurrencyCodeB() == 980) {
                bank.setUSD_buy(currency.getRateBuy());
                bank.setUSD_sell(currency.getRateSell());
            }else if(currency.getCurrencyCodeA() == 978 && currency.getCurrencyCodeB() == 980) {
                bank.setEUR_buy(currency.getRateBuy());
                bank.setEUR_sell(currency.getRateSell());
            }else if(currency.getCurrencyCodeA() == 985) {
                bank.setPLN_buy(currency.getRateCross());
                bank.setPLN_sell(currency.getRateCross());
            }
        }
        return bank;
    }

    public Bank getNbu(List<banksUtil.NbuBank> listCurr) {
        Bank bank = new Bank();
        for (banksUtil.NbuBank currency : listCurr) {
            switch (currency.getCc()) {
                case "USD":
                    bank.setUSD_buy(currency.getRate());
                    bank.setUSD_sell(currency.getRate());
                    break;
                case "EUR":
                    bank.setEUR_buy(currency.getRate());
                    bank.setEUR_sell(currency.getRate());
                    break;
                case "PLN":
                    bank.setPLN_buy(currency.getRate());
                    bank.setPLN_sell(currency.getRate());
                    break;
                case "BTC":
                    bank.setBTC_buy(currency.getRate());
                    bank.setBTC_sell(currency.getRate());
                    break;
            }
        }
        return bank;
    }
}
