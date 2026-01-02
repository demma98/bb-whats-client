
//package ;

import javax.microedition.lcdui.*;

class InputField {
    
    String s;
    
    public int x = 0;
    public int y = 0;
    
    public int max_x;
    
    int cursor = 0;
    
    boolean accent = false;
    
    InputField(int max_x) {
        this.max_x = max_x;
        
        clear();
    }
    
    public void keyPressed(int keycode){
        if(keycode == '\''){
            accent = !accent;
            
            if(accent)
                return;
        }
        
        if(accent){
            
            if(keycode == 'a')
                keycode = 'á';
            else if(keycode == 'A')
                keycode = 'Á';
            else if(keycode == 'e')
                keycode = 'é';
            else if(keycode == 'E')
                keycode = 'É';
            else if(keycode == 'i')
                keycode = 'í';
            else if(keycode == 'I')
                keycode = 'Í';
            else if(keycode == 'o')
                keycode = 'ó';
            else if(keycode == 'O')
                keycode = 'Ó';
            else if(keycode == 'u')
                keycode = 'ú';
            else if(keycode == 'U')
                keycode = 'Ú';
            else if(keycode == 'n')
                keycode = 'ñ';
            else if(keycode == 'N')
                keycode = 'Ñ';
            
            accent = false;
        }
        
        if(!accent){
            
            String s_1 = s.substring(0, cursor);
            String s_2 = s.substring(cursor, s.length());
            
            s = s_1 + (char)keycode + s_2;
            cursor++;
        }
    }
    
    public void paint(Graphics g, int height){
        g.setColor(255, 235, 255);
        g.setStrokeStyle(Graphics.SOLID);
        
        int c = g.getFont().substringWidth(s, 0, cursor);
        
        if(x + c < max_x){
            g.drawString(s, x, y, (Graphics.TOP | Graphics.LEFT));
            g.drawLine(x + c, y - 3, x + c, y + height - 4);
        }
        else{
            c = g.getFont().stringWidth(s) - c;
            
            g.drawString(s, max_x, y, (Graphics.TOP | Graphics.RIGHT));
            g.drawLine(max_x - c, y - 3, max_x - c, y + height - 4);
        }
    }
    
    public void clear(){
        s = "";
        cursor = 0;
        accent = false;
    }
    
    public void delete(){
        if(s.length() > 0){
            String s_1 = s.substring(0, cursor - 1);
            String s_2 = s.substring(cursor, s.length());
            s = s_1 + s_2;
            cursor--;
        }
    }
    
    public void cursorLeft(){
        if(cursor > 0)
            cursor--;
    }
    
    public void cursorRight(){
        if(cursor < s.length())
            cursor++;
    }
    
    public String toString(){
        return s;
    }
    
    public void setString(String s){
        this.s = s;
        cursor = s.length();
    }
} 
