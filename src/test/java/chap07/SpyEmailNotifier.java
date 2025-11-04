package chap07;

import lombok.Getter;

@Getter
public class SpyEmailNotifier implements EmailNotifier {

  private boolean isCalled;
  private String email;

  @Override
  public void sendRegisterEmail(String email) {
    isCalled = true;
    this.email = email;
  }

}
