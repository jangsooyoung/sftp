set -v 
#jar xvf  sftp.jar  ==> com directory 
javac -classpath . sftp.java
jar -vcfm  sftp.jar Manifest.txt  com sftp.class "sftp\$FileInputStreamSleep.class" *.java readme.txt


java -jar sftp.jar [GET|PUT] user@remote_ip:remote_file local_file [-m] [-h] [-bBlockSize] [-sSleepTm] [-iID_rsa_file | -pPasswd] [-Pport]
java -jar sftp.jar [RM |LS ] user@remote_ip:remote_file [-m] [-iID_rsa_file | -pPasswd] [-Pport]
func
        GET | get     : get remote_file to local_file  (Success:0, Error:1)
        PUT | put     : put local_file  to remote_file (Success:0, Error:1)
        LS  | ls      : ls  remote_file (Success:0, Not Found:1)
        RM  | rm      : rm  remote_file (Success:0, Error:1)
option
        -iID_rsa_file :  rsa id file          (default ~/.ssh/id_rsa)
        -pPassword    :  user/PASSWORD        (only when using id/passwd)
        -Pport        :  port                 (default 22)
        -bBlockSize   :  BLOCKSIZE            (deafult 1024 byte)
        -sSleeptm     :  sleep_ms per blocksz (default 100 ms)
        -m            :  print message
        -h            :  process stat ('.' per block)
