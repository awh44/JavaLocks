simulate: compile-simulation
	@java Airport ${PLANES} ${TIME}

compile-simulation: Airport.java Aircraft.java Lock.java LockB.java LockC.java
	@javac Airport.java Aircraft.java Lock.java LockB.java LockC.java

B-test: compile-test
	@java Main B

C-test: compile-test
	@java Main C

compile-test: Main.java Test.java Lock.java LockB.java LockC.java
	@javac Main.java Test.java Lock.java LockB.java LockC.java

clean:
	rm *.class
