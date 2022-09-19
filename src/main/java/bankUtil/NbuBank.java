package banksUtil;

import java.util.Objects;

public class NbuBank {
    private int r030;
    private String txt;
    private float rate;
    private String cc;
    private String exchangedate ;

    public int getR030() {
        return r030;
    }

    public String getTxt() {
        return txt;
    }

    public float getRate() {
        return rate;
    }

    public String getCc() {
        return cc;
    }

    public String getExchangedate() {
        return exchangedate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NbuBank nbuBank = (NbuBank) o;
        return r030 == nbuBank.r030 && Objects.equals(txt, nbuBank.txt) && Objects.equals(rate, nbuBank.rate) &&
                Objects.equals(cc, nbuBank.cc) && Objects.equals(exchangedate, nbuBank.exchangedate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r030, txt, rate, cc, exchangedate);
    }

    @Override
    public String toString() {
        return "NbuBank{" +
                "r030=" + r030 +
                ", txt='" + txt + '\'' +
                ", rate='" + rate + '\'' +
                ", cc='" + cc + '\'' +
                ", exchangedate='" + exchangedate + '\'' +
                '}';
    }
}
