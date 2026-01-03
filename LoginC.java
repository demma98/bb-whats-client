
//package ;

import javax.microedition.lcdui.*;

class LoginC extends ListC{
    
    InputField numField;
    
    LoginC(Demma app) {
        super(app);
        
        setList(new String[]{"number:", "", ""});
        
        title = "login";
        
        off_x_f = 5;
        off_y_f = 40;
        
        numField = new InputField(getWidth() - 10);
        
        numField.x = 5;
        numField.y = this.charHeight() + off_y_f;
    }
    
    
    protected void paint(Graphics g){
        super.paint(g);
        
        numField.paint(g, charHeight());
    }
    
    protected void keyPressed(int keycode){
        
        if(keycode == -8 || keycode == '\n'){ //press middle button
            try {
                AccountC.setNumber(AccountC.getAccount(), numField.toString().getBytes());
                String resp = TunnelD.sendResp("\"req\" : \"login\"");
            
                if(StringD.getFromJSON(resp, "\"status\"").equals("success")){
                    AccountC.setPassword(AccountC.getAccount(), new byte[]{});
                    app.setStateLoading(app.STATE_CODE);
                }
                else{
                    list[2] = "failed " + StringD.getFromJSON(resp, "\"status\"") + " " + resp;
                }
            }
            catch (Exception e){
                list[2] = e.toString();
            }
        }
        else if('0' <= keycode && keycode <= '9'){
            numField.keyPressed(keycode);
        }
        else if(keycode == 8){  // delete key
            if(numField.toString().length() > 0){
                numField.delete();
            }
        }
        
        super.keyPressed(keycode);
    }
    
} 
