simulate: compile-simulation
	java Airport ${PLANES} ${TIME}

compile-simulation: Airport.java Aircraft.java Lock.java LockB.java
	javac Airport.java Aircraft.java Lock.java LockB.java

clean:
	rm *.class
