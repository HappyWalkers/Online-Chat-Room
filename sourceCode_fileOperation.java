package server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JOptionPane;
public class fileOperation {
    private static Properties pps;
    static {
        pps = new Properties();
        FileReader reader = null;
        try {
            File file = new File(ConstVarible.dataPath + "account.inc");
            if (file.exists() == false) {
                file.createNewFile();
            }
            reader = new FileReader(file);
            pps.load(reader);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "文件操作异常1");
            System.exit(0);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void register(String nickname, String password) {
        pps.setProperty(nickname, password);
        PrintStream ps = null;
        try {
            ps = new PrintStream(ConstVarible.dataPath + "account.inc");
            pps.list(ps);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "文件操作异常2");
            System.exit(0);
        } finally {
            try {
                ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static String getPasswordByAccount(String account) {
        String password = pps.getProperty(account);
        return password;
    }
    public static boolean check(String str) {
        if (pps.getProperty(str) == null) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean checkNoteName(String str) {
        File file = new File(ConstVarible.dataPath + str);
        if (file.exists() == true) {
            return true;
        } else {
            return false;
        }
    }
    public static ArrayList<String> getFileNameByAccount(String account) {
        File file = new File(ConstVarible.dataFile);
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (File f : files) {
            String filename = f.getAbsolutePath();
            if (filename.startsWith(ConstVarible.dataPath + account + "_")) {
                arrayList.add(filename);
            }
        }
        return arrayList;
    }
    public static int countWordInFile(String word, File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String msg = new String(data);
        String aft = msg.replace(word, "");
        return (msg.length() - aft.length()) / word.length();
    }
    public static Vector<Vector<String>> getfriend(String account) {
        File file = new File(ConstVarible.dataPath+account);
        if(file.exists()==false)return null;
        Vector<Vector<String>> data = new Vector<>();
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while(true){
                String string=bufferedReader.readLine();
                if(string==null)break;
                Vector<String> row=new Vector<>();
                row.add(string);
                data.add(row);
            }
            bufferedReader.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
    public static int storeFriendsAccount(String account, String friendAccount) {
        if (check(friendAccount) == false) {
            return 0;
        } else {
            File file = new File(ConstVarible.dataPath + account);
            try {
                if (file.exists() == false) {
                    file.createNewFile();
                }
                if(countWordInFile(friendAccount, file)==1){
                    return 1;
                }
                FileWriter fw=new FileWriter(file,true);
                fw.append(friendAccount+"\n");
                fw.close();
                storeFriendsAccount(friendAccount, account);
                return 2;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 3;
        }
    }
}