package Test;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

public class Fish implements Serializable
{
    private String name;
    private double weight;
    private double price;

    public Fish(String name, double weight, double price)
    {
        this.name=name;
        this.weight=weight;
        this.price=price;
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Fish))
        {
            return false;
        }
        Fish tmp = (Fish) o;
        return (tmp.name.equals(this.name) &&
                tmp.weight == this.weight &&
                tmp.price == (this.price));
    }

    public int hashCode()
    {
        int code = 17;
        code = 31 * code + this.name.hashCode();
        code = 31 * code + (int)this.weight;
        code = 31 * code + (int)this.price;
        return code;
    }

    public double getWeight()
    {
        return this.weight;
    }

    public double getPrice() {
        return price;
    }



    @Override
    public String toString()
    {
        return this.name+" weight:"+this.weight+" price:"+this.price;
    }
    public static void getByPredicate(List<Fish> fishes,
                                      Predicate<Fish> p) {
        for (Fish f : fishes) {
            if (p.test(f)) {
                System.out.println(f);
            }
        }
    }
}
