package chap07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegisterTest {

  private UserRegister userRegister;
  private final StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();
  private final MemoryUserRepository fakeRepository = new MemoryUserRepository();

  @BeforeEach
  void setUp() {
    userRegister = new UserRegister(stubWeakPasswordChecker, fakeRepository);
  }

  @DisplayName("약한 암호면 가입 실패")
  @Test
  void weakPassword() {
    stubWeakPasswordChecker.setWeak(true); // 암호가 약하다고 응답하도록 설정

    assertThatThrownBy(() -> userRegister.register("id", "pw", "email"))
        .isInstanceOf(WeakPasswordException.class);
  }

  @DisplayName("이미 같은 ID가 존재하면 가입 실패")
  @Test
  void dupIdExists() {
    fakeRepository.save(new User("id", "pw1", "email@email.com"));

    assertThatThrownBy(() -> userRegister.register("id", "pw1", "email@email.com"))
        .isInstanceOf(DupIdException.class);
  }

  @DisplayName("같은 ID가 없으면 가입 성공함")
  @Test
  void noDupId_RegisterSuccess() {
    userRegister.register("id", "pw", "email");
    User user = fakeRepository.findById("id").orElseThrow();
    assertThat(user.getId()).isEqualTo("id");
    assertThat(user.getEmail()).isEqualTo("email");
  }

}
