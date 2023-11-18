package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
    private Map<Long, FriendsStatus> friendsStatus;

    public User() {
        this.friends = new HashSet<>();
        this.friendsStatus = new HashMap<>();
    }

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
        this.friendsStatus = new HashMap<>();
    }

    public void addFriend(long id) {
        friends.add(id);
    }

    public void removeFriend(long id) {
        friends.remove(id);
    }

    public Map<Long, FriendsStatus> getFriendsStatus() {
        return friendsStatus;
    }
}
