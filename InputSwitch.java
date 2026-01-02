
//package ;

import javax.microedition.lcdui.*;

class InputSwitch {
    
    public int x;
    public int y;
    String s_0;
    String s_1;
    
    public boolean active = false;
    
    Demma app;
    
    InputSwitch(Demma app){
        this(app, 0, 0);
    }
    
    InputSwitch(Demma app, int x, int y){
        this(app, x, y, "", "");
    }
    
    InputSwitch(Demma app, int x, int y, String s_0, String s_1) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.s_0 = s_0;
        this.s_1 = s_1;
    }
    
    public void paint(Graphics g, Font font){
        g.setColor(app.color_font);
        g.fillRect(x - 16, y, 32, 16);
        g.setColor(app.color_reply_bg);
        if(active){
            g.fillRect(x + 6, y + 1, 9, 14);
        }
        else{
            g.fillRect(x - 15, y + 1, 9, 14);
        }
        
        g.setColor(app.color_font);
        g.drawString(s_0, x - 20, y, (Graphics.TOP | Graphics.RIGHT));
        g.drawString(s_1, x + 20, y, (Graphics.TOP | Graphics.LEFT));
    }
    
    public void press(){
        active = !active;
    }
} 
