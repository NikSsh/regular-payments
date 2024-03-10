package com.my.paymentdao.persistence.domain.entity;

import com.my.paymentdao.dto.payment.PaymentBriefResponse;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@NamedNativeQuery(name = "Payment.findAllWithLastWithdrawalDateTime",
    query =
          "SELECT p.id, p.withdrawal_period as withdrawalPeriod, "
        + "p.created_at as createdAt, pw.lastWithdrawal "
        + "FROM regular_payments.payments p "
        + "LEFT JOIN ( "
              + "SELECT pw.payment_id, MAX(pw.created_at) as lastWithdrawal "
              + "FROM regular_payments.payment_withdrawals pw "
              + "GROUP BY pw.payment_id "
        + ") pw ON p.id = pw.payment_id "
        + "GROUP BY p.id, pw.lastWithdrawal;",
    resultSetMapping = "Mapping.PaymentWithLastWithdrawal")
@SqlResultSetMapping(name = "Mapping.PaymentWithLastWithdrawal",
    classes = @ConstructorResult(targetClass = PaymentBriefResponse.class,
        columns = {@ColumnResult(name = "id"),
            @ColumnResult(name = "withdrawalPeriod", type = Duration.class),
            @ColumnResult(name = "lastWithdrawal", type = LocalDateTime.class),
            @ColumnResult(name = "createdAt", type = LocalDateTime.class)}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "payer_full_name")
  private String payerFullName;
  @Column(name = "payer_inn")
  private String payerInn;
  @Column(name = "payer_card_number")
  private String payerCardNumber;
  @Column(name = "recipient_account")
  private String recipientAccount;
  @Column(name = "recipient_mfo")
  private String recipientMfo;
  @Column(name = "recipient_okpo")
  private String recipientOkpo;
  @Column(name = "recipient_name")
  private String recipientName;
  @Column(name = "withdrawal_period")
  private Duration withdrawalPeriod;
  @Column(name = "amount")
  private BigDecimal amount;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @OneToMany(mappedBy = "payment")
  private Set<PaymentWithdrawal> paymentWithdrawals;
}
