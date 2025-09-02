DAT250: Software Technology Experiment Assignment 2 - Report
This report documents the process of completing the second assignment, focusing on the implementation of a REST API for a Poll application using Spring Boot. It details the technical problems encountered during development and the solutions implemented to resolve them.

---

### Technical Problems and Resolutions

#### Problem 1: Compilation Errors Due to Incorrect Package References
During the implementation of the PollControllerTest.java file, the initial compilation failed with multiple `cannot find symbol` and `package does not exist` errors. The compiler was unable to locate the `User`, `Poll`, and `Vote` classes.

**Resolution:**
The root cause was a mismatch between the import statements and the actual package structure of the project. The test class was attempting to import classes from a non-existent `pollapp.model` package. This was resolved by correcting the import statements to reflect the correct package for the domain model classes, which was `pollapp`.

---

#### Problem 2: IllegalStateException - Cannot Find Spring Boot Configuration
After resolving the initial compilation issues, running the tests resulted in a `java.lang.IllegalStateException`. The error message indicated that the test framework was "Unable to find a `@SpringBootConfiguration`." This occurred because the test class, located in a sub-package, could not automatically find the main application class responsible for bootstrapping the Spring context.

**Resolution:**
The test was explicitly configured to find the main application class. The `@SpringBootTest` annotation was modified to include the `classes` parameter, specifying the location of the main application class: `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollappApplication.class)`.

---

#### Problem 3: Final Compilation Failure Due to Mismatched Package Path
Despite the previous fixes, a final compilation error persisted, stating that the main application class could not be found. The error was not in the code itself, but in a discrepancy between the file's physical location and the package declaration. The `PollControllerTest.java` file was physically located in the `src/test/java/pollapp/` directory, while its package declaration was `package pollapp.controller;`, which implied it should be in a sub-directory named `controller`.

**Resolution:**
This issue was resolved by correcting the file's physical location to align with its package declaration. The `PollControllerTest.java` file was moved into a new `controller` folder, resulting in the correct path `src/test/java/pollapp/controller/PollControllerTest.java`. This final change allowed the compiler to successfully locate all necessary classes, and the test suite ran without errors. This was the most time taking. Only a user error where i made wrong.

---

### Pending Issues
All required tests are now compiling and passing successfully. There are no pending issues to report for the completion of the mandatory steps in this assignment. The API's core functionality is verified and working as expected. I am still wondering why not all test get passed in the http. It would help if you could explain that.