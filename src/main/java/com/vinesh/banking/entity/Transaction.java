@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private Double amount;
private String transactionType;
private LocalDateTime transactionDate;
    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Double getAmount() {
    return amount;
}

public void setAmount(Double amount) {
    this.amount = amount;
}

public String getTransactionType() {
    return transactionType;
}

public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
}

public LocalDateTime getTransactionDate() {
    return transactionDate;
}

public void setTransactionDate(LocalDateTime transactionDate) {
    this.transactionDate = transactionDate;
}
}
