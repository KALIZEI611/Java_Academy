package Test;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main123 {
    public static void main(String[] args) {
        /*
        List<Fish> fishes = new ArrayList<>();
        fishes.add(new Fish("eel",1.5,120));
        fishes.add(new Fish("salmon",2.5,180));
        fishes.add(new Fish("carp",3.5,80));
        fishes.add(new Fish("tuna",4.2,320));
        fishes.add(new Fish("trout",2.8,150));
        System.out.println("Before Sorting:");


        //fishes.sort((Fish f1, Fish f2) -> (int) f1.getWeight() * 100 - (int) f2.getWeight() * 100);
        fishes.sort((Fish f1, Fish f2) -> (int) f1.getPrice() * 100 - (int) f2.getPrice() * 100);
        System.out.println("Fishes more expencive than 200:");
        getByPredicate(fishes, (f) -> f.getPrice() > 200 );
        System.out.println("After Sorting:");
        fishes.forEach(System.out::println);
        Stream.of("Argentina", "Bulgaria",
                        "Canada","Denmark","Ukraine","USA")
                .filter((c)->c.startsWith("U"))
                .forEach(System.out::println);
*/

        FileOutputStream fout = null;

        ObjectOutputStream oout=null;
        try {
            fout = new FileOutputStream("test.txt");
            Fish f = new Fish("salmon",2.5,180);
            oout = new ObjectOutputStream(fout);
            oout.writeObject(f);
            //Fish f2= (Fish)(new ObjectInputStream(new FileInputStream("test.txt"))).readObject();
            //System.out.println(f2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("main").
                    log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger("main").
                    log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                oout.close();
            } catch (IOException ex) {
                Logger.getLogger("main").
                        log(Level.SEVERE, null, ex);
            }
        }


    }
}
