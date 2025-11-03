package chap03;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpiryDateCalculatorTest {

  /*
  * - 서비스를 이용하려면 매달 1만 원을 선불로 납부한다.
  * - 납부일 기준으로 한 달 뒤가 서비스 만료일이 된다.
  * - 2개월 이상 요금을 납부할 수 있다.
  * - 10만 원을 납부하면 서비스를 1년 제공한다.
  * - 첫 납부일과 만료일의 일자가 다르다면 첫 납부일 기준으로 다음 만료일을 정한다.
  * e.g. 2025-1-30 첫 납부 -> 2025-02-28 (첫 납부일과 만료일의 일자가 다름)
  *   -> 2025-03-30 (첫 납부일의 일자 기준으로 계산)
  * - 납부일 기준 일자가 만료일의 일자에 없다면 말일로 계산한다.
  * e.g. 2025-03-31 첫 납부 -> 2025-04-30
  * 납부한 금액 기준으로 서비스 만료일을 계산하는 기능을 TDD로 구현하라.
  * */

  private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
    assertThat(realExpiryDate).isEqualTo(expectedExpiryDate);
  }

  /**
   * 쉬운 것부터 테스트하라. 테스트를 추가할 때에는 다음 두 가지를 고려하라.
   * - 구현하기 쉬운 것부터 먼저 테스트
   * - 예외 상황을 먼저 테스트
   */
  @Test
  void 만원_납부하면_한달_뒤가_만료일이_됨() {
    PayData payData = PayData.builder()
        .billingDate(LocalDate.of(2019, 3, 1))
        .payAmount(10_000)
        .build();
    LocalDate expectedExpiryDate = LocalDate.of(2019, 4, 1);
    assertExpiryDate(payData, expectedExpiryDate);

    PayData payData2 = PayData.builder()
        .billingDate(LocalDate.of(2019, 5, 5))
        .payAmount(10_000)
        .build();
    LocalDate expectedExpiryDate2 = LocalDate.of(2019, 6, 5);
    assertExpiryDate(payData2, expectedExpiryDate2);
  }

  /*
   * 쉬운 구현을 하나 했다면 예외 상황을 찾아보라.
   * 단순히 한 달 추가로 끝나지 않는 상황이 존재한다.
   * 납부일이 2019-01-31이고 납부액이 1만 원이라면 만료일은 2019-02-28이다.
   * 납부일 기준으로 다음 달의 같은 날이 만료일이 아니다.
   * */
  @Test
  void 납부일과_한달_뒤_일자가_같지_않음() {
    PayData payData = PayData.builder()
        .billingDate(LocalDate.of(2019, 1, 31))
        .payAmount(10_000)
        .build();
    LocalDate expectedExpiryDate = LocalDate.of(2019, 2, 28);
    assertExpiryDate(payData, expectedExpiryDate);

    PayData payData2 = PayData.builder()
        .billingDate(LocalDate.of(2019, 5, 31))
        .payAmount(10_000)
        .build();
    LocalDate expectedExpiryDate2 = LocalDate.of(2019, 6, 30);
    assertExpiryDate(payData2, expectedExpiryDate2);

    PayData payData3 = PayData.builder()
        .billingDate(LocalDate.of(2020, 1, 31))
        .payAmount(10_000)
        .build();
    LocalDate expectedExpiryDate3 = LocalDate.of(2020, 2, 29);
    assertExpiryDate(payData3, expectedExpiryDate3);

    // LocalDate.plusMonths()가 알아서 한 달 추가 처리를 해주는 것을 확인할 수 있다.
  }

  /**
   * 한달 뒤가 만료일이 되는 테스트를 진행했으니 그다음으로 쉽거나 예외적인 것을 선택하라. 생각할 수 있는 쉬운 예 -> n만원을 지불하면 만료일이 n 달 뒤가 된다.
   * 예외적인 상황 -> 첫 납부일이 2019-01-31이고 만료되는 2019-02-28에 1만 원을 납부하면 다음 만료일은 2019-03-31이다. 이전 테스트가 1개월 요금
   * 지불을 기준으로 하므로 1개월 요금 지불에 대한 예외 상황을 마무리하고 2개월 이상 요금 지불을 테스트하자.
   */

  // 첫 납부일이 2019-01-31이고 만료되는 2019-02-28에 1만 원을 납부하면 다음 만료일은 2019-03-31이다.
  @Test
  void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
    PayData payData = PayData.builder()
        .firstBillingDate(LocalDate.of(2019, 1, 31))
        .billingDate(LocalDate.of(2019, 2, 28))
        .payAmount(10_000)
        .build();

    LocalDate expectedExpiryDate1 = LocalDate.of(2019, 3, 31);
    assertExpiryDate(payData, expectedExpiryDate1);

    PayData payData2 = PayData.builder()
        .firstBillingDate(LocalDate.of(2019, 1, 30))
        .billingDate(LocalDate.of(2019, 2, 28))
        .payAmount(10_000)
        .build();

    LocalDate expectedExpiryDate2 = LocalDate.of(2019, 3, 30);
    assertExpiryDate(payData2, expectedExpiryDate2);

    PayData payData3 = PayData.builder()
        .firstBillingDate(LocalDate.of(2019, 5, 31))
        .billingDate(LocalDate.of(2019, 6, 30))
        .payAmount(10_000)
        .build();

    LocalDate expectedExpiryDate = LocalDate.of(2019, 7, 31);
    assertExpiryDate(payData3, expectedExpiryDate);
  }

  /**
   * 쉬운 테스트부터 해보라.
   * 이번에 추가할 사례는 다음과 같다.
   * 2만 원을 지불하면 만료일이 두 달 뒤가 된다.
   * 3만 원을 지불하면 만료일이 세 달 뒤가 된다.
   */
  @Test
  void 이만원_이상_납부하면_비례해서_만료일_계산() {
    PayData payData = PayData.builder()
        .billingDate(LocalDate.of(2019, 3, 1))
        .payAmount(20_000)
        .build();

    assertExpiryDate(payData, LocalDate.of(2019, 5, 1));

    PayData payData2 = PayData.builder()
        .billingDate(LocalDate.of(2019, 3, 1))
        .payAmount(30_000)
        .build();

    LocalDate expectedExpiryDate = LocalDate.of(2019, 6, 1);
    assertExpiryDate(payData2, expectedExpiryDate);
  }

  @Test
  void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부() {
    PayData payData = PayData.builder()
        .firstBillingDate(LocalDate.of(2019, 1, 31))
        .billingDate(LocalDate.of(2019, 2, 28))
        .payAmount(20_000)
        .build();

    /*
    * 2월 28일에 2만원 납부 -> 4월 28일 (A)
    * 첫 납부일 일자 기준으로 변경 -> 4월 31일 (B)
    * 4월에는 31일이 없음 -> 4월 말일로 변경 -> 4월 30일 (C)
    * 따라서 (A)의 말일이 4월의 말일보다 크다면 4월의 말일로 수정해주는 작업이 필요
    * 판단 기준은 후보 만료일의 월 최대 일수 < 첫 납부일의 일(day)인 경우
    * 첫 납부일의 일(day)을 후보 만료일의 월 최대 일수로 수정해야 함
    * e.g. 월 최대 일수(4월의 말일은 30일) < 첫 납부일의 일(31일) 이므로 31을 30으로 수정해주는 작업이 필요
    * */

    LocalDate expectedExpiryDate = LocalDate.of(2019, 4, 30);
    assertExpiryDate(payData, expectedExpiryDate);
  }

  @Test
  void 십만원을_납부하면_1년_제공() {
    PayData payData = PayData.builder()
        .billingDate(LocalDate.of(2019, 1, 28))
        .payAmount(100_000)
        .build();
    LocalDate expectedExpiryDate = LocalDate.of(2020, 1, 28);
    assertExpiryDate(payData, expectedExpiryDate);
  }

}
