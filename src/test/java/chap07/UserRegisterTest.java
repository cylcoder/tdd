package chap07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegisterTest {

  private UserRegister userRegister;
  private final StubWeakPasswordChecker weakPasswordChecker = new StubWeakPasswordChecker();
  private final MemoryUserRepository userRepository = new MemoryUserRepository();
  private final SpyEmailNotifier emailNotifier = new SpyEmailNotifier();

  @BeforeEach
  void setUp() {
    userRegister = new UserRegister(weakPasswordChecker, userRepository, emailNotifier);
  }

  @DisplayName("약한 암호면 가입 실패")
  @Test
  void weakPassword() {
    weakPasswordChecker.setWeak(true); // 암호가 약하다고 응답하도록 설정

    assertThatThrownBy(() -> userRegister.register("id", "pw", "email"))
        .isInstanceOf(WeakPasswordException.class);
  }

  @DisplayName("이미 같은 ID가 존재하면 가입 실패")
  @Test
  void dupIdExists() {
    userRepository.save(new User("id", "pw1", "email@email.com"));

    assertThatThrownBy(() -> userRegister.register("id", "pw1", "email@email.com"))
        .isInstanceOf(DupIdException.class);
  }

  @DisplayName("같은 ID가 없으면 가입 성공함")
  @Test
  void noDupId_RegisterSuccess() {
    userRegister.register("id", "pw", "email");
    User user = userRepository.findById("id").orElseThrow();
    assertThat(user.getId()).isEqualTo("id");
    assertThat(user.getEmail()).isEqualTo("email");
  }

  /**
   * UserRegister가 회원가입 후 이메일을 전송한다고 가정하자.
   * 이때 진짜 메일 서버에 연결하지 않고 메일이 보내졌는지만 확인하고 싶다.
   */
  @DisplayName("가입하면 메일을 전송함")
  @Test
  void whenRegisterThenSendMail() {
    userRegister.register("id","pw", "email@email.com");
    assertThat(emailNotifier.isCalled()).isTrue();
    assertThat(emailNotifier.getEmail()).isEqualTo("email@email.com");
  }

}
