package chap07;

import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegister {

  private final WeakPasswordChecker weakPasswordChecker;
  private final UserRepository userRepository;
  private final EmailNotifier emailNotifier;

  public void register(String id, String pw, String email) {
    if (weakPasswordChecker.checkPasswordWeak(pw)) {
      throw new WeakPasswordException();
    }

    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      throw new DupIdException();
    }

    userRepository.save(new User(id, pw, email));

    emailNotifier.sendRegisterEmail(email);
  }

}
