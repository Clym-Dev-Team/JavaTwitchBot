package main.system.commandSystem;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@Table(name = "Core-Commands")
@Entity
public class Command {
    @Id private String uniqueName;
    private String description;
    private String matcherString;
    private boolean regexMatcher;
    private HashSet<TwitchUserPermissions> permissions;
    private String commandText;
    private boolean activ;
    private boolean hiddenInList;
    private int cooldown;
    private CooldownType cooldownType;
    private String messageColor;
    //TODO Group System
    //TODO Action System (auto-deleteDate)

    public static CommandRepo repo;

    @Autowired
    public void setRepo(CommandRepo commandRepo) {
        repo = commandRepo;
    }

    public Command() {
    }

    public Command(String uniqueName, String description, String matcherString, boolean regexMatcher, HashSet<TwitchUserPermissions> permissions, String commandText, boolean activ, boolean hiddenInList, int cooldown, CooldownType cooldownType, String messageColor) {
        this.uniqueName = uniqueName;
        this.description = description;
        this.matcherString = matcherString;
        this.regexMatcher = regexMatcher;
        this.permissions = permissions;
        this.commandText = commandText;
        this.activ = activ;
        this.hiddenInList = hiddenInList;
        this.cooldown = cooldown;
        this.cooldownType = cooldownType;
        this.messageColor = messageColor;
    }

    public String uniqueName() {
        return uniqueName;
    }

    public Command setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
        return this;
    }

    public String description() {
        return description;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public String matcherString() {
        return matcherString;
    }

    public Command setMatcherString(String matcherString) {
        this.matcherString = matcherString;
        return this;
    }

    public boolean regexMatcher() {
        return regexMatcher;
    }

    public Command setRegexMatcher(boolean regexMatcher) {
        this.regexMatcher = regexMatcher;
        return this;
    }

    public HashSet<TwitchUserPermissions> permissions() {
        return permissions;
    }

    public Command setPermissions(HashSet<TwitchUserPermissions> permissions) {
        this.permissions = permissions;
        return this;
    }

    public String commandText() {
        return commandText;
    }

    public Command setCommandText(String commandText) {
        this.commandText = commandText;
        return this;
    }

    public boolean activ() {
        return activ;
    }

    public Command setActiv(boolean activ) {
        this.activ = activ;
        return this;
    }

    public boolean hiddenInList() {
        return hiddenInList;
    }

    public Command setHiddenInList(boolean hiddenInList) {
        this.hiddenInList = hiddenInList;
        return this;
    }

    public int cooldown() {
        return cooldown;
    }

    public Command setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public CooldownType cooldownType() {
        return cooldownType;
    }

    public Command setCooldownType(CooldownType cooldownType) {
        this.cooldownType = cooldownType;
        return this;
    }

    public String messageColor() {
        return messageColor;
    }

    public Command setMessageColor(String messageColor) {
        this.messageColor = messageColor;
        return this;
    }
}
