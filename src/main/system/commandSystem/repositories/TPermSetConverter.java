package main.system.commandSystem.repositories;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import main.system.commandSystem.repositories.TwitchUserPermissions;

import java.util.HashSet;
import java.util.StringJoiner;

//TODO This entire thing should be thrown out and replaced by some m:n tables (note, maybe this set based permission system will be thrown out to)
@Converter
public class TPermSetConverter implements AttributeConverter<HashSet<TwitchUserPermission>, String> {
    @Override
    public String convertToDatabaseColumn(HashSet<TwitchUserPermission> twitchUserPermissions) {
        StringJoiner joiner = new StringJoiner("; ");
        for (TwitchUserPermission permissions : twitchUserPermissions) {
            joiner.add(permissions.name());
        }
        return joiner.toString();
    }

    @Override
    public HashSet<TwitchUserPermission> convertToEntityAttribute(String s) {
        String[] strings = s.split(";");
        HashSet<TwitchUserPermission> permissions = new HashSet<>();
        for (String s1 : strings) {
            permissions.add(TwitchUserPermission.valueOf(s1.trim()));
        }
        return permissions;
    }

//    @Override
//    public TwitchUserPermissions[] convertToDatabaseColumn(HashSet<TwitchUserPermissions> twitchUserPermissions) {
//        return twitchUserPermissions.toArray(TwitchUserPermissions[]::new);
//    }
//
//    @Override
//    public HashSet<TwitchUserPermissions> convertToEntityAttribute(TwitchUserPermissions[] twitchUserPermissions) {
//        return new HashSet<>(List.of(twitchUserPermissions));
//    }
}
