package chap02;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CalculatorTest {

  @Test
  void plus() {
    int result = Calculator.plus(1, 2);
    assertThat(result).isEqualTo(3);
    assertThat(Calculator.plus(4, 1)).isEqualTo(5);
  }

}
