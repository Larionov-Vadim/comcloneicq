@echo OFF

echo Compile
dir /s /B *.java > sources.txt
javac -d out -classpath lib\mfz-rxtx-2.2-20081207-win-x64\RXTXcomm.jar. @sources.txt
del sources.txt

echo Create reader.jar
jar -cvfm reader.jar manifest_reader.txt -C out\ .

echo Create writer.jar
jar -cvfm writer.jar manifest_writer.txt -C out\ .
