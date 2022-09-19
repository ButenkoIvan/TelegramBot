package settings;

import java.util.List;

public class IntermediateSetting {
    private Long chatId;
    private String numberOfDecimalPlaces;
    private String selectedBank;
    private List<String> selectedCurrency;
    private String notificationTime;
    private String zoneId;
    private String language;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setNumberOfDecimalPlaces(String numberOfDecimalPlaces) {
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    public void setSelectedBank(String selectedBank) {
        this.selectedBank = selectedBank;
    }

    public void setSelectedCurrency(List<String> selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getNumberOfDecimalPlaces() {
        return numberOfDecimalPlaces;
    }

    public String getSelectedBank() {
        return selectedBank;
    }

    public List<String> getSelectedCurrency() {
        return selectedCurrency;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
