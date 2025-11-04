package chap03;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PayData {

  private LocalDate firstBillingDate;
  private LocalDate billingDate;
  private int payAmount;

  /*public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final PayData data = new PayData();

    public Builder billingDate(LocalDate billingDate) {
      data.billingDate = billingDate;
      return this;
    }

    public Builder payAmount(int payAmount) {
      data.payAmount = payAmount;
      return this;
    }

    public PayData build() {
      return data;
    }
  }*/

}
