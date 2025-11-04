package chap07;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegister {

  private final WeakPasswordChecker weakPasswordChecker;

  public void register(String id, String pw, String email) {
    if (weakPasswordChecker.checkPasswordWeak(pw)) {
      throw new WeakPasswordException();
    }
  }

}
