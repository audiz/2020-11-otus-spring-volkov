Student Testing App (question output only)
Goal: Create an app using Spring IoC to get familiar with the core IoC functionality that all of Spring is built on.
The result: a simple application configured with an XML context.
Task Description:

The resources store questions and various answers to them in the form of a CSV file (5 questions).
Questions can be with a choice of several options or with a free answer - at your desire and discretion.
The application should simply output the test questions from a CSV file with possible answers.

Requirements:
0. The application must have an object model (we prefer objects and classes, not strings and arrays/lists of strings).
1. All classes in the application must solve a strictly defined task (see p. 18-19 "Rules for the design of the.pdf code" attached to the lesson materials).
2. The context is described by an XML file.
3. All dependencies must be configured in the IoC container.
4. The name of the resource with questions (CSV file) must be hardcoded with a line in the XML file with the context.
5. CSV with questions is read as a resource, not as a file.
6. Scanner, PrintStream and other standard types do not need to be put in the context!
7. All I/O must be done in English.
8. It is highly desirable to write an unit test of some service (only an attempt to write a test will be evaluated).
9. Remember - "without fanaticism".

Optional (task with "star"):
1*. The application must be launched correctly using " java-jar"

Homework score - 9/10