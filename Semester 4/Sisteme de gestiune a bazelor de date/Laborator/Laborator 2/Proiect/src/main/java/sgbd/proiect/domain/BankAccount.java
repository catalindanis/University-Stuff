package sgbd.proiect.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankAccount implements Entity<Long> {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private Double balance;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
