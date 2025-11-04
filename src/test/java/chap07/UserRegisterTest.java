package chap07;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegisterTest {

  private UserRegister userRegister;
  private final StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();

  @BeforeEach
  void setUp() {
    userRegister = new UserRegister(stubWeakPasswordChecker);
  }

  @DisplayName("약한 암호면 가입 실패")
  @Test
  void weakPassword() {
    stubWeakPasswordChecker.setWeak(true); // 암호가 약하다고 응답하도록 설정

    assertThatThrownBy(() -> userRegister.register("id", "pw", "email"))
        .isInstanceOf(WeakPasswordException.class);
  }

}
