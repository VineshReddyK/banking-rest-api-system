@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private Double balance;
    public Account() {
    }
    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getAccountNumber() {
    return accountNumber;
}

public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
}

public Double getBalance() {
    return balance;
}

public void setBalance(Double balance) {
    this.balance = balance;
}
}
