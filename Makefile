compile:
	javac -d compiled/ $$(find . -name '*.java')
	javadoc -d doc/ $$(find . -name '*.java')
