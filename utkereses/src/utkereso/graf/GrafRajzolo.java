package utkereso.graf;

/**
 *
 * @author Takacs Peter
 */

public class GrafRajzolo extends javax.swing.JPanel 
{
   
    private java.awt.Image grafKepe = null; 
    private java.awt.Image hatter = null;                            
    private Graf graf;
    private Graf.Csucs mouse_evt_vertex = null;               // Egér esemény gráf csúcs eleme
    private java.awt.Point mouse_evt_position_move = null;    // Egér esemény helye csúcs mozgatásakor   
    private java.awt.Point mouse_evt_position = null;         // Egér esemény helye
    
   
    public GrafRajzolo(Graf graf) 
    {
        this.graf = graf;

        addComponentListener(new java.awt.event.ComponentAdapter() 
        {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) 
            {
                hatter = null;
                frissit();
            }
        });
  
        EgerEsemeny ee = new EgerEsemeny();
        addMouseListener(ee);
        addMouseMotionListener(ee);
    }
    
    
    @Override
    public final void paint(java.awt.Graphics rajzlap) 
    {
        if(grafKepe == null)
        {
            if(hatter == null)
            {
                hatter = createImage(getWidth(), getHeight());
                paintHatter((java.awt.Graphics2D) hatter.getGraphics());
            }
            grafKepe = createImage(getWidth(), getHeight());
            paintGraf((java.awt.Graphics2D) grafKepe.getGraphics()); 
        }
        rajzlap.drawImage(grafKepe, 0, 0, this);
        frissit();
    }

    
   public void paintGraf(java.awt.Graphics2D rajzlap)
   { 
        rajzlap.drawImage(hatter, 0, 0, this);
        Graf.GrafElemRajzolo rajzolo = graf.grafRajzolas(rajzlap);
        if(mouse_evt_vertex != null && mouse_evt_position != null)
        {
            rajzolo.elRajzolas(mouse_evt_vertex.getX(), mouse_evt_vertex.getY(),
                    mouse_evt_position.x, mouse_evt_position.y, null, "");   
        }
    }

    
    public void paintHatter(java.awt.Graphics2D rajzolasiCel)
    {
        rajzolasiCel.setColor(java.awt.Color.WHITE);
        rajzolasiCel.fillRect(0, 0, getWidth(), getHeight());
    }

  
    public void frissit()
    {
        grafKepe = null;
        repaint();
        revalidate();
    }
    
    
    
    public class EgerEsemeny extends java.awt.event.MouseAdapter 
    {
               
        @Override
        public void mousePressed(java.awt.event.MouseEvent event) 
        {
            java.awt.Point p = event.getPoint();
            mouse_evt_vertex = graf.csucsKereses(p.x, p.y);
            mouse_evt_position = null;
           
            if((event.getButton() == java.awt.event.MouseEvent.BUTTON1) 
                    && (event.getClickCount()==1)
                    && (mouse_evt_vertex == null))
            {
                mouse_evt_vertex = graf.csucsHozzaad(p.x, p.y);
                frissit();
            }
            else
            {
                if(event.getButton() == java.awt.event.MouseEvent.BUTTON3)
                {
                    mouse_evt_position = p;
                }
            }
                                 
            if(mouse_evt_vertex != null)
            {
               mouse_evt_position_move = new java.awt.Point(mouse_evt_vertex.getX() - event.getX(), 
                        mouse_evt_vertex.getY() - event.getY());
            }   
        }
        
        
        @Override
        public void mouseReleased(java.awt.event.MouseEvent event) {
            if(mouse_evt_vertex != null)
            {
                if(mouse_evt_position != null)
                {
                    Graf.Csucs v = graf.csucsKereses(event.getX(), event.getY());
                    if(v != null && !v.equals(mouse_evt_vertex))
                    {               
                        int db = graf.elSzam(mouse_evt_vertex.getX(), mouse_evt_vertex.getY(), 
                                mouse_evt_position.x, mouse_evt_position.y);
                        if(db!=2)
                        graf.elHozzaad(mouse_evt_vertex, v); 
                        frissit();
                    }
                    else
                    {
                        frissit();
                    } 
                }
            }
            mouse_evt_vertex = null;
            mouse_evt_position = null;
        }
        
        
        @Override
        public void mouseDragged(java.awt.event.MouseEvent event) {
            if(mouse_evt_vertex != null)
            {
                if(mouse_evt_position == null)
                {
                    int x = mouse_evt_position_move.x + event.getX();
                    int y = mouse_evt_position_move.y + event.getY();
                    mouse_evt_vertex.setKoordinatak(x, y);
                    frissit();
                }
                else
                {
                    mouse_evt_position = event.getPoint();
                    frissit();
                }
            }
        }
    }
  
}
