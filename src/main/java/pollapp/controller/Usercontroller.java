package pollapp.controller;

import pollapp.Pollmanager;
import pollapp.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * REST controller for User-related endpoints.
 * This class handles incoming HTTP requests and uses the Pollmanager
 * to create and retrieve user data.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class Usercontroller {

    private final Pollmanager pollManager;

    /**
     * Constructor-based dependency injection for Pollmanager.
     * @param pollManager The Pollmanager instance to be injected.
     */
    public Usercontroller(Pollmanager pollManager) {
        this.pollManager = pollManager;
    }

    /**
     * Handles GET requests to retrieve a list of all users.
     * Accessible at: GET /api/users
     * @return A list of all User objects.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return pollManager.getAllUsers();
    }

    /**
     * Handles POST requests to create a new user.
     * Accessible at: POST /api/users
     * @param user The User object to create, received from the request body.
     * @return A ResponseEntity with the newly created User object and a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = pollManager.createUser(user.getUsername(), user.getEmail());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
