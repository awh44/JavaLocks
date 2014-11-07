simulate: compile-simulation
	@java Main ${PLANES} ${TIME}

compile-simulation: Main.java Aircraft.java Lock.java LockB.java LockC.java
	@javac Main.java Aircraft.java Lock.java LockB.java LockC.java

B-test: compile-test
	@java TestMain B

C-test: compile-test
	@java TestMain C

compile-test: TestMain.java Test.java Lock.java LockB.java LockC.java
	@javac TestMain.java Test.java Lock.java LockB.java LockC.java

clean:
	rm *.class
