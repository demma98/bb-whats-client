
//package ;

import javax.microedition.lcdui.*;

class ListC extends Canvas implements CommandListener, Runnable{
    
    Demma app;
    
    String list[];
    
    int select = 0;
    
    int animation = 0;
    int animation_d = 1;
    Thread animation_t;
    
    //String msgS;
    String title = "";
    
    protected Font font;
    
    int off_x = 0;
    int off_y = 0;
    int off_x_f = 0;
    int off_y_f = 0;
    int max_y;
    int y_space = 4;
    
    int state;
    final int STATE_LIST = 1;
    final int STATE_ERROR = 2;
    
    ListC(Demma app) {
        this.app = app;
        font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        setState(STATE_LIST);
        max_y = getHeight();
        
        setCommandListener(this);
        
        animation_t = new Thread(this);
        animation_t.start();
    }
    
    public String[] getListFromString(String s){
        return getListFromString(s, 0);
    }
    
    public String[] getListFromString(String s, int n){
        String s_temp = s;
        
        char c = 0;
        char cA;
        
        int l_start = 0;
        int l_end = 0;
        
        int l_n = 0;
        
        for(int i = 0; i < s.length() && l_n <= n; i++){
            cA = s.charAt(i);
            if(cA == '\\'){
                i++;
            }
            else {
                if(c == 0){
                    if(cA == '\'' || cA == '"' || cA == '`'){
                        c = cA;
                    }
                    else if(cA == '['){
                        l_start = i;
                    }
                    else if(cA == ']'){
                        l_end = i;
                        l_n++;
                    }
                }
                else {
                    if(cA == c){
                        c = 0;
                    }
                }
            }
        }
        
        s_temp = s_temp.substring(l_start, l_end);
        
        String[] b_temp = new String[30];
        l_n = 0;
        for(int i = 0; i < s_temp.length() && l_n < b_temp.length; i++){
            if(b_temp[l_n] == null){
                b_temp[l_n] = "";
            }
            
            cA = s_temp.charAt(i);
            if(cA == '\\'){
                i++;
                
                if(c != 0){
                    cA = s.charAt(i);
                    if(cA == 'n'){
                        b_temp[l_n] += '\n';
                    }
                    else {
                        b_temp[l_n] += s_temp.charAt(i);
                    }
                }
            }
            else {
                if(c == 0){
                    if(cA == '\'' || cA == '"' || cA == '`'){
                        c = cA;
                    }
                    else if(cA == ','){
                        l_n++;
                    }
                }
                else {
                    if(cA == c){
                        c = 0;
                    }
                    else{
                        b_temp[l_n] += cA;
                    }
                }
            }
        }
        if(l_n < b_temp.length){
            l_n++;
        }
        
        String[] resp = new String[l_n];
        for(int i = 0; i < l_n; i++){
            resp[i] = b_temp[i];
        }
        
        return resp;
    }
    
    protected void paint(Graphics g){
        g.setColor(app.color_bg);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        paintList(g);
    }
    
    protected  void paintList(Graphics g){
        if(state == STATE_LIST){
            int y = off_y + off_y_f;
            int x = off_x + off_x_f;
            
            g.setColor(app.color_font);
            g.setStrokeStyle(Graphics.SOLID);
            for(int i = 0; i < list.length && y <= getHeight(); i++){
                if(i == select){
                    g.setColor(app.color_selected);
                    g.fillRect(5, y - 2, this.getWidth() - 10, font.getHeight() + 2);
                    
                    g.setColor(app.color_font);
                    g.setStrokeStyle(Graphics.SOLID);
                }
                g.drawString(list[i], x, y, (Graphics.TOP | Graphics.LEFT));
                
                y += charHeight();
            }
            
            if(title.length() > 0)
                paintTitle(g);
        }
        else if (state == STATE_ERROR){
            g.setColor(app.color_error_bg);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            g.setColor(app.color_error_font);
            g.setStrokeStyle(Graphics.SOLID);
            int y = 5;
            for(int i = 0; i < list.length; i++){
                g.drawString(list[i], 5, y, (Graphics.TOP | Graphics.LEFT));
                y += charHeight();
            }
        }
    }
    
    protected void paintTitle(Graphics g){
        g.setColor(app.color_title_bg);
        g.fillRect(0, 0, getWidth(), 27);
        
        g.setColor(app.color_font);
        g.setStrokeStyle(Graphics.SOLID);
        
        String t_temp = title;
        for(int i = 0; i < animation; i++)
            t_temp = "- " + t_temp + " -";
            
        g.drawString(t_temp, getWidth()/2, 4, (Graphics.TOP | Graphics.HCENTER));
    }
    
    protected void keyPressed(int keycode) {
        if(state == STATE_LIST){
            int up = getKeyCode(UP);
            int down = getKeyCode(DOWN);
            
            if(keycode == down) {
                if(select + 1 != list.length){
                    select++;
                }
            }
            else if(keycode == up){
                if(select != 0){
                    select--;
                }
            }
            
            int s_temp = select * (charHeight());
            if(- s_temp > off_y){
                off_y = -s_temp;
            }
            if(s_temp + off_y > max_y){
                //off_y = ((max_y / charHeight()) * charHeight()) - s_temp;
                off_y = max_y - s_temp;
            }
        }
        
        repaint();
    }
    
    public int charHeight(){
        return font.getHeight() + y_space;
    }
    
    public void setList(String[] list){
        this.list = list;
        repaint();
    }
    
    public void paintError(String e){
        list = new String[]{e};
        setState(STATE_ERROR);
    }
    
    public void paintError(String e, int max_x){
        list = StringD.cropString(e, font, max_x, 20);
        setState(STATE_ERROR);
    }
    
    void setState(int state){
        this.state = state;
    }
    
    public String getSelected(){
        if(list.length > 0)
            return list[select];
        else
            return "";
    }
    
    public void commandAction(Command command, Displayable d){
        if(command == app.SETTINGS){
            app.setState(app.STATE_SETTINGS);
        }
        else if(command == app.ACCOUNTS){
            app.setState(app.STATE_ACCOUNT);
        }
        
        repaint();
    }
    
    public void run(){
        while(true){
            try{Thread.sleep(500);}catch(Exception e){};
            
            animation += animation_d;
            if(animation == 0 || animation == 3){
                animation_d = -animation_d;
            }
            repaint();
        }
    }
}

