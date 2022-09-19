package serviceClasses;

import settings.Currency;

import java.time.LocalDateTime;
import java.util.Objects;

public class Bank {

    private Float USD_buy = 0.0f;
    private Float USD_sell = 0.0f;
    private Float EUR_buy = 0.0f;
    private Float EUR_sell = 0.0f;
    private Float PLN_buy = 0.0f;
    private Float PLN_sell = 0.0f;
    private Float BTC_buy = 0.0f;
    private Float BTC_sell = 0.0f;
    private LocalDateTime time;


    public void setUSD_buy(Float USD_buy) {
        this.USD_buy = USD_buy;
    }

    public void setUSD_sell(Float USD_sell) {
        this.USD_sell = USD_sell;
    }

    public void setEUR_buy(Float EUR_buy) {
        this.EUR_buy = EUR_buy;
    }

    public void setEUR_sell(Float EUR_sell) {
        this.EUR_sell = EUR_sell;
    }

    public void setPLN_buy(Float PLN_buy) {
        this.PLN_buy = PLN_buy;
    }

    public void setPLN_sell(Float PLN_sell) {
        this.PLN_sell = PLN_sell;
    }

    public void setBTC_buy(Float BTC_buy) {
        this.BTC_buy = BTC_buy;
    }

    public void setBTC_sell(Float BTC_sell) {
        this.BTC_sell = BTC_sell;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(USD_buy, bank.USD_buy)
                && Objects.equals(USD_sell, bank.USD_sell)
                && Objects.equals(EUR_buy, bank.EUR_buy)
                && Objects.equals(EUR_sell, bank.EUR_sell)
                && Objects.equals(PLN_buy, bank.PLN_buy)
                && Objects.equals(PLN_sell, bank.PLN_sell)
                && Objects.equals(BTC_buy, bank.BTC_buy)
                && Objects.equals(BTC_sell, bank.BTC_sell)
                && Objects.equals(time, bank.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(USD_buy, USD_sell, EUR_buy, EUR_sell, PLN_buy, PLN_sell, BTC_buy, BTC_sell, time);
    }

    @Override
    public String toString() {
        return "Bank{" +
                ", USD_buy=" + USD_buy +
                ", USD_sell=" + USD_sell +
                ", EUR_buy=" + EUR_buy +
                ", EUR_sell=" + EUR_sell +
                ", PLN_buy=" + PLN_buy +
                ", PLN_sell=" + PLN_sell +
                ", BTC_buy=" + BTC_buy +
                ", BTC_sell=" + BTC_sell +
                ", time=" + time +
                '}';
    }

    public Float getBuyRate (Currency currency){
        switch (currency){
            case EUR:
                return this.EUR_buy;
            case USD:
                return this.USD_buy;
            case PLN:
                return this.PLN_buy;
            case BTC:
                return this.BTC_buy;
        }
        return null;
    }

    public Float getSellRate (Currency currency){
        switch (currency){
            case EUR:
                return this.EUR_sell;
            case USD:
                return this.USD_sell;
            case PLN:
                return this.PLN_sell;
            case BTC:
                return this.BTC_sell;
        }
        return null;
    }
}