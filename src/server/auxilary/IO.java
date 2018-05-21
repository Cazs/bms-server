package server.auxilary;

import org.springframework.boot.SpringApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import server.AppConfig;
import server.Server;
import server.model.ApplicationObject;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.Arrays;

/**
 * Created by ghost on 2017/01/28.
 * @author ghost
 */
public class IO<T extends ApplicationObject>
{
    public static final String TAG_VERBOSE = "verbose";
    public static final String TAG_INFO = "info";
    public static final String TAG_WARN = "warning";
    public static final String TAG_ERROR = "error";
    private static final String TAG = "IO";
    private MongoOperations mongoOperations;
    private static IO io = new IO();

    private IO()
    {
        try
        {
            mongoOperations = new AppConfig().mongoOperations();
        } catch (UnknownHostException e)
        {
            IO.log("UnknownHost Error", IO.TAG_ERROR, e.getMessage());
        }
    }

    public static IO getInstance(){return io;}

    public MongoOperations mongoOperations(){return mongoOperations;}

    public static String generateRandomString(int len)
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String str="";
        for(int i=0;i<len;i++)
            str+=chars.charAt((int)(Math.floor(Math.random()*chars.length())));
        return str;
    }

    public static AbstractMap.SimpleEntry<Integer, LocalDateTime> isEpochSecondOrMilli(long epoch_date)
    {
        switch (String.valueOf(epoch_date).length())
        {
            case 10: // is using epoch seconds
                return new AbstractMap.SimpleEntry<>(0, LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch_date), ZoneId.systemDefault()));
            case 13: // is using epoch millis
                return new AbstractMap.SimpleEntry<>(1, LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch_date), ZoneId.systemDefault()));
            default:
                IO.log(IO.class.getName(), IO.TAG_ERROR, "unknown date format ["+epoch_date+"] - should be epoch millis or epoch seconds.");
                return new AbstractMap.SimpleEntry<>(3, LocalDateTime.of(1970, Month.JANUARY.getValue(), 1, 0, 0));
        }
    }

    public static String getYyyyMMddFormmattedDate(LocalDateTime date)
    {
        return date.getYear() +
                "-" + (date.getMonth().getValue() >= 10 ? date.getMonth().getValue() : "0" + String.valueOf((date.getMonth().getValue()))) +
                "-" + (date.getDayOfMonth() >= 10 ? date.getDayOfMonth() : "0" + String.valueOf(date.getDayOfMonth()));
    }

    public static String getEncryptedHexString(String message) throws Exception
    {
        StringBuilder str = new StringBuilder();
        for(byte b: hash(message))
            str.append(Integer.toHexString(0xFF & b));
        return str.toString();
    }

    //TODO: use blowfish/bcrypt
    public static byte[] hash(String plaintext) throws Exception
    {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        return m.digest();
    }

    //TODO: use blowfish/bcrypt
    public static byte[] encrypt(String digest, String message) throws Exception
    {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(digest.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);

        return cipherText;
    }

    //TODO: use blowfish/bcrypt
    public static String decrypt(String digest, byte[] message) throws Exception
    {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(digest.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;)
        {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public void quickSort(T arr[], int left, int right, String comparator)
    {
        int index = partition(arr, left, right, comparator);
        if (left < index - 1)
            quickSort(arr, left, index - 1, comparator);
        if (index < right)
            quickSort(arr, index, right, comparator);
    }

    public int partition(T arr[], int left, int right, String comparator) throws ClassCastException
    {
        int i = left, j = right;
        T tmp;
        double pivot = (Double) arr[(left + right) / 2].get(comparator);

        while (i <= j)
        {
            while ((Double) arr[i].get(comparator) < pivot)
                i++;
            while ((Double) arr[j].get(comparator) > pivot)
                j--;
            if (i <= j)
            {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void writeAttributeToConfig(String key, String value) throws IOException
    {
        //TO_Consider: add meta data for [key,value] to meta records.
        File f = new File("config.cfg");
        StringBuilder result = new StringBuilder();
        boolean rec_found=false;
        if(f.exists())
        {
            String s = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            int line_read_count=0;
            while ((s = in.readLine())!=null)
            {
                if(s.contains("="))
                {
                    String k = s.split("=")[0];
                    String val = s.split("=")[1];
                    //If the record exists, change it
                    if(k.equals(key))
                    {
                        val = value;//Update record value
                        rec_found=true;
                    }
                    result.append(k+"="+val+"\n");//Append existing record.
                    line_read_count++;
                } else IO.log(TAG, IO.TAG_ERROR, "Config file may be corrupted.");
            }
            if(!rec_found)//File exists but no key was found - write new line.
                result.append(key+"="+value+"\n");
            /*if(in!=null)
                in.close();*/
        } else result.append(key+"="+value+"\n");//File DNE - write new line.

        IO.log(TAG, IO.TAG_INFO, "writing attribute to config: " + key + "=" + value);

        /*if(!rec_found)//File exists but record doesn't exist - create new record
            result.append(key+"="+value+"\n");*/

        //Write to disk.
        PrintWriter out = new PrintWriter(f);
        out.print(result);
        out.flush();
        out.close();
    }

    public static void initEnv()
    {
        try
        {
            if(RemoteComms.host == null)
            {
                String ip = IO.readAttributeFromConfig("SERVER_IP");
                String port = IO.readAttributeFromConfig("SERVER_PORT");
                if (ip != null && port != null)
                {
                    RemoteComms.host = "http://" + ip + ":" + port;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting host to: " + RemoteComms.host);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "attributes SERVER_IP and/or SERVER_PORT are not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.DB_IP == null)
            {
                String db_ip = IO.readAttributeFromConfig("DB_IP");
                if (db_ip != null)
                {
                    RemoteComms.DB_IP = db_ip;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_IP to: " + db_ip);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "DB_IP attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.DB_PORT == 0)
            {
                String db_port = IO.readAttributeFromConfig("DB_PORT");
                if (db_port != null)
                {
                    RemoteComms.DB_PORT = Integer.parseInt(db_port);
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_PORT to: " + db_port);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "DB_PORT attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.DB_NAME == null)
            {
                String db_name = IO.readAttributeFromConfig("DB_NAME");

                if (db_name != null)
                {
                    RemoteComms.DB_NAME = db_name;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_NAME to: " + db_name);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "DB_NAME attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.TTL == 0)
            {
                String ttl = IO.readAttributeFromConfig("TTL");

                if (ttl != null)
                {
                    RemoteComms.TTL = Integer.parseInt(ttl);
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting TTL to: " + ttl);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "TTL attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.SYSTEM_EMAIL == null)
            {
                String system_email = IO.readAttributeFromConfig("SYSTEM_EMAIL");
                if (system_email != null)
                {
                    RemoteComms.SYSTEM_EMAIL = system_email;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting SYSTEM_EMAIL to: " + system_email);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "SYSTEM_EMAIL attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.MAILJET_API_KEY == null)
            {
                String mailjet_api_key = IO.readAttributeFromConfig("MAILJET_API_KEY");
                if (mailjet_api_key != null)
                {
                    RemoteComms.MAILJET_API_KEY = mailjet_api_key;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting MAILJET_API_KEY to: " + mailjet_api_key);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "MAILJET_API_KEY attribute is not set in the config file.");
                    return;
                }
            }

            if(RemoteComms.MAILJET_API_KEY_SECRET == null)
            {
                String mailjet_api_key_secret = IO.readAttributeFromConfig("MAILJET_API_KEY_SECRET");
                if (mailjet_api_key_secret != null)
                {
                    RemoteComms.MAILJET_API_KEY_SECRET = mailjet_api_key_secret;
                    IO.log(Server.class.getName(), IO.TAG_INFO, "setting MAILJET_API_KEY_SECRET to: " + mailjet_api_key_secret);
                }
                else
                {
                    IO.log(Server.class.getName(), IO.TAG_WARN, "MAILJET_API_KEY_SECRET attribute is not set in the config file.");
                    return;
                }
            }
        } catch (IOException e)
        {
            IO.log(Server.class.getName(), IO.TAG_ERROR, e.getMessage());
        }
    }
    public static String readAttributeFromConfig(String key) throws IOException
    {
        File f = new File("config.cfg");

        if(f.exists())
        {
            String s = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            while ((s = in.readLine())!=null)
            {
                if(s.contains("="))
                {
                    String var = s.split("=")[0];
                    String val = s.split("=")[1];
                    if(var.equals(key))
                    {
                        /*if(in!=null)
                            in.close();*/
                        return val;
                    }
                }
            }
        }
        return null;
    }

    public static String readStream(InputStream stream) throws IOException
    {
        //Get message from input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        if(in!=null)
        {
            StringBuilder msg = new StringBuilder();
            String line;
            while ((line = in.readLine())!=null)
            {
                msg.append(line + "\n");
            }
            in.close();

            return msg.toString();
        }else IO.log(TAG, IO.TAG_ERROR, "could not read error stream from server response.");
        return null;
    }

    public static void log(String src, String tag, String msg)
    {
        switch (tag.toLowerCase())
        {
            case TAG_VERBOSE:
                if (Globals.DEBUG_VERBOSE.getValue().toLowerCase().equals("on"))
                    System.out.println(String.format("%s> %s: %s", src, tag, msg));
                break;
            case TAG_INFO:
                if (Globals.DEBUG_INFO.getValue().toLowerCase().equals("on"))
                    System.out.println(String.format("%s> %s: %s", src, tag, msg));
                break;
            case TAG_WARN:
                if (Globals.DEBUG_WARNINGS.getValue().toLowerCase().equals("on"))
                    System.out.println(String.format("%s> %s: %s", src, tag, msg));
                break;
            case TAG_ERROR:
                if (Globals.DEBUG_ERRORS.getValue().toLowerCase().equals("on"))
                    System.err.println(String.format("%s> %s: %s", src, tag, msg));
                break;
            default://fallback for custom tags
                System.out.println(String.format("%s> %s: %s", src, tag, msg));
                break;
        }
    }
}
