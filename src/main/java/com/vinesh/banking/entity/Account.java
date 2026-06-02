@Entity
@Table(name = "accounts")

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private Double balance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}
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
