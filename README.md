# sftp
slow sftp commands with speed control,
sftp with communication load generation control function for slow network shared network

This sftp is an application using jcraft.
The directory under com is the unpacked file to create the jar file.

see jcraft
http://www.jcraft.com/jsch/

# Usage 
java -jar sftp.jar [GET|PUT] user@remote_ip:remote_file local_file [-m] [-bBlockSize] [-sSleepTm] [-iID_rsa_file | -pPasswd] [-Pport]
java -jar sftp.jar [RM |LS ] user@remote_ip:remote_file [-m] [-iID_rsa_file | -pPasswd] [-Pport]
func
    GET | get     : get remote_file to local_file  (Success:0, Error:255)
    PUT | put     : put local_file  to remote_file (Success:0, Error:255)
    LS  | ls      : ls  remote_file (Success:0, Not Found:255)
    RM  | rm      : rm  remote_file (Success:0, Error:255)
option
    -iID_rsa_file :  rsa id file          (default ~/.ssh/id_rsa)
    -pPassword    :  user/PASSWORD        (only when using id/passwd)
    -Pport        :  port                 (default 22)
    -bBlockSize   :  BLOCKSIZE            (deafult 1024 byte)
    -sSleeptm     :  sleep_ms per blocksz (default 100 ms)
    -m            :  print message

# Description Arguments must be in order
  O) java -jar sftp.jar get user@remote_ip:remote_file -m 
  X) java -jar sftp.jar -m get user@remote_ip:remote_file

# Note: Do not use spaces for option values.
  O) -Pxxxxx
  X) -P xxxxx

