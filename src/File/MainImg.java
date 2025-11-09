package File;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainImg {
    public static void main(String[] args) {
        /*
        InputStream in=null;
        OutputStream out=null;
        byte[] buffer=null;
        try {
            in = new FileInputStream(new File("test.txt"));
            buffer = new byte[in.available()];
            in.read(buffer);
            File file = new File("outputFile.tmp");
            out = new FileOutputStream(file);
            out.write(buffer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("main").
                    log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger("main").
                    log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger("main").
                        log(Level.SEVERE, null, ex);
            }
        }
        */
        InputStream in=null;
        OutputStream out=null;
        byte[] buffer=new byte[8*1024];
        try {
            in = new FileInputStream(new File("test.txt"));
            File file = new File("test2.txt");
            out = new FileOutputStream(file);
            int bytesRead=0;
            long len = file.length();
            System.out.println(len);
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger("main").
            log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger("main").
                    log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger("main").
                        log(Level.SEVERE, null, ex);
            }
        }
    }
}
