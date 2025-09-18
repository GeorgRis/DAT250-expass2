One problem with the guide was that i did not know to make new files or use the ones i had.
I ended up making new to fit the JPA and adding the test. The code was not a porblem, but had a little bit problems understanding how it all fit together.
It has become queite a "messy" file system i thing know. I would like some tips for that in the guide.


For the database: The @Id and @GeneratedValue annotations on the id field in each class tell JPA to create a primary key for each table and to have the database automatically generate a unique value for it. 
The @OneToMany and @ManyToOne annotations define the relationships between the tables, and JPA uses this information to create the foreign key constraints to maintain data integrity.


users table: Created from User.java. This table would have columns for id, username, and email, as defined in the class. The @Column(unique = true) annotation on the username field tells JPA to create a unique constraint on the corresponding column in the table.

polls table: Created from Poll.java. This table would have columns for id, question, publishedAt, and validUntil. It would also have a foreign key column named created_by_id to link back to the users table, as specified by the @ManyToOne and @JoinColumn annotations.

vote_options table: This table would contain id, caption, and presentationOrder columns. It would also have a foreign key column named poll_id to link to the polls table.

votes table: Created from Vote.java. This table would have id, and votedAt columns, as well as foreign key columns voted_by_id and votes_on_id to link back to the users and vote_options tables, respectively.


Felt it was better to do this in the same repo as expass2. No pending issue.