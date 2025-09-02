package pollapp.controller;

import pollapp.Poll;
import pollapp.Pollmanager;
import pollapp.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Poll-related endpoints.
 * This class handles incoming HTTP requests and uses the Pollmanager
 * to retrieve and return poll data.
 */
@RestController
@RequestMapping
public class Pollcontroller {

    private final Pollmanager pollManager;

    public Pollcontroller(Pollmanager pollManager) {
        this.pollManager = pollManager;
    }

    // -- Poll Endpoints --

    @GetMapping("/polls")
    public List<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    @PostMapping("/users/{userId}/polls")
    public ResponseEntity<Poll> createPoll(@PathVariable String userId, @RequestBody Poll poll) {
        Poll createdPoll = pollManager.createPoll(poll.getQuestion(), userId);
        return new ResponseEntity<>(createdPoll, HttpStatus.CREATED);
    }

    @DeleteMapping("/polls/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable String pollId) {
        boolean deleted = pollManager.deletePoll(pollId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/internal/clear")
    public ResponseEntity<Void> clearAllData() {
        pollManager.clearData();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // -- Vote Endpoints --

    @PostMapping("/users/{userId}/votes")
    public ResponseEntity<Vote> createVote(@PathVariable String userId, @RequestBody Vote vote) {
        Vote createdVote = pollManager.createVote(userId, vote.getPollId(), vote.getChoiceId());
        if (createdVote != null) {
            return new ResponseEntity<>(createdVote, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users/{userId}/votes/{voteId}")
    public ResponseEntity<Vote> updateVote(@PathVariable String userId, @PathVariable String voteId, @RequestBody Vote vote) {
        Vote updatedVote = pollManager.updateVote(voteId, vote.getChoiceId());
        if (updatedVote != null) {
            return new ResponseEntity<>(updatedVote, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/polls/{pollId}/votes")
    public List<Vote> getVotesForPoll(@PathVariable String pollId) {
        return pollManager.getVotesByPollId(pollId);
    }
}
