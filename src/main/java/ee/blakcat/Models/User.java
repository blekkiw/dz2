package ee.blakcat.Models;


import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {

private String id;
private String login;
private String password;


    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.id = UUID.randomUUID().toString();
    }
}
