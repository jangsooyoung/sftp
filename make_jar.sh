set -v 
javac -classpath . woorisftp.java
jar -vcfm  sftp.jar Manifest.txt  com sftp.class "sftp\$FileInputStreamSleep.class" *.java readme.txt


