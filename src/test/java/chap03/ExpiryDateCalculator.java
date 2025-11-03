package chap03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

  public static final int YEARLY_PAYMENT_THRESHOLD = 100_000;
  public static final int YEARLY_BONUS_MONTHS = 12;
  public static final int MONTHLY_FEE_UNIT = 10_000;

  public LocalDate calculateExpiryDate(PayData payData) {
    int addedMonths = calculateAddedMonths(payData.getPayAmount());
    if (payData.getFirstBillingDate() == null) { // 첫 납부일이 없으면
      return payData.getBillingDate().plusMonths(addedMonths);
    } else { // 첫 납부일이 있으면
      return expiryDateUsingFirstBillingDate(payData, addedMonths);
    }
  }

  private int calculateAddedMonths(int payAmount) {
    return payAmount == YEARLY_PAYMENT_THRESHOLD ? YEARLY_BONUS_MONTHS : payAmount / MONTHLY_FEE_UNIT;
  }

  private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addedMonths) {
    LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths); // 납부일 기준으로 후보 만료일 구함
    int dayOfFirstBilling = payData.getFirstBillingDate().getDayOfMonth(); // 첫 납부일의 일(day)
    int dayLengthOfCandiMon = lastDayOfMonth(candidateExp); // 후보 만료일의 마지막 날(day)

    if (isSameDayOfMonth(payData.getFirstBillingDate(), candidateExp)) {
      return candidateExp;
    }

    // 만약 첫 납부일의 일자와 후보 만료일의 일자가 다르다면 첫 납부일의 일자로 맞춤
    // 후보 만료일의 월 기준 말일 < 첫 납부일의 말일인 경우
    if (dayOfFirstBilling > dayLengthOfCandiMon) {
      // 후보 만료일의 일(day)를 후보 만료일의 월의 말일로 변경
      return candidateExp.withDayOfMonth(dayLengthOfCandiMon);
    }
    // 후보 만료일의 일자를 첫 납부일의 일자로 변경
    return candidateExp.withDayOfMonth(dayOfFirstBilling);
  }

  private boolean isSameDayOfMonth(LocalDate firstDate, LocalDate secondDate) {
    return firstDate.getDayOfMonth() == secondDate.getDayOfMonth();
  }

  private int lastDayOfMonth(LocalDate date) {
    return YearMonth.from(date).lengthOfMonth();
  }

}
