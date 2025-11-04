package chap07;

import lombok.Setter;

@Setter
public class StubWeakPasswordChecker implements WeakPasswordChecker {

  private boolean isWeak;

  @Override
  public boolean checkPasswordWeak(String pw) {
    return isWeak;
  }

}
