package chap07;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryUserRepository implements UserRepository {

  private final Map<String, User> users = new HashMap<>();

  @Override
  public void save(User user) {
    users.put(user.getId(), user);
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(users.get(id));
  }

}
