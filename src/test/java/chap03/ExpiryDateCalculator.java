package chap03;

import java.time.LocalDate;

public class ExpiryDateCalculator {

  public LocalDate calculateExpiryDate(PayData payData) {
    int addedMonths = 1;
    if (payData.getFirstBillingDate() != null) {
      // 납부일 기준으로 후보 만료일 구함
      LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
      // 만약 첫 납부일의 일자와 후보 만료일의 일자가 다르다면
      if (payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
        // 후보 만료일의 일자를 첫 납부일의 일자로 변경
        return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
      }
    }
    return payData.getBillingDate().plusMonths(addedMonths);
  }

}
