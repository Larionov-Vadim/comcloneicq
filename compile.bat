@echo OFF

echo Remove out\
del out\*

echo Compile
dir /s /B *.java > sources.txt
javac -d out -classpath lib\mfz-rxtx-2.2-20081207-win-x64\RXTXcomm.jar. @sources.txt
del sources.txt

echo Create reader.jar
jar -cvfm FirstUser.jar manifest_first_user.txt -C out\ .

echo Create writer.jar
jar -cvfm SecondUser.jar manifest_second_user.txt -C out\ .

pause