ROOT=/Users/zhengjiaye/projects/java_proj/static_analysis_lab/flow_analysis
CLASS=IntraCPTest
FORMAT=jimple
SOOT=$(ROOT)/src/main/resources/soot-4.2.1-jar-with-dependencies.jar
OUT=$(ROOT)/sootOutput
WORK_DIR=$(ROOT)/src/test/test-code/constantPropagation/
SRC=$(WORK_DIR)/$(CLASS).java

all: $(CLASS).class
	java -cp $(SOOT) soot.Main -f $(FORMAT) -d $(OUT) -process-path $(WORK_DIR) $(CLASS)

$(CLASS).class: $(SRC)
	javac $(SRC)

clean:
	rm $(OUT)/*
	 
