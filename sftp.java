import com.jcraft.jsch.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class sftp {
    public static boolean    is_msg = false;
    public static boolean    is_hash = false;
    public static int BLOCKSZ = 1024;
    public static String id_rsa = "~/.ssh/id_rsa";
    public static String passwd = null;
    public static int sleep_tm = 100;
    public static int port = 22;

    public static String func;
    public static String remote_ip;
    public static String userid;
    public static String remote_file;
    public static String local_file;
    public static String separator = System.getProperty("file.separator");

    public static int total_tran_len = 0;
    public static void usage_and_exit() {
        System.err.println("java -jar sftp.jar [GET|PUT] user@remote_ip:remote_file local_file [-m] [-h] [-bBlockSize] [-sSleepTm] [-iID_rsa_file | -pPasswd] [-Pport] ");
        System.err.println("java -jar sftp.jar [RM |LS ] user@remote_ip:remote_file [-m] [-iID_rsa_file | -pPasswd] [-Pport] ");
        System.err.println("func");
        System.err.println("    GET | get     : get remote_file to local_file  (Success:0, Error:1)");
        System.err.println("    PUT | put     : put local_file  to remote_file (Success:0, Error:1)");
        System.err.println("    LS  | ls      : ls  remote_file (Success:0, Not Found:1)");
        System.err.println("    RM  | rm      : rm  remote_file (Success:0, Error:1)");
        System.err.println("option");
        System.err.println("    -iID_rsa_file :  rsa id file          (default ~/.ssh/id_rsa)");
        System.err.println("    -pPassword    :  user/PASSWORD        (only when using id/passwd)");
        System.err.println("    -Pport        :  port                 (default 22)");
        System.err.println("    -bBlockSize   :  BLOCKSIZE            (deafult 1024 byte)");
        System.err.println("    -sSleeptm     :  sleep_ms per blocksz (default 100 ms)");
        System.err.println("    -m            :  print message ");
        System.err.println("    -h            :  process stat ('.' per block) ");
        System.err.println("");
        System.exit(1);
    }
    public static void main(String args[]) throws Exception 
    {
        byte buf[];
        if (args.length < 2) 
            usage_and_exit();

        int p = 0;
        func = args[p++];
        

        String remote_str   = args[p++];

        String remote_arr[] = remote_str.split("@|:");
        if (remote_arr.length != 3 || remote_str.startsWith("-")) {
            System.err.println("remove file format error : user_id@ip:file_path\n");
            System.err.println(remote_str);
            System.exit(1);
        }
        userid      = remote_arr[0];
         remote_ip   = remote_arr[1];
         remote_file = remote_arr[2];

        if (!  ("GET".equals(func) || "get".equals(func) || "PUT".equals(func) || "put".equals(func) || "LS".equals(func) || "ls".equals(func) || "RM".equals(func) || "rm".equals(func))) {
            usage_and_exit();
        }
        if ("GET".equals(func) || "get".equals(func) || "PUT".equals(func) || "put".equals(func)) {
            if (args.length < 3 || args[p].startsWith("-")) {
                System.err.println("get/put     local_file not found\n");
                System.exit(1);
            }
            local_file = args[p++];
        }

        try {
            while (p < args.length) {
                if (args[p].length() == 2 && args[p].startsWith("-m"))  {
                    is_msg = true;

                } else if (args[p].length() == 2 && args[p].startsWith("-h"))  {
                    is_hash = true;

                } else if (args[p].length() > 2 && args[p].startsWith("-i"))  {
                    id_rsa   = args[p].substring(2);

                } else if (args[p].length() > 2 && args[p].startsWith("-p"))  {
                    passwd   = args[p].substring(2);

                } else if (args[p].length() > 2 && args[p].startsWith("-s"))  {
                    sleep_tm = Integer.parseInt(args[p].substring(2));


                } else if (args[p].length() > 2 && args[p].startsWith("-b"))  {
                    BLOCKSZ = Integer.parseInt(args[p].substring(2));

                } else if (args[p].length() > 2 && args[p].startsWith("-P"))  {
                    port = Integer.parseInt(args[p].substring(2));

                } else {
                    System.err.println("argument error [" + args[p] + "]");
                    usage_and_exit();
                }
                p++; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("");
            usage_and_exit();
        }
        buf = new byte[BLOCKSZ];


        JSch jsch = new JSch();
        Session sess = null;
        if (passwd != null) {
            if (is_msg) 
                System.out.println("speed:" + sleep_tm + " ms per " + BLOCKSZ + " byte " + "ID/PW:" + userid + "/" + passwd );
            sess = jsch.getSession(userid, remote_ip, port);       
            sess.setPassword(passwd);
        } else {
            if (is_msg) 
                System.out.println("speed:" + sleep_tm + " ms per " + BLOCKSZ + " byte " + "rsa_file:" + id_rsa );
            try {
                jsch.addIdentity(id_rsa);
            } catch (Exception e) {
                System.out.println("id_rsa file error : [" + id_rsa + "]");
                usage_and_exit();
            }
            sess = jsch.getSession(userid, remote_ip, port);       
            sess.setConfig("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
        }
        if (is_msg) 
            System.out.println(func + " remote:" + remote_ip + ",  user:" + userid + " port=" + port);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        sess.setConfig(config);
        sess.setTimeout(10000);
        sess.connect();
        ChannelSftp ch = (ChannelSftp) sess.openChannel("sftp");   
        ch.setEnv("SSH_MSG_CHANNEL_DATA", ""+ BLOCKSZ);
        ch.setEnv("SSH_MSG_CHANNEL_WINDOW_ADJUST", "" + BLOCKSZ);
        try {
            ch.connect();
            if ("ls".equals(func) || "LS".equals(func)) {
                if (is_msg) System.out.println("LS [" + remote_file + "]");
                Vector flist = ch.ls(remote_file);
                for (int i = 0; i < flist.size(); i++) 
                    System.out.println(flist.get(i).toString());
                System.out.println("total " + flist.size());

            } else if ("rm".equals(func) || "RM".equals(func)) {
                if (is_msg) System.out.println("RM [" + remote_file + "]");
                ch.rm(remote_file);

            } else if ("put".equals(func) || "PUT".equals(func)) {
                FileInputStream in = null;
                OutputStream out = ch.put(remote_file, ChannelSftp.OVERWRITE);
                if (out != null) {
                    in = new FileInputStream(local_file);
                    int nbuf;
                    do {
                        nbuf = in.read(buf, 0, buf.length);
                        if (nbuf > 0) {
                            if (is_hash) System.err.print(".");
                            out.write(buf, 0, nbuf);
                            total_tran_len += nbuf;
                        }
                        out.flush();
                        Thread.sleep(sleep_tm);
                    } while (nbuf >= 0);
                }
                if (is_hash) System.out.println("");
                if (is_msg)  System.out.println("total " + total_tran_len + " byte");
                if (is_msg)  System.out.println("quit");
                try{ in.close(); } catch  (Exception e) {}
                try{ out.close(); } catch  (Exception e) {}


            } else if ("get".equals(func) || "GET".equals(func)) {
                InputStream in = ch.get(remote_file);
                File fp = new File(local_file);
                if (fp.isDirectory())
                    local_file = local_file + separator + getFileName(remote_file);
                if (is_msg) System.out.println("GET [" + remote_file + "] to [" +  local_file + "]");
                
                FileOutputStream out = new FileOutputStream(new File(local_file));
                int len = 0;
                while (0 <= len) {
                    len = in.read(buf);
                    if (0 < len) {
                        if (is_hash) System.err.print(".");
                        out.write(buf, 0, len);
                        total_tran_len += len;
                    }
                    Thread.sleep(sleep_tm);
                }
                if (is_hash) System.out.println("");
                if (is_msg)  System.out.println("total " + total_tran_len + " byte");
                if (is_msg)  System.out.println("quit");
                try{ in.close(); } catch  (Exception e) {}
                try{ out.close(); } catch  (Exception e) {}
            }
            if (is_msg) System.err.print("\n");
        } catch  (Exception e) {
            System.err.println("ERROR:");
            System.out.println(e.getMessage());
            System.err.println("");
            System.exit(1);
        } finally {
            try {sess.disconnect(); } catch  (Exception e) {}
            try {ch.quit();         } catch  (Exception e) {}
            try {ch.disconnect();   } catch  (Exception e) {}
        }
        System.exit(0);
    }

    public static String getFileName(String fname) {
        int last_p = fname.lastIndexOf(separator);
        if (last_p != -1)
            return fname.substring(last_p + 1);
        return fname;
    }

    public static class FileInputStreamSleep extends FileInputStream {
        public int    sleep_tm;
        public int    blocksz;

        public FileInputStreamSleep(File fp, int sleep_tm, int blocksz) throws FileNotFoundException  {
            super(fp);
            this.sleep_tm = sleep_tm;
            this.blocksz = blocksz;
        }
        public int read() throws IOException {
            int ch = super.read();
            checkSleep(1);
            return ch;
        }
        public int read(byte b[], int off, int len) throws IOException {
            if (len > blocksz)
                len = blocksz;
            int rlen = super.read(b, off, len);
            checkSleep(rlen);
            total_tran_len += rlen;
            return rlen;
        }
        public int read(byte b[]) throws IOException {
            int rlen = super.read(b);
            checkSleep(rlen);
            total_tran_len += rlen;
            return rlen;
        }
        public int r_cnt = 0;
        public void checkSleep(int len) {
            r_cnt += len;
            if (r_cnt > blocksz) {
                try {Thread.sleep(sleep_tm); } catch  (Exception e) {}
                r_cnt = 0;
                if (is_msg) System.err.print(".");
            }
        }
    }
}
