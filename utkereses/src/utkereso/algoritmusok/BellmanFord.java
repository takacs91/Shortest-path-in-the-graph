package utkereso.algoritmusok;
import utkereso.graf.*;

/**
 *
 * @author Takacs Peter
 */
public final class BellmanFord{
    
    private final Graf graf;
    private Graf.Csucs s, cs;
    private Graf.El e, el;
    public double[] D;    // A csúcsokkal indexelt tömb. A forrás és csúcs közötti távolság. 
    public int P[];       // A csúcsok legrövidebb útbeli megelőzőjét tárolja. 
    public boolean kor = false;
   
    
    public BellmanFord(Graf graf){
        this.graf = graf;  
        D = new double[graf.getCsucsokSzama()]; 
        P = new int[graf.getCsucsokSzama()];  
    }
    
    public boolean volt_kor()
    {
        return kor;
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
        System.out.println("\nD:");
        for(int i = 0 ; i < D.length ; i++)
        {
            System.out.println(i+1+": "+D[i]);
        }
        
        System.out.println("\nP:");  
        for (int i = 0 ; i < P.length ; i++) 
        {        
            System.out.println((i+1)+".csúcs megelőzője: "+P[i]); 
        }
    }
    
    public void eredmeny(){
        
        if(graf.csucsok.isEmpty() != true)
        {  
            for(Graf.Csucs v: graf.csucsok)
            {
                D[v.index] = Double.POSITIVE_INFINITY;
                P[v.index] = -1;
            }
            s = graf.csucsKereses(0);
            D[s.index] = 0;
            P[s.index] = -1; 
            s.szinez(1, 1);
            s.felirat("("+Math.round(D[s.index])+")");
            //----------------------------------------------------------------------
        
            for(int i = 1 ; i < graf.getCsucsokSzama(); i++)
            {
                for (int u = 0 ; u < graf.getCsucsokSzama() ; u++) 
                {    
                    for (int v = 0 ; v < graf.getCsucsokSzama() ; v++) 
                    {
                        if(graf.elek.get(u).get(v) != null)
                        {
                            if(D[v]>(D[u]+graf.elek.get(u).get(v).getSuly()))
                            {
                                D[v]=D[u]+graf.elek.get(u).get(v).getSuly();
                                cs = graf.csucsKereses(u);
                                P[v]=cs.getSorszam();
                                graf.csucsok.get(v).felirat("("+Math.round(D[v])+")");
                                graf.csucsok.get(v).szinez(1, 1);
                            }
                        }
                    }
                }
            }
            //----------------------------------------------------------------------
            
            for (int u = 0 ; u < graf.getCsucsokSzama() ; u++) 
            {    
                for (int v = 0 ; v < graf.getCsucsokSzama() ; v++) 
                {
                    if(graf.elek.get(u).get(v) != null)
                    {
                        if(D[v]>(D[u]+graf.elek.get(u).get(v).getSuly()))
                        {
                            kor = true;
                        }
                    }
                }
            }
            //System.out.println("Negatív kör: "+kor);
        }
        
        szinez();
    }
    
}
