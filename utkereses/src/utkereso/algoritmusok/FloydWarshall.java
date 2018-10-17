package utkereso.algoritmusok;
import utkereso.graf.*;

/**
 *
 * @author Takacs Peter
 */
public final class FloydWarshall{
    
    private final Graf graf; 
    private Graf.El e; 
    public double [][]D;
    public int [][]P; 
    public boolean kor = false;
    
    
    public FloydWarshall(Graf graf){
        this.graf = graf;  
        D = new double[graf.getCsucsokSzama()][graf.getCsucsokSzama()]; 
        P = new int[graf.getCsucsokSzama()][graf.getCsucsokSzama()];  
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
    
    public void kiirat()
    {
        System.out.println("\nD:");
        String str2 = "|\t";
        for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)
        {
            for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
            {         
                str2 += D[i][j] + "\t";
            }
            System.out.println(str2 + "|");
            str2 = "|\t";
        }
        
        System.out.println("\nP:");  
        String str = "|\t";
        for(int i=0;i<graf.getCsucsokSzama();i++){
            for(int j=0;j<graf.getCsucsokSzama();j++){
                str += P[i][j] + "\t";
            }

            System.out.println(str + "|");
            str = "|\t";
        }
        
    }
  
    public void eredmeny(){
        
        
        for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)  // Kezdeti D tömb feltöltése
        {
            for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
            {     
                e = graf.elek.get(i).get(j);
                if(e == null)
                {
                    D[i][j] = Double.POSITIVE_INFINITY;
                }
                if(i==j)
                {
                    D[i][j] = 0;
                }
                if(e != null)
                {
                    D[i][j] = (double) e.getSuly();
                }
            }
        }
        
        System.out.println("\nD(kezdetben):");    // Kezdeti D tömb kiiratása ellenőrzés céljából
        String str2 = "|\t";
        for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)
        {
            for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
            {         
                str2 += D[i][j] + "\t";
            }
            System.out.println(str2 + "|");
            str2 = "|\t";
        }
        
        for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)   // Kezdeti P tömb feltöltése
        {
            for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
            {
                P[i][j] = 0;
                if(D[i][j]!=Double.POSITIVE_INFINITY)
                {
                    if(i!=j)
                    {
                        P[i][j] = i+1;
                    }
                } 
            }
        } 
        
        System.out.println("\nP(kezdetben):");    // Kezdeti P tömb kiiratása ellenőrzés céljából
        String str = "|\t";
        for(int i=0;i<graf.getCsucsokSzama();i++){
            for(int j=0;j<graf.getCsucsokSzama();j++){
                str += P[i][j] + "\t";
            }
            System.out.println(str + "|");
            str = "|\t";
        }
        
        
        for(int k = 0 ; k < graf.getCsucsokSzama() ; k++)   // Az algoritmus
        {
            for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)
            {
                for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
                {
                    if  ((D[i][k] + D[k][j]) < D[i][j])
                    {
                        D[i][j] = D[i][k] + D[k][j];
                        P[i][j] = P[k][j];
                    }   
                }
            }
        }
        
        for(int k = 0 ; k < graf.getCsucsokSzama() ; k++)   // Vizsgálja, hogy létezik-e negatív összhosszúságú kör
        {
            for(int i = 0 ; i < graf.getCsucsokSzama() ; i++)
            {
                for(int j = 0 ; j < graf.getCsucsokSzama() ; j++)
                {
                    if  ((D[i][k] + D[k][j]) < D[i][j])
                    {
                        kor = true;
                    }   
                }
            }
        }
        //System.out.println(kor);
 
    }
    
}
