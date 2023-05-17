package main.inputs.TipeeeStream;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "Donations")
@Component
public class DonationEvent {
    @Id
    long id;
    //The symbol (and label) could be enough, if it works, but i don't think it will. The Code would enable conversions
    //the symbol and label give options
    public String currency_code;
    public String currency_symbol;
    public String currency_label;

    public double amount;
    public String message;
    public boolean hasMessage;
    public String tipeee_username;
    public ZonedDateTime donated_at;

    public DonationEvent() {
    }

    public DonationEvent(long id, String currency_code, String currency_symbol, String currency_label, double amount, String message, boolean hasMessage, String tipeee_username, ZonedDateTime donated_at) {
        this.id = id;
        this.currency_code = currency_code;
        this.currency_symbol = currency_symbol;
        this.currency_label = currency_label;
        this.amount = amount;
        this.message = message;
        this.hasMessage = hasMessage;
        this.tipeee_username = tipeee_username;
        this.donated_at = donated_at;
    }

    @Autowired
    public void setDonationRepo(DonationRepo donationRepo) {
        repo = donationRepo;
    }
    public static DonationRepo repo;



    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String banner = "===================-DONATION " + id + "-===================";
        str.append(banner).append("\n")
                .append("created_at = ").append(donated_at).append("\n")
                .append("from = ").append(tipeee_username).append("\n")
                .append("donation = ").append(amount).append(currency_symbol).append("\n");
        if (hasMessage)
            str.append("message = ").append(message).append("\n");
        str.append("=".repeat(banner.length()));

        return str.toString();
    }
}
