package no.hvl.dat250.jpa.polls;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PollsTest {

    private EntityManagerFactory emf;

    private void populate(jakarta.persistence.EntityManager em) {
        System.out.println("\n========== CREATING TEST DATA ==========");

        User alice = new User("alice", "alice@online.com");
        User bob = new User("bob", "bob@bob.home");
        User eve = new User("eve", "eve@mail.org");

        em.persist(alice);
        em.persist(bob);
        em.persist(eve);

        Poll poll = alice.createPoll("Vim or Emacs?");
        VoteOption vim = poll.addVoteOption("Vim");
        VoteOption emacs = poll.addVoteOption("Emacs");

        Poll poll2 = eve.createPoll("Pineapple on Pizza");
        VoteOption yes = poll2.addVoteOption("Yes! Yammy!");
        VoteOption no = poll2.addVoteOption("Mamma mia: Nooooo!");

        em.persist(poll);
        em.persist(poll2);
        em.persist(alice.voteFor(vim));
        em.persist(bob.voteFor(vim));
        em.persist(eve.voteFor(emacs));
        em.persist(eve.voteFor(yes));

        System.out.println("========== DATA CREATION COMPLETE ==========\n");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("\n========== DATABASE SETUP ==========");

        EntityManagerFactory emf = new PersistenceConfiguration("polls")
                .managedClass(Poll.class)
                .managedClass(User.class)
                .managedClass(Vote.class)
                .managedClass(VoteOption.class)
                .property(PersistenceConfiguration.JDBC_URL, "jdbc:h2:mem:polls")
                .property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, "drop-and-create")
                .property(PersistenceConfiguration.JDBC_USER, "sa")
                .property(PersistenceConfiguration.JDBC_PASSWORD, "")
                .property("hibernate.show_sql", "true")
                .property("hibernate.format_sql", "true")
                .createEntityManagerFactory();

        emf.runInTransaction(em -> {
            populate(em);
        });
        this.emf = emf;
    }

    @Test
    public void testUsers() {
        System.out.println("\n========== TESTING USER QUERIES ==========");

        emf.runInTransaction(em -> {
            System.out.println("Query 1: Count all users");
            Integer actual = (Integer) em.createNativeQuery("select count(id) from users", Integer.class).getSingleResult();
            assertEquals(3, actual);

            System.out.println("Query 2: Find user by username");
            User maybeBob = em.createQuery("select u from User u where u.username like 'bob'", User.class).getSingleResultOrNull();
            assertNotNull(maybeBob);
        });
    }

    @Test
    public void testVotes() {
        System.out.println("\n========== TESTING VOTE QUERIES (with JOINs) ==========");

        emf.runInTransaction(em -> {
            System.out.println("Complex JOIN Query: Count Vim votes");
            Long vimVotes = em.createQuery("select count(v) from Vote v join v.votesOn as o join o.poll as p join p.createdBy u where u.email = :mail and o.presentationOrder = :order", Long.class)
                    .setParameter("mail", "alice@online.com")
                    .setParameter("order", 0)
                    .getSingleResult();

            System.out.println("Complex JOIN Query: Count Emacs votes");
            Long emacsVotes = em.createQuery("select count(v) from Vote v join v.votesOn as o join o.poll as p join p.createdBy u where u.email = :mail and o.presentationOrder = :order", Long.class)
                    .setParameter("mail", "alice@online.com")
                    .setParameter("order", 1)
                    .getSingleResult();

            assertEquals(2, vimVotes);
            assertEquals(1, emacsVotes);
        });
    }

    @Test
    public void showDatabaseContent() {
        System.out.println("\n========== DATABASE CONTENT INSPECTION ==========");

        emf.runInTransaction(em -> {
            System.out.println("--- USERS ---");
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            users.forEach(u -> System.out.println("  " + u.getUsername() + " (" + u.getEmail() + ")"));

            System.out.println("--- POLLS ---");
            List<Poll> polls = em.createQuery("SELECT p FROM Poll p", Poll.class).getResultList();
            polls.forEach(p -> System.out.println("  " + p.getQuestion() + " by " + p.getCreatedBy().getUsername()));

            System.out.println("--- VOTES ---");
            List<Vote> votes = em.createQuery("SELECT v FROM Vote v", Vote.class).getResultList();
            votes.forEach(v -> System.out.println("  " + v.getVotedBy().getUsername() + " voted for: " + v.getVotesOn().getCaption()));
        });
    }
}