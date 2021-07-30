set -v 
cp sftp.jar woorisftp.jar
javac -classpath . sftp.java
jar -vcfm  sftp.jar Manifest.txt  com sftp.class "sftp\$FileInputStreamSleep.class" *.java readme.txt
cp sftp.jar ../


