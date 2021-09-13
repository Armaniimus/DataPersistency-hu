import java.util.Date;
import java.util.HashMap;

public class HashMapTest {
    public static void main(String[] args) {
        HashMap hash = new HashMap();
        System.out.println( hash.isEmpty() );
        System.out.println( hash.get("5") );

        System.out.println( hash.put(5, "5") );
        System.out.println( hash.get(5) );

        System.out.println( hash.put("5", 90) );
        System.out.println( hash.get("5") );

        Date date = new Date();
        System.out.println( hash.put("date", date) );
        System.out.println( hash.get("date") );
    }
}
