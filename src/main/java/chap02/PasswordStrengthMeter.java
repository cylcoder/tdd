package chap02;

public class PasswordStrengthMeter {

  public PasswordStrength meter(String password) {
    if (password == null || password.isEmpty()) {
      return PasswordStrength.INVALID;
    }

    return switch(getMetCriteriaCounts(password)) {
      case 0, 1 -> PasswordStrength.WEAK;
      case 2 -> PasswordStrength.NORMAL;
      case 3 -> PasswordStrength.STRONG;
      default -> throw new IllegalStateException();
    };
  }

  private int getMetCriteriaCounts(String password) {
    int metCounts = 0;
    if (password.length() >= 8) {
      metCounts++;
    }
    if (meetsContainingNumberCriteria(password)) {
      metCounts++;
    }
    if (meetsContainingUppercaseCriteria(password)) {
      metCounts++;
    }
    return metCounts;
  }

  private boolean meetsContainingNumberCriteria(String password) {
    for (char ch : password.toCharArray()) {
      if (Character.isDigit(ch)) {
        return true;
      }
    }
    return false;
  }

  private boolean meetsContainingUppercaseCriteria(String password) {
    for (char ch : password.toCharArray()) {
      if (Character.isUpperCase(ch)) {
        return true;
      }
    }
    return false;
  }

}
