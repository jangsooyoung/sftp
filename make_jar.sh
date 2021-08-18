set -v 
javac -classpath . woorisftp.java
jar -vcfm  woorisftp.jar Manifest.txt  com woorisftp.class "woorisftp\$FileInputStreamSleep.class" *.java readme.txt


