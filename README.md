sftp : slow sftp commands with speed control for slow shared network 
# sftp
 slow sftp commands with speed control for slow shared network k<br>
<br>
This sftp is an application using jcraft.<br>
The directory under com is the unpacked file to create the jar file.<br>
<br>
see jcraft<br>
http://www.jcraft.com/jsch/<br>
<br>
# Usage <br>
java -jar sftp.jar [GET|PUT] user@remote_ip:remote_file local_file [-m] [-bBlockSize] [-sSleepTm] [-iID_rsa_file | -pPasswd] [-Pport]<br>
java -jar sftp.jar [RM |LS ] user@remote_ip:remote_file [-m] [-iID_rsa_file | -pPasswd] [-Pport]<br>
func<br>
    GET | get     : get remote_file to local_file  (Success:0, Error:255)<br>
    PUT | put     : put local_file  to remote_file (Success:0, Error:255)<br>
    LS  | ls      : ls  remote_file (Success:0, Not Found:255)<br>
    RM  | rm      : rm  remote_file (Success:0, Error:255)<br>
option<br>
    -iID_rsa_file :  rsa id file          (default ~/.ssh/id_rsa)<br>
    -pPassword    :  user/PASSWORD        (only when using id/passwd)<br>
    -Pport        :  port                 (default 22)<br>
    -bBlockSize   :  BLOCKSIZE            (deafult 1024 byte)<br>
    -sSleeptm     :  sleep_ms per blocksz (default 100 ms)<br>
    -m            :  print message<br>
<br>
# Note: <br>
 Description Arguments must be in order<br>
  O) java -jar sftp.jar get user@remote_ip:remote_file -m <br>
  X) java -jar sftp.jar -m get user@remote_ip:remote_file<br>
<br>
 Do not use spaces for option values.<br>
  O) -Pxxxxx<br>
  X) -P xxxxx<br>

