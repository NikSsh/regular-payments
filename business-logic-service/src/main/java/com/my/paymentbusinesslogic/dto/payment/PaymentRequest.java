package com.my.paymentbusinesslogic.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

  @Schema(example = "Martin Luther King")
  @NotBlank(message = "payer full name shouldn't be blank")
  @Pattern(regexp = "^[А-ЩЬЮЯҐЄІЇA-Z][а-щьюяґєіїa-zА-ЩЬЮЯҐЄІЇA-Z\\-]{0,}\\s[А-ЩЬЮЯҐЄІЇA-Z][а-щьюяґєіїa-zА-ЩЬЮЯҐЄІЇA-Z\\-]{1,}(\\s[А-ЩЬЮЯҐЄІЇA-Z][а-щьюяґєіїa-zА-ЩЬЮЯҐЄІЇA-Z\\-]{1,})?$")
  private String payerFullName;
  @Schema(example = "1246993119")
  @NotBlank(message = "payer INN number shouldn't be blank")
  @Pattern(regexp = "^\\d{10}$", message = "INN must be a 10-digit number.")
  private String payerInn;
  @Schema(example = "5457082220254747")
  @NotBlank(message = "payer card number shouldn't be blank")
  @Pattern(regexp = "\\b\\d{16}\\b", message = "Payer card number should be a 16-digit number")
  private String payerCardNumber;
  @Schema(example = "UA213223130000026007233566001")
  @NotBlank(message = "Recipient account shouldn't be blank")
  @Pattern(regexp = "^(UA\\d{27})$", message = "The value must be in IBAN-code format"
      + "(29 characters and start with UA)")
  private String recipientAccount;
  @Schema(example = "123456")
  @NotBlank(message = "Recipient MFO shouldn't be blank")
  @Pattern(regexp = "^(\\d{6})$", message = "The MFO code must consist of 6 digits")
  private String recipientMfo;
  @Schema(example = "38528147")
  @NotBlank(message = "Recipient OKPO shouldn't be blank")
  @Pattern(regexp = "^(\\d{8,10})$", message = "The OKPO code must be 8 to 10 characters, digits only")
  private String recipientOkpo;
  @NotBlank(message = "Recipient name shouldn't be blank")
  @Pattern(regexp = "^[\\wА-ЩЬЮЯҐЄІЇа-щьюяґєії\\s.,'-]{3,160}$",
      message = "The value must be between 3 and 160 characters long")
  private String recipientName;
  @Schema(example = "100")
  @NotNull(message = "Amount shouldn't be null")
  @DecimalMin(value = "0", inclusive = false, message = "Payment amount should be greater than 0")
  private BigDecimal amount;
  @Schema(example = "PT1H30M")
  @NotNull(message = "Withdrawal period shouldn't be null")
  private Duration withdrawalPeriod;
}
