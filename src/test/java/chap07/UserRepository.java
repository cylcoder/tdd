package chap07;

import java.util.Optional;

public interface UserRepository {

  void save(User user);

  Optional<User> findById(String id);

}
