package utkereso.algoritmusok;
import utkereso.graf.*;

/**
 *
 * @author Takacs Peter
 */
public class Dijkstra{
    
    private java.util.ArrayList<Graf.Csucs> KESZ = 
            new java.util.ArrayList<Graf.Csucs>();           
    private java.util.ArrayList<Graf.Csucs> VperKESZ = 
            new java.util.ArrayList<Graf.Csucs>();  // (V\KESZ)  A csúcsok halmaza a KESZ csúcsok eltávolítása után.
    private double[] D;   // A csúcsokkal indexelt tömb. A forrás és csúcs közötti távolság. 
    public int P[];       // A csúcsok legrövidebb útbeli megelőzőjét tárolja. 
    private final Graf graf;
    private Graf.Csucs s, x;  
    private Graf.El e, el; 
    private boolean volt_negativ = false;   
    

    public Dijkstra(Graf graf){
        this.graf = graf;  
        D = new double[graf.getCsucsokSzama()]; 
        P = new int[graf.getCsucsokSzama()];    
        for(Graf.Csucs v: graf.csucsok)
        {
            VperKESZ.add(v);
        }
    }
    
    public boolean isNegativSuly()
    {
        return volt_negativ;
    }
      
    public void alap()
    {
        for(Graf.Csucs cs: graf.csucsok)
        {
            cs.szinez(0, 6);
            cs.felirat("");
        }
        for (int u = 0 ; u < graf.getCsucsokSzama() ; u++) 
        {    
            for (int v = 0 ; v < graf.getCsucsokSzama() ; v++) 
            {
                if(graf.elek.get(u).get(v) != null)
                {
                    graf.elek.get(u).get(v).szinez(0);
                    graf.elek.get(u).get(v).nyilSzinez(0);
                }
            }
        }
    }
    
    public void szinez()
    {
        for(int i = 0 ; i < P.length ; i++)
        {
            if(P[i] != -1)
            {
                e = graf.elek.get(P[i]-1).get(i);
                el = graf.elek.get(i).get(P[i]-1);
                if (el != null)
                    el.szinez(2);
                e.szinez(2);
                e.nyilSzinez(2);
            }
        }
    }
    
    public void kiirat()
    {
        int szamol =0;
        System.out.println("KESZ:");
        for(Graf.Csucs cs: KESZ)
        {
            System.out.println(KESZ.get(szamol));
            szamol++;
        }
        
        System.out.println("VperKESZ:");
        szamol =0;
        for(Graf.Csucs cs: VperKESZ)
        {
            System.out.println(VperKESZ.get(szamol));
            szamol++;
        }
        
        System.out.println("D:");
        for(int i = 0 ; i < D.length ; i++){
            System.out.println(i+1+": "+D[i]);  
        }

        System.out.println("Szomszédsági:");
        Graf.El el;
        for (int i = 0 ; i < graf.getCsucsokSzama() ; i++) 
        {    
            for (int j = 0 ; j < graf.getCsucsokSzama() ; j++) 
            {
                el = graf.getEl(i, j);
                System.out.println(el);
            }
        }

        System.out.println("\nP:");  
        for (int i = 0 ; i < P.length ; i++) 
        {        
            System.out.println((i+1)+".csúcs megelőzője: "+P[i]); 
        }
    }
    
    public void eredmeny(){
        int suly = 0;
        double min, a, b;
      
        if(graf.csucsok.isEmpty() != true)
        {
            s = graf.csucsKereses(0);
            KESZ.add(s);
            VperKESZ.set(0, null);
            s.szinez(1,1);
            //System.out.println(s.getSorszam());
            
            for(Graf.Csucs v: graf.csucsok)
            {
                e = graf.elek.get(s.index).get(v.index);
                if (e != null)
                {
                    suly = Math.abs(e.getSuly());
                    if(e.getSuly()<0)                               // Eldönti, hogy vizsgált-e negatív súlyú élt
                        volt_negativ = true;
                    D[v.index] = suly;
                    P[v.index] = s.getSorszam();   
                    v.szinez(1,1);                                  // Csúcs beszínezése
                    v.felirat("("+Math.round(suly)+")");            // Csúcsra útköltség feliratozása  
                    //System.out.println(v.getSorszam());
                }
                else
                {
                    D[v.index] = Double.POSITIVE_INFINITY;
                    if(v.index!=0)
                    {
                        v.felirat("(-)");
                    }
                    else
                    {
                        s.felirat("(0)");
                    }
                }
            }
            //-------------------------------------------------------------------------   
          
            for(int i = 1 ; i < graf.getCsucsokSzama() ; i++)
            {
                //Válasszunk olyan x eleme V\KÉSZ csúcsot, melyre D[x] minimális.
                //Tegyük x-et a KÉSZ-be.           
                min = 100000000;
                for(Graf.Csucs cs: VperKESZ)
                {      
                    if(cs != null)
                    {
                        if(min > D[cs.index])
                        {
                            min = D[cs.index];
                            x = cs;
                        }
                    }
                }
                KESZ.add(x);
                VperKESZ.set(x.index, null);
                x.szinez(1,1);
                //System.out.println("i:"+i+" (x) "+x.getSorszam());
                //------------------------------------------------------------------------- 
                
                for(Graf.Csucs w: VperKESZ)
                {
                    //D[w] := min{D[w]; D[x]+C[x,w]}
                    
                    if (w != null)
                    {
                        el = graf.elek.get(x.index).get(w.index);
                        if(el != null)
                        {
                            a = D[w.index];
                            b = D[x.index] + Math.abs(el.getSuly());
                            if(el.getSuly()<0)
                                volt_negativ = true;
                            //System.out.println ("a="+a);
                            //System.out.println ("b="+b);
                            if (a < b)
                            {    
                                D[w.index] = a;
                                w.felirat("("+Math.round(a)+")");
                                //System.out.println("i:"+i+" (a) "+w.getSorszam());
                            }
                            else
                            {
                                D[w.index] = b;
                                P[w.index] = x.getSorszam();    
                                w.felirat("("+Math.round(b)+")");
                                //System.out.println("i:"+i+" (b) "+w.getSorszam());
                            } 
                        } 
                    }
                }  
            }       
        }
        
        for(int i = 0 ; i < P.length ; i++){  // Amelyik csúcsnak nincs megelőzője, azt (-1)-el jelölöm. 
            if(P[i] == 0)
                P[i] = -1;
        }
        
        szinez();
   
    }
    
}
