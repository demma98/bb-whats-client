
//package ;

import javax.microedition.lcdui.*;

class LoadingC extends Canvas implements Runnable{
    
    Demma app;
    
    Font font;
    
    int count = 0;
    
    Thread loading;
    
    LoadingC(Demma app) {
        this.app = app;
        font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        
        loading = new Thread(this);
        loading.start();
    }
    
    protected  void paint(Graphics g){
        g.setColor(152, 44, 226);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g.setColor(235, 235, 255);
        g.setStrokeStyle(Graphics.SOLID);
        
        String l = "loading";
        for(int i = 0; i < count; i++){
            l = "- " + l + " -";
        }
        g.drawString(l, this.getWidth()/2, this.getHeight()/2, (Graphics.BASELINE | Graphics.HCENTER));
    }
    
    public void run(){
        while(true){
            try{
                Thread.sleep(200);
            }catch (Exception e){}
            count ++;
            if(count > 15){
                count = 0;
            }
            repaint();
        }
    }
    
} 
