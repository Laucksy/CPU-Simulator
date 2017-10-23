compile:
	javac -d compiled/ $$(find . -name '*.java')
	javadoc -d doc/ $$(find . -name '*.java')
assemble1:
	cd compiled ; java Assembler.Assembler ../data/test.as
assemble2:
	cd compiled ; java Assembler.Assembler ../data/branching.as
visualize1:
	cd compiled ; java Visualizer.Visualizer ../data/test.o
visualize2:
	cd compiled ; java Visualizer.Visualizer ../data/branching.o
simulate1:
	cd compiled ; java Simulator.Simulator ../data/test.o
simulate2:
	cd compiled ; java Simulator.Simulator ../data/branching.o
simulate1noisy:
	cd compiled ; java Simulator.Simulator ../data/test.o noisy
simulate2noisy:
	cd compiled ; java Simulator.Simulator ../data/branching.o noisy

