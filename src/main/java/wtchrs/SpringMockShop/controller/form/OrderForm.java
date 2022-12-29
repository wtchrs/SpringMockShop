package wtchrs.SpringMockShop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class OrderForm {

    @NotNull
    Long memberId;
    @NotNull
    Long itemId;

    @Min(value = 1)
    int count;
}
