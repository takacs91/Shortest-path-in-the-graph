package utkereso.panelek;

import utkereso.graf.*;

/**
 *
 * @author Takacs Peter
 */

public class MainClass {
    
    public static class Applet extends javax.swing.JApplet {
      
        @Override
        public void init(){
            MainClass main = new MainClass();
            setContentPane(main.tervezoPanel);
        }
    }
    
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                javax.swing.JFrame frame = new javax.swing.JFrame();
                MainClass main = new MainClass();
                frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                main.cim="Legrövidebb útkereső algoritmusok gráfokban";
                frame.setTitle(main.cim);
                frame.setContentPane(main.tervezoPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
            }
        });
    }
    
    private String cim;
    private final TervezoPanel tervezoPanel;
    private final MuveletekPanel muveletekPanel;
    private final Graf graf;
    private final GrafRajzolo grafrajzolo;

    
    public MainClass(){
        
        graf = new Graf(); 
        muveletekPanel = new MuveletekPanel(graf);
        grafrajzolo = new GrafRajzolo(graf);
        tervezoPanel = new TervezoPanel(grafrajzolo, muveletekPanel);
    }
       
}
