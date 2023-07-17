print-statement:
	@echo "Printing statement"
	javac src/main/*.java src/main/data/*.java src/main/exception/*.java src/main/model/*.java src/main/service/*.java
	java -cp src main.Main