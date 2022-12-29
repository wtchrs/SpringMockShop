package wtchrs.SpringMockShop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "Username is necessary.")
    private String username;

    private String city;
    private String street;
    private String zipcode;
}
