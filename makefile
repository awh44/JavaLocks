simulate: compile-simulation
	@java Main ${PLANES} ${TIME}

simulate-with-C: compile-simulation
	@java Main ${PLANES} ${TIME} C

compile-simulation: Main.java Aircraft.java Lock.java LockB.java LockC.java
	@javac Main.java Aircraft.java Lock.java LockB.java LockC.java

B-test-print: compile-test
	@java TestMain B print

B-test-count: compile-test
	@java TestMain B count

C-test-print: compile-test
	@java TestMain C print

C-test-count: compile-test
	@java TestMain C count

compile-test: TestMain.java Test.java Lock.java LockB.java LockC.java
	@javac TestMain.java Test.java Lock.java LockB.java LockC.java

clean:
	rm *.class
