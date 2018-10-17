package utkereso.graf;

import java.util.Random; 

/**
 *
 * @author Takacs Peter
 */

public class Graf {
     
    private boolean negSuly = false;
    public final java.util.ArrayList<Csucs> csucsok = 
            new java.util.ArrayList<Csucs>();                    // Csúcsokat tároló tömb
    public final java.util.ArrayList<java.util.ArrayList<El>> elek = 
            new java.util.ArrayList<java.util.ArrayList<El>>();  // Éleket tároló mátrix
    private int csucsokSzama = 0;  

    
    public java.util.ArrayList tombLetrehoz(int ujMeret, java.util.ArrayList elem)
    {
        return new java.util.ArrayList(java.util.Collections.nCopies(ujMeret, elem));
    }
   
    public Csucs csucsLetrehoz(int sorszam, int index, int x, int y)
    {
        Csucs cs = new Csucs(sorszam, index);
        ++csucsokSzama;
        csucsok.add(index, cs);
        for(int i = index+1 ; i < csucsokSzama ; ++i)
        {
            csucsok.get(i).index = i;
        }     
        cs.setKoordinatak(x, y);
        return cs;
    }
       
    public Csucs csucsKereses(int x, int y)
    {
        for(int i = csucsokSzama-1 ; i >= 0 ; --i)
        {
            Csucs cs = csucsok.get(i);
            int dx = cs.x - x;
            int dy = cs.y - y;
            if(Math.sqrt(dx*dx + dy*dy) <= GrafElemRajzolo.SUGAR)
            {
                return cs;
            }
        }
        return null;
    }
    
    public Csucs csucsKereses(int index)
    {
         return csucsok.get(index);
    }
     
    public int getCsucsokSzama()
    {
        return csucsok.size();
    }
    
    public int elSzam (int x1, int y1, int x2, int y2)
    {
        Csucs cs1 = csucsKereses(x1, y1);
        Csucs cs2 = csucsKereses(x2, y2);
        El el1 = getEl(cs1, cs2);
        El el2 = getEl(cs2, cs1);
        if ((el1 == null) && (el2 == null))
        {
            return 0;
        }
        if ((el1 != null) && (el2 == null))
        {
            return 1;
        }
        if ((el1 == null) && (el2 != null))
        {
            return 1;
        }
        if ((el1 != null) && (el2 != null))
        {
            return 2;
        }
        return 0;
    }
    
    public boolean letezikEl (Csucs cs1, Csucs cs2)
    {
        El el1 = getEl(cs1, cs2);
        El el2 = getEl(cs2, cs1);
        if ((el1 == null) && (el2 == null))
        {
            return false;
        }
        return true;
    }
    
    public Csucs csucsHozzaad(int x, int y)
    {
        int sorszam = 0;
        if (csucsokSzama==0)
        {
            sorszam = 1;
        }
        else
        {
            sorszam = csucsok.get(csucsokSzama-1).sorszam + 1;
        }
        for(int i = 0 ; i < csucsokSzama ; ++i)
        {
            elek.get(i).add(null);
        }
        elek.add(tombLetrehoz(csucsokSzama+1, null));
        Csucs cs = csucsLetrehoz(sorszam, csucsokSzama, x, y);
        return cs;      
    }
    
    public El elLetrehoz(Csucs u, Csucs v, int suly)
    {
        El el = new El(u, v, suly);
        elek.get(u.index).set(v.index, el);
        return el;
    }
    
    public El elHozzaad(Csucs u, Csucs v)
    {
        if(u.sorszam!=v.sorszam)
        {
            El e = getEl(u, v);
            Random rnd = new Random(); 
            
            El e2 = getEl(v, u);
            if (e2 != null)
            {
                e = elLetrehoz(u, v, e2.getSuly());
            }
            else
            {         
                if (negSuly == true)
                {  
                    int r = rnd.nextInt(99) - 50;
                    //int r = rnd.nextInt(149) - 50;
                    //int r = rnd.nextInt(20) - 50;
                    while (r == 0)
                    {
                        r = rnd.nextInt(149) - 50;
                    }
                    e = elLetrehoz(u, v, r);
                }
                else
                {      
                    e = elLetrehoz(u, v, rnd.nextInt(99) + 1);
                }   
                return e;     
            }       
        }
        return null;
    }          
            
     
    public El getEl(int i, int j)
    {
        return elek.get(i).get(j);
    }
                 
    public El getEl(Csucs u, Csucs v)
    {
        return elek.get(u.index).get(v.index);
    }
    
    
    public GrafElemRajzolo grafRajzolas(java.awt.Graphics2D rajzlap)
    { 
        GrafElemRajzolo rajzolo = new GrafElemRajzolo(rajzlap);
        for(int i = 0 ; i < csucsokSzama ; ++i)
        {
            Csucs u = csucsok.get(i);
            for(int j = i+1 ; j < csucsokSzama ; ++j)
            {
                Csucs v = csucsok.get(j);
                El e1 = getEl(u, v);                
                if(e1 != null)
                {
                    e1.elRajzolas(rajzolo, u.x, u.y, v.x, v.y);              
                }
                El e2 = getEl(v, u);       
                if(e2 != null)
                {
                    e2.elRajzolas(rajzolo, u.x, u.y, v.x, v.y);  
                }
            }
        }
       
        for(Csucs cs: csucsok)
        {   
            cs.csucsRajzolas(rajzolo, cs.x, cs.y); 
        }
        return rajzolo;
    }

    public void setNegSuly()
    {
        if (negSuly == true)
        {
            negSuly = false;
        }
        else
        {
            negSuly = true;
        }
    }
    
    public class Csucs 
    {   
        private int sorszam;       
        public int index;         
        private int x = 0;          
        private int y = 0;   
        private int szin = 0;                 
        private int kitoltes = 0;     
        private String felirat = "";  
      
        
        public Csucs(int sorszam, int index)
        {
            this.sorszam = sorszam;
            this.index = index;
        }   
        
        public void szinez(int szin, int kitoltes)
        {      
             this.szin = szin;
             this.kitoltes = kitoltes;   
        }
        
        public void felirat(String text)
        {
            this.felirat = text;
        }
        
        public void csucsRajzolas(GrafElemRajzolo rajzolo, int x, int y)
        { 
            java.awt.Color csSzin;
            java.awt.Color csKitoltes;
            String text;
            csSzin = GrafElemRajzolo.SZINEK[szin];
            csKitoltes = rajzolo.kitoltesiSzin(szin, kitoltes); 
            text = this.felirat;
            rajzolo.csucsRajzolas(x, y, csSzin, csKitoltes, toString(), text);  
        }
        
        public int getSorszam()
        {
            return sorszam;
        }
        
        public int getX()
        {
            return x;
        }
        
        public int getY()
        {
            return y;
        }
      
        public void setKoordinatak(int x, int y)
        {
            this.x = x < 0 ? 0 : x;
            this.y = y < 0 ? 0 : y;
        }
        
        @Override
        public String toString()
        {
            return "" + sorszam;
        }
    }

    
    public class El 
    {    
        private int suly;          
        private final Csucs u;    // Egyik végpont   
        private final Csucs v;    // Másik végpont      
        private int szin = 0;     
        private int nyilszin = 0;
      
        
        public El(Csucs u, Csucs v, int suly)
        {
            this.u = u;
            this.v = v;
            this.suly = suly;
        }
        
        public void szinez(int szin)
        {       
            this.szin = szin;
        }
        
        public void nyilSzinez(int szin)
        {
            this.nyilszin = szin;
        }
        
        public void elRajzolas(GrafElemRajzolo rajzolo, int x1, int y1, int x2, int y2)
        {  
            java.awt.Color elSzin; 
            elSzin = GrafElemRajzolo.SZINEK[szin];
            
            java.awt.Color nySzin; 
            nySzin = GrafElemRajzolo.SZINEK[nyilszin];
            
            if(u.sorszam > v.sorszam)
                rajzolo.elRajzolas(x1, y1, x2, y2, elSzin, toString());  
            if (u.sorszam < v.sorszam)
                rajzolo.elRajzolas(x1, y1, x2, y2, elSzin, toString());
            
            if (u.sorszam > v.sorszam)    // a kisebb sorszámú csúcs fele mutat a nyíl
            { 
                rajzolo.elRajzolasNyillal(x2, y2, x1, y1, nySzin); 
            }
            if (u.sorszam < v.sorszam)    // a nagyobb sorszámú csúcs fele mutat a nyíl
            {
                rajzolo.elRajzolasNyillal(x1, y1, x2, y2, nySzin);
            } 
        }
         
        public int getSuly()
        {
            return suly;
        }
  
        @Override
        public String toString()
        {
            return "" + suly;
        }
    }

  
    public static class GrafElemRajzolo 
    {
        public static final int SUGAR = 15;        
        private final int xCsucs = 15;                        
        private final int yCsucs = 15;                         
        private final java.awt.Stroke csucsKorVastagsag;          
        private final java.awt.Stroke elVastagsag;               
        private final java.awt.Font csucsBetuTipus;                
        private final java.awt.Font elBetuTipus;                 
        private final java.awt.FontMetrics csucsBetuLeiro;                                                                                
        private java.awt.Graphics2D rajzlap; 
  
        
        private static final java.awt.Color[] SZINEK = 
        {
            java.awt.Color.BLACK, 
            java.awt.Color.LIGHT_GRAY, 
            java.awt.Color.BLUE
        };
        
        // Kitöltő szín
        private static final java.awt.Color[] KITOLTO = 
        {
            java.awt.Color.LIGHT_GRAY, 
            new java.awt.Color(224, 224, 224), 
        };
        
        // Fehér
        private static final java.awt.Color WHITE = java.awt.Color.WHITE;
        

        public java.awt.Color kitoltesiSzin(int szin, int kitoltes)
        {
            switch(kitoltes){
                case 1:
                    return KITOLTO[szin];
                default:
                    return WHITE;
            }
        }
                 
        public GrafElemRajzolo(java.awt.Graphics2D rajzlap)
        {
            this.rajzlap = rajzlap;
            csucsKorVastagsag = new java.awt.BasicStroke(2);
            elVastagsag = new java.awt.BasicStroke(2);
            csucsBetuTipus = rajzlap.getFont().deriveFont(java.awt.Font.BOLD, 15);
            csucsBetuLeiro = rajzlap.getFontMetrics(csucsBetuTipus);
            elBetuTipus = rajzlap.getFont().deriveFont(java.awt.Font.BOLD, 12);    
            rajzlap.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        } 
        
        public final void csucsRajzolas(int x, int y, java.awt.Color szin, 
                java.awt.Color kitoltes, String text, String felirat)
        { 
            rajzlap.setStroke(csucsKorVastagsag);
            rajzlap.setFont(csucsBetuTipus);
            rajzlap.setColor(kitoltes == null ? WHITE : kitoltes);
            rajzlap.fillOval(x - xCsucs, y - yCsucs, 2*xCsucs, 2*yCsucs);
            rajzlap.setColor(szin == null ? SZINEK[0] : szin);
            rajzlap.drawOval(x - xCsucs, y - yCsucs, 2*xCsucs, 2*yCsucs);
            rajzlap.drawString(text, x - csucsBetuLeiro.stringWidth(text)/2, y + 6); 
            rajzlap.setColor(SZINEK[1]);
            rajzlap.drawString(felirat, x-7, y-22);  
        }
        
        public final void elRajzolas(int x1, int y1, int x2, int y2, java.awt.Color szin, String text)
        { 
            // Él
            rajzlap.setStroke(elVastagsag);
            rajzlap.setColor(szin == null ? SZINEK[0] : szin);
            rajzlap.drawLine(x1, y1, x2, y2);
            
            // Felirat
            int x = (x1+x2)/2;
            int y = (y1+y2)/2;
            rajzlap.setFont(elBetuTipus);
            rajzlap.setColor(java.awt.Color.RED);
            rajzlap.drawString(text, x, y+10);
        }
   
        public final void elRajzolasNyillal(int x1, int y1, int x2, int y2, java.awt.Color szin)
        {
            int nyilHegyx, nyilHegyy, nyil1SzarnyX, nyil1SzarnyY, nyil2SzarnyX, nyil2SzarnyY;          
            rajzlap.setColor(szin == null ? SZINEK[0] : szin);
                   
            double[] iranyVektor = {x2-x1, y2-y1};  
            int d =(int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));   //vektor hossza
            iranyVektor[0] = iranyVektor[0] / d;    // irány vektor normalizálása
            iranyVektor[1] = iranyVektor[1] / d;    // irány vektor normalizálása
                
            // Nyílhegy koordinátája
            nyilHegyx = (int) (x1 + (d - SUGAR) * iranyVektor[0]);
            nyilHegyy = (int) (y1 + (d - SUGAR) * iranyVektor[1]);
                 
            // Nyíl szárnyának egyik vége
            nyil1SzarnyX = (int) (x1 + (d - 25) * iranyVektor[0] + iranyVektor[1] * 5);
            nyil1SzarnyY = (int) (y1 + (d - 25) * iranyVektor[1] + iranyVektor[0] * 5 * -1);
                 
            // Nyíl szárnyának másik vége
            nyil2SzarnyX = (int) (x1 + (d - 25) * iranyVektor[0] + iranyVektor[1] * 5 * -1);
            nyil2SzarnyY = (int) (y1 + (d - 25) * iranyVektor[1] + iranyVektor[0] * 5);
                       
            int [] x = {nyil1SzarnyX, nyilHegyx, nyil2SzarnyX};
            int [] y = {nyil1SzarnyY, nyilHegyy, nyil2SzarnyY};
            rajzlap.drawPolyline(x, y, 3);      
        }  
    }
}
      

