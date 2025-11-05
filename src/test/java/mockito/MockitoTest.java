package mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;

class MockitoTest {

  // 1. mock(Class) - 가짜 객체 생성
  @Test
  void createMock() {
    UserRepository repository = mock(UserRepository.class);
    assertThat(repository).isNotNull();
  }

  // 2. when().thenReturn()
  @Test
  void stubWithWhenThenReturn() {
    UserRepository repository = mock(UserRepository.class);
    when(repository.findById("foo")).thenReturn(new User("foo"));
    User user = repository.findById("foo");
    assertThat(user.getId()).isEqualTo("foo");
  }

  // 3. given().willReturn() - BDD style Stub
  @Test
  void stubWithGivenWillReturn() {
    UserRepository repository = mock(UserRepository.class);
    given(repository.findById("foo")).willReturn(new User("foo"));
    User user = repository.findById("foo");
    assertThat(user.getId()).isEqualTo("foo");
  }

  // 4. verify(mock) - 메서드 호출 여부 검증
  @Test
  void verifyMethodCall() {
    UserRepository repository = mock(UserRepository.class);
    repository.save(new User("foo"));
    verify(repository).save(any());
    verify(repository).save(any(User.class));
  }

  // 5. verify(mock, times(n)) - 호출 횟수 검증
  @Test
  void verifyMethodCallCount() {
    UserRepository repository = mock(UserRepository.class);
    repository.save(new User("foo"));
    repository.save(new User("bar"));
    verify(repository, times(2)).save(any(User.class));
  }

  // 6. verify(mock, never)) - 호출되지 않았는지 확인
  @Test
  void verifyNeverCalled() {
    UserRepository repository = mock(UserRepository.class);
    verify(repository, never()).save(any(User.class));
  }

  // 7. doThrow().when(mock).method() - 예외 던지도록 설정
  @Test
  void stubThrowException() {
    UserRepository repository = mock(UserRepository.class);
    doThrow(new RuntimeException("DB error")).when(repository).save(any(User.class));
    assertThatThrownBy(() -> repository.save(new User("foo")))
        .isInstanceOf(RuntimeException.class);
  }

  // 8. doReturn().when(mock).method() - 특정 리턴 값
  @Test
  void spyDoReturnExample() {
    List<String> realList = new ArrayList<>();
    List<String> spyList = spy(realList);
    doReturn(100).when(spyList).size();
    assertThat(spyList).hasSize(100);
  }

  /**
   * given().willReturn()이 있는데 doReturn().when().method()가 필요한 이유
   * given().willReturn()은 given 절의 메서드를 실행한 뒤
   * Mockito가 그 호출 내용을 가로채서 이후 같은 호출이 들어오면
   * willReturn()의 값으로 반환하도록 Stub을 등록한다.
   * 즉, stub 설정 시점에 메서드 호출이 1번 일어난다.
   * 따라서 Spy일 때는 실제 메서드가 실행되는데 그걸 원하지 않는 경우 doReturn() 방식을 사용한다.
   * 아래는 Spy에 given().willReturn()을 써서 문제가 발생하는 사례이다.
   */
  @Getter
  static class ExpensiveService {
    public String loadData() {
      System.out.println("경고: 부하가 큰 실제 로직 실행되므로 실행되면 안됨");
      throw new RuntimeException("과부하 발생");
    }
  }

  @Test
  void givenWillReturnCausesSideEffectOnSpy() {
    ExpensiveService realService = new MockitoTest.ExpensiveService();
    ExpensiveService spyService = spy(realService);
    assertThatThrownBy(() -> given(spyService.loadData()).willReturn("STUBBED_DATA"))
        .isInstanceOf(RuntimeException.class);
  }

  // 9. spy(realObject) - 실제 객체 감시
  @Test
  void spyRealObject() {
    List<String> realList = new ArrayList<>();
    List<String> spyList = spy(realList);

    spyList.add("a");
    spyList.add("b");

    verify(spyList).add("a"); // "a"를 인수로 add()가 호출되었는지 검증
    verify(spyList).add("b"); // "b"를 인수로 add()가 호출되었는지 검증
    assertThat(spyList).hasSize(2);
  }

}