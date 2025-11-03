package chap02;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/*
*
* */
class PasswordStrengthMeterTest {

  private final PasswordStrengthMeter meter = new PasswordStrengthMeter();

  private void assertStrength(String password, PasswordStrength expStr) {
    PasswordStrength result = meter.meter(password);
    assertThat(result).isEqualTo(expStr);
  }

  @Test
  void meetAllCriteria_Then_Strong() {
    assertStrength("ab12!@AB", PasswordStrength.STRONG);
    assertStrength("abc1!Add", PasswordStrength.STRONG);
  }

  @Test
  void meetsOtherCriteria_except_for_Length_Then_Normal() {
    assertStrength("ab12!@A", PasswordStrength.NORMAL);
    assertStrength("Ab12!c", PasswordStrength.NORMAL);
  }

  @Test
  void meetsOtherCriteria_except_for_number_Then_Normal() {
    assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
  }

  @Test
  void nullInput_Then_invalid() {
    assertStrength(null, PasswordStrength.INVALID);
  }

  @Test
  void emptyInput_Then_Invalid() {
    assertStrength("", PasswordStrength.INVALID);
  }

  @Test
  void meetsOtherCriteria_except_for_Uppercase_Then_Normal() {
    assertStrength("ab12!@df", PasswordStrength.NORMAL);
  }

  @Test
  void meetsOnlyLengthCriteria_Then_Weak() {
    assertStrength("abdefghi", PasswordStrength.WEAK);
  }

  @Test
  void meetsOnlyNumCriteria_Then_Weak() {
    assertStrength("12345", PasswordStrength.WEAK);
  }

  @Test
  void meetsOnlyUpperCriteria_Then_Weak() {
    assertStrength("ABZEF", PasswordStrength.WEAK);
  }

  @Test
  void meetsNoCriteria_Then_Weak() {
    assertStrength("abc", PasswordStrength.WEAK);
  }

}
