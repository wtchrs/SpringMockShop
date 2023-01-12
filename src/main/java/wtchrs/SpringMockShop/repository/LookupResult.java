package wtchrs.SpringMockShop.repository;

import lombok.Data;

import java.util.List;

@Data
public class LookupResult<T> {
    private int count;
    private List<? extends T> data;

    public LookupResult(List<? extends T> data) {
        this.data = data;
        this.count = data.size();
    }
}
