package model;

import lombok.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGoogle {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String image;
    private Date dob;
    private int roleId;
    private int Status;
}
