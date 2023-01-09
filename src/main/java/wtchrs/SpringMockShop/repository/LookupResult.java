package wtchrs.SpringMockShop.repository;

import lombok.Data;

import java.util.List;

@Data
public class LookupResult<T> {
    private int count;
    private List<T> data;

    public LookupResult(List<T> data) {
        this.data = data;
        this.count = data.size();
    }
}
