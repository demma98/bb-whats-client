
//package ;

import javax.microedition.lcdui.*;

class AccountC extends ListC {
    static final int max = 3;
    
    static final String file = "accounts";
    
    Command EXIT = new Command("Exit", Command.EXIT, 2);
    
    AccountC(Demma app) {
        super(app);
        
        this.addCommand(EXIT);
        
        String[] l_temp = new String[max];
        
        for(int i = 0; i < max; i++){
            String b = getNumber(i);
            if(b.length() == 0){
                b = "+";
            }
            l_temp[i] = b;
        }
        
        setList(l_temp);
        
        title = "accounts";
        
        off_x_f = 5;
        off_y_f = 40;
    }
    
    protected void keyPressed(int keycode){
        if(keycode == -8){ //press middle button
            setAccount(select);
            
            if(getSelected().equals("+"))
                app.setStateLoading(app.STATE_LOGIN);
            else if(getPassword(getAccount()).equals(""))
                app.setStateLoading(app.STATE_CODE);
            else
                app.setStateLoading(app.STATE_MAIN_CHATS);
        }
        else if(keycode == 8){  // delete key
            list[select] = "+";
        }
        
        super.keyPressed(keycode);
    }
    
    public void commandAction(Command command, Displayable d){
        if(command == EXIT){
            app.destroyApp(false);
            app.notifyDestroyed();
        }
        
        super.commandAction(command, d);
    }
    
    public static int getAccount(){
        try {
            return (int)SettingsC.getRecord(0, file)[0];
        }
        catch (Exception e){
            return 0;
        }
    }
    
    public static String getNumber(int id){
        try {
            String num = new String(SettingsC.getRecord(1 + (id * 2), file), "UTF-8");
            for(int i = 0; i < num.length(); i++){
                if(num.charAt(i) < '0' || '9' < num.charAt(i)){
                    num = "";
                }
            }
            return num;
        }
        catch (Exception e){
            return "";
        }
    }
    
    public static String getPassword(int id){
        try {
            return new String(SettingsC.getRecord(2 + (id * 2), file), "UTF-8");
        }
        catch (Exception e){
            return "";
        }
    }
    
    public void setAccount(int id){
        SettingsC.setRecord(0, new byte[]{(byte) id}, file);
    }
    
    public static void setNumber(int id, byte[] bytes){
        SettingsC.setRecord(1 + (id * 2), bytes, file);
    }
    
    public static void setPassword(int id, byte[] bytes){
        SettingsC.setRecord(2 + (id * 2), bytes, file);
    }
} 
