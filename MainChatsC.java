
//package ;

import javax.microedition.lcdui.*;

class MainChatsC extends ListC{
    String[] chats;
    String[] ids;
    
    Command EXIT = new Command("Exit", Command.EXIT, 2);
    
    MainChatsC(Demma app) {
        super(app);
        
        this.addCommand(EXIT);
        
        try {
            TunnelD tunnel = new TunnelD();
            
            String resp = tunnel.sendResp("\"req\": \"chats\"");
            
            if(StringD.getFromJSON(resp, "\"status\"").equals("success")){
                chats = getListFromString(StringD.getFromJSON(resp, "\"chats\""));
                ids = getListFromString(StringD.getFromJSON(resp, "\"ids\""));
                
                setList(chats);
            }
            else{
                paintError(resp, getWidth() - 10);
            }
        }
        catch (Exception e) {
            paintError(e.toString());
        }
        
        off_x_f = 10;
        off_y_f = 32;
        max_y = getHeight() - 48;
        title = "chats";
    }
    
    protected void keyPressed(int keycode){
        
        if(keycode == -8){ //press middle button
            app.temp = ids[select];
            app.setStateLoading(app.STATE_CHAT);
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
} 
