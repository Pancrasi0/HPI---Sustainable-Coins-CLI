package Entidades;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private final Map<String, User> users = new HashMap<>();

    public AuthManager() {
    }

    // Registra un usuario si no existe (silencioso si ya existe)
    public boolean registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
        }
        return false;
    }

    // Devuelve el User si credenciales correctas, o null si falla
    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
}
