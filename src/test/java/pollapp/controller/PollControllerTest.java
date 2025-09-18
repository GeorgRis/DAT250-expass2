//package pollapp.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import pollapp.DemoApplication;
//import pollapp.Pollmanager;
//import pollapp.Poll;
//import pollapp.User;
//import no.hvl.dat250.jpa.polls.Vote;
//
//import java.time.Instant;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
//public class PollControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private Pollmanager pollmanager;
//
//    @BeforeEach
//    public void setup() {
//        // Clear all data before each test run to ensure a clean slate
//        pollmanager.clearData();
//    }
//
//    @Test
//    public void testFullPollWorkflow() {
//        // Step 1: Create UserOne
//        User userOne = new User(null, "UserOne", "user.one@example.com");
//        ResponseEntity<User> userOneResponse = restTemplate.postForEntity("http://localhost:" + port + "/users", userOne, User.class);
//        assertThat(userOneResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(userOneResponse.getBody()).isNotNull();
//        String createdUserOneId = userOneResponse.getBody().getId();
//
//        // Step 2: Get all users (should contain one user)
//        ResponseEntity<List<User>> userListResponse1 = restTemplate.exchange(
//                "http://localhost:" + port + "/users", HttpMethod.GET, null, new org.springframework.core.ParameterizedTypeReference<List<User>>() {});
//        assertThat(userListResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(userListResponse1.getBody()).isNotNull();
//        assertThat(userListResponse1.getBody().size()).isEqualTo(1);
//
//        // Step 3: Create UserTwo
//        User userTwo = new User(null, "UserTwo", "user.two@example.com");
//        ResponseEntity<User> userTwoResponse = restTemplate.postForEntity("http://localhost:" + port + "/users", userTwo, User.class);
//        assertThat(userTwoResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(userTwoResponse.getBody()).isNotNull();
//        String createdUserTwoId = userTwoResponse.getBody().getId();
//
//        // Step 4: Get all users again (should contain two users)
//        ResponseEntity<List<User>> userListResponse2 = restTemplate.exchange(
//                "http://localhost:" + port + "/users", HttpMethod.GET, null, new org.springframework.core.ParameterizedTypeReference<List<User>>() {});
//        assertThat(userListResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(userListResponse2.getBody()).isNotNull();
//        assertThat(userListResponse2.getBody().size()).isEqualTo(2);
//
//        // Step 5: Create a new poll from User One
//        Poll newPoll = new Poll(null, "What is your favorite programming language?", Instant.now(), Instant.now().plusSeconds(86400), createdUserOneId);
//        ResponseEntity<Poll> pollResponse = restTemplate.postForEntity(
//                "http://localhost:" + port + "/users/" + createdUserOneId + "/polls", newPoll, Poll.class);
//        assertThat(pollResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(pollResponse.getBody()).isNotNull();
//        String createdPollId = pollResponse.getBody().getId();
//        String choiceOneId = pollResponse.getBody().getChoices().get(0).getId();
//
//        // Step 6: Get all polls
//        ResponseEntity<List<Poll>> pollListResponse = restTemplate.exchange(
//                "http://localhost:" + port + "/polls", HttpMethod.GET, null, new org.springframework.core.ParameterizedTypeReference<List<Poll>>() {});
//        assertThat(pollListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(pollListResponse.getBody()).isNotNull();
//        assertThat(pollListResponse.getBody().size()).isEqualTo(1);
//
//        // Step 7: User Two votes for the first choice
//        Vote newVote = new Vote(null, createdUserTwoId, createdPollId, choiceOneId, Instant.now());
//        ResponseEntity<Vote> voteResponse = restTemplate.postForEntity(
//                "http://localhost:" + port + "/users/" + createdUserTwoId + "/votes", newVote, Vote.class);
//        assertThat(voteResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(voteResponse.getBody()).isNotNull();
//        String createdVoteId = voteResponse.getBody().getId();
//
//        // Step 8: Get votes for the poll
//        ResponseEntity<List<Vote>> votesListResponse = restTemplate.exchange(
//                "http://localhost:" + port + "/polls/" + createdPollId + "/votes", HttpMethod.GET, null, new org.springframework.core.ParameterizedTypeReference<List<Vote>>() {});
//        assertThat(votesListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(votesListResponse.getBody()).isNotNull();
//        assertThat(votesListResponse.getBody().size()).isEqualTo(1);
//
//        // Step 9: User One deletes the poll
//        restTemplate.delete("http://localhost:" + port + "/polls/" + createdPollId);
//
//        // Step 10: Get votes for the poll again (should be an empty list)
//        ResponseEntity<List<Vote>> votesListResponseAfterDelete = restTemplate.exchange(
//                "http://localhost:" + port + "/polls/" + createdPollId + "/votes", HttpMethod.GET, null, new org.springframework.core.ParameterizedTypeReference<List<Vote>>() {});
//        assertThat(votesListResponseAfterDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(votesListResponseAfterDelete.getBody()).isNotNull();
//        assertThat(votesListResponseAfterDelete.getBody()).isEmpty();
//    }
//}
