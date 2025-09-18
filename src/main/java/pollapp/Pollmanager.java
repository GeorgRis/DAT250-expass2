package pollapp;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Manages all the application data. This class acts as a simple,
 * in-memory database for users, polls, and votes.
 */
@Component
public class Pollmanager {

    private final List<User> users = new ArrayList<>();
    private final List<Poll> polls = new ArrayList<>();
    private final List<Vote> votes = new ArrayList<>();

    // A method to clear all data. This is useful for testing.
    public void clearData() {
        users.clear();
        polls.clear();
        votes.clear();
    }

    // -- User Methods --
    public User createUser(String username, String email) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, username, email);
        users.add(user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // -- Poll Methods --
    public Poll createPoll(String question, String userId, List<String> options) {
        String id = UUID.randomUUID().toString();
        Poll poll = new Poll(id, question, Instant.now(), Instant.now().plusSeconds(86400), userId);
        polls.add(poll);

        // Add custom choices instead of hardcoded Yes/No
        if (options != null && !options.isEmpty()) {
            for (int i = 0; i < options.size(); i++) {
                Choice choice = new Choice(UUID.randomUUID().toString(), options.get(i), i + 1);
                poll.addChoice(choice);
            }
        } else {
            // Fallback to default choices if no options provided
            Choice choice1 = new Choice(UUID.randomUUID().toString(), "Yes", 1);
            Choice choice2 = new Choice(UUID.randomUUID().toString(), "No", 2);
            poll.addChoice(choice1);
            poll.addChoice(choice2);
        }

        return poll;
    }

    public List<Poll> getAllPolls() {
        return new ArrayList<>(polls);
    }

    public Poll getPollById(String id) {
        return polls.stream()
                .filter(poll -> poll.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deletePoll(String id) {
        // Find the poll to delete
        Poll pollToDelete = getPollById(id);
        if (pollToDelete == null) {
            return false;
        }
        // Remove the poll itself
        polls.remove(pollToDelete);
        votes.removeIf(vote -> vote.getPollId().equals(id));
        return true;
    }

    // -- Vote Methods --
    public Vote createVote(String userId, String pollId, String choiceId) {
        String id = UUID.randomUUID().toString();
        Vote vote = new Vote(id, userId, pollId, choiceId, Instant.now());
        votes.add(vote);
        return vote;
    }

    public Vote updateVote(String voteId, String choiceId) {
        for (Vote vote : votes) {
            if (vote.getId().equals(voteId)) {
                vote.setChoiceId(choiceId);
                return vote;
            }
        }
        return null;
    }

    public List<Vote> getVotesByPollId(String pollId) {
        return votes.stream()
                .filter(vote -> vote.getPollId().equals(pollId))
                .collect(Collectors.toList());
    }

    public Poll getPoll(String pollId) {
        return getPollById(pollId);
    }
}