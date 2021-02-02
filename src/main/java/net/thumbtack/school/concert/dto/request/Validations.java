package net.thumbtack.school.concert.dto.request;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validations {
    public static boolean tokenIsValid(String token) {
        return token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    public static boolean firstNameIsValid(String firstName) {
        return firstName.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean lastNameIsValid(String lastName) {
        return lastName.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean loginIsValid(String login) {
        return login.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean passwordIsValid(String password) {
        return password.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean nameOfSongIsValid(String nameSong) {
        return nameSong.matches("^[a-zA-Z0-9]{1,20}$");
    }

    public static boolean namesOfSongsIsValid(Set<String> namesSong) {
        for (String name : namesSong) if (!nameOfSongIsValid(name)) return false;
        return true;
    }

    public static boolean composersIsValid(Set<String> composers) {
        for (String s : composers) if (!s.matches("^[a-zA-Z0-9]{1,20}$")) return false;
        return true;
    }

    public static boolean authorsIsValid(Set<String> authors) {
        for (String s : authors) if (!s.matches("^[a-zA-Z0-9]{1,20}$")) return false;
        return true;
    }

    public static boolean performerIsValid(String performer) {
        return performer.matches("^[a-zA-Z0-9]{1,20}$");
    }

    public static boolean songDurationIsValid(int songDuration) {
        return songDuration <= 500 && songDuration >= 30;
    }

    public static boolean ratingIsValid(int rating) {
        return rating >= 1 && rating <= 5;
    }

    public static boolean commentIsValid(String text) {
        return text.matches("^[a-zA-Z]{1,50}$");
    }

    public static boolean songsIsValid(List<String> nameOfSong, List<Set<String>> composers, List<Set<String>> authors, List<String> performer, List<Integer> songDuration) {
        for (int i = 0; i < nameOfSong.size(); i++) {
            if (!(nameOfSongIsValid(nameOfSong.get(i)) &&
                    composersIsValid(composers.get(i)) &&
                    authorsIsValid(authors.get(i)) &&
                    performerIsValid(performer.get(i)) &&
                    songDurationIsValid(songDuration.get(i)))) return false;
        }
        return nameOfSong.size() == new HashSet<>(nameOfSong).size(); // Проверка на одинаковые названия песен
    }
}
