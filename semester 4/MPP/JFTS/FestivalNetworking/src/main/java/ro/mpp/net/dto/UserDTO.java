package ro.mpp.net.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ro.mpp.net.dto._netutils.FestivalDTO;

@Getter
@AllArgsConstructor
@ToString
public class UserDTO implements FestivalDTO {
    private int id;
    private String username;
    private String password;
}
