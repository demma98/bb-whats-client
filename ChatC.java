
//package ;

import javax.microedition.lcdui.*;

class ChatC extends ListC{
    
    InputField inputField;
    
    String chatId;
    
    String[] messages;
    String[] fromMe;
    String[] ids;
    String[] names;
    int[] indexes;
    
    boolean isGroup;
    
    String selectReply = "";
    
    Command BACK = new Command("Back", Command.BACK, 3);
    
    private int STATE_SINGLE_MESSAGE = 42;
    int selectMessage = 0;
    String[] singleMessage;
    
    ChatC(Demma app, String id) {
        super(app);
        
        chatId = id;
        
        
        this.addCommand(BACK);
        
        loadMessages();
        
        inputField = new InputField(getWidth() - 10);
        inputField.x = 5;
        inputField.y = this.getHeight() - 22;
        
        off_x_f = 5;
        off_y_f = 32;
        max_y = getHeight() - 84;
        
        keyPressed(getKeyCode(DOWN));
    }
    
    protected void loadMessages(){        
        try {
            TunnelD tunnel = new TunnelD();
            
            String resp = tunnel.sendResp("\"req\": \"messages\", \"id\" : \"" + chatId + "\"");
            
            if(StringD.getFromJSON(resp, "\"status\"").equals("success")){
            
                messages = getListFromString(StringD.getFromJSON(resp, "\"messages\""));
                
                fromMe = getListFromString(StringD.getFromJSON(resp, "\"fromMe\""));
                
                ids = getListFromString(StringD.getFromJSON(resp, "\"ids\""));
                
                isGroup = StringD.getFromJSON(resp, "\"isGroup\"").equals("true");
                
                title = StringD.getFromJSON(resp, "\"name\"");
                
                if(isGroup)
                    names = getListFromString(StringD.getFromJSON(resp, "\"names\""));
                
                setList(messages);
                formatMessages();
                
                select = list.length - 1;
                
                keyPressed(getKeyCode(DOWN));
            }
            else{
                //paintError(StringD.getFromJSON(resp, "\"messages\""));
                //paintError(StringD.getFromJSON(resp, "\"isGroup\"") + "-" + resp, this.getWidth() - 10);
                paintError(resp, getWidth() - 10);
            }
        }
        catch (Exception e) {
            //paintError(e.toString(), this.getWidth() - 10);
            paintError(e.toString());
        }
    }
    
    
    protected void paint(Graphics g){
        if(state == STATE_LIST){
            int y = off_y + off_y_f;
            int x = off_x + off_x_f;
            int in;
            
            g.setColor(app.color_bg);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setStrokeStyle(Graphics.SOLID);
            g.setColor(app.color_font);
            
            boolean me_temp;
                        
            for(int i = 0; i < list.length && y <= getHeight(); i++){
                in = indexes[i];
                me_temp = fromMe[in].equals("true");
                
                if(selectReply == ids[indexes[i]]){
                    g.setColor(app.color_reply_bg);
                }
                else if(select == i && list[i].equals("...\n")){
                    g.setColor(app.color_reply_bg);
                    
                }
                else if(indexes[select] == in){
                    g.setColor(app.color_selected);
                }
                else if(me_temp){
                    g.setColor(app.color_chat_me);
                }
                else{
                    g.setColor(app.color_chat_not_me);
                }
                
                g.fillRect(5, y - 2, getWidth() - 10, font.getHeight() + 2);
                
                if(i > 0){
                    if(indexes[i] == indexes[i - 1])
                        g.fillRect(5, y - y_space, getWidth() - 10, y_space);
                }
                
                g.setColor(app.color_font);
                
                if(me_temp){
                    g.drawString(list[i], getWidth() - (x * 2), y, (Graphics.TOP | Graphics.RIGHT));
                }
                else {
                    g.drawString(list[i], x, y, (Graphics.TOP | Graphics.LEFT));
                }
                
                y += charHeight();
            }
            
            g.setColor(app.color_input_field);
            g.fillRect(0, getHeight() - 10 - font.getHeight(), getWidth(), font.getHeight() + 10);
            inputField.paint(g, font.getHeight());
            
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
        else if(state == STATE_SINGLE_MESSAGE){
            g.setColor(app.color_bg);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(app.color_font);
            g.setStrokeStyle(Graphics.SOLID);
            
            int y = 5 + (charHeight() * -selectMessage);
            for(int i = 0; i < singleMessage.length; i++){
                g.drawString(singleMessage[i], getWidth()/2, y, (Graphics.TOP | Graphics.HCENTER));
                y += charHeight();
            }
        }
    }
    
    protected void keyPressed(int keycode){
        int left = getKeyCode(LEFT);
        int right = getKeyCode(RIGHT);
        
        if(state == STATE_LIST){
            if(keycode == -8){  // ok key
                if(list[select].equals("...\n")){
                    state = STATE_SINGLE_MESSAGE;
                    singleMessage = StringD.cropString(messages[indexes[select]], font, getWidth() - 10, 40);
                    selectMessage = 0;
                }
                else {
                    if(selectReply == ids[indexes[select]]){
                        selectReply = "";
                    }
                    else{
                        selectReply = ids[indexes[select]];
                    }
                }
            }
            else if(keycode == '\n'){   // return key
                if(inputField.toString().length() > 0){
                    try{
                        TunnelD tunnel = new TunnelD();
                    
                        if(selectReply.equals(""))
                            tunnel.send("\"req\": \"send\", \"id\" : \"" + chatId + "\", \"body\" : \"" + StringD.safe(inputField.toString()) + "\"");
                        else
                            tunnel.send("\"req\": \"send\", \"id\" : \"" + chatId + "\", \"body\" : \"" + StringD.safe(inputField.toString()) + "\", \"replyTo\" : \"" + selectReply + "\"");
                        
                        selectReply = "";
                        
                        inputField.clear();
                        
                        Thread.sleep(100);
                        
                    }
                    catch (Exception e) {
                        paintError(e.toString());
                    }
                }
                
                loadMessages();
            }
            else if(' ' <= keycode && keycode <= '~'){  //  text key
                inputField.keyPressed(keycode);
            }
            else if(keycode == 8){  // delete key
                if(inputField.toString().length() > 0){
                    inputField.delete();
                }
            }
            else if(keycode == left){   // left key
                inputField.cursorLeft();
            }
            else if(keycode == right){  //right key
                inputField.cursorRight();
            }
        }
        else if(state == STATE_SINGLE_MESSAGE) {
            int up = getKeyCode(UP);
            int down = getKeyCode(DOWN);
            
            if(keycode == up){
                selectMessage--;
            }
            else if(keycode == down){
                selectMessage++;
            }
            else if(keycode != left && keycode != right){
                state = STATE_LIST;
            }
            
            if(selectMessage < 0)
                selectMessage = 0;
            else if(selectMessage >= singleMessage.length)
                selectMessage = singleMessage.length - 1;
        }
        super.keyPressed(keycode);
    }
    
    protected void keyRepeated(int keycode){
        if(keycode == 8 || (' ' <= keycode && keycode <= '~')){   // delete key or text keys
            keyPressed(keycode);
        }
    }
    
    public void commandAction(Command command, Displayable d){
        if(command == BACK){
            if(state == STATE_SINGLE_MESSAGE){
                state = STATE_LIST;
            }
            else{
                if(inputField.toString().length() == 0){
                    app.setStateLoading(app.STATE_MAIN_CHATS);
                }
                else{
                    inputField.clear();
                    selectReply = "";
                    repaint();
                }
            }
        }
        super.commandAction(command, d);
    }
    
    public void formatMessages(){
        int max_size = 5;
        
        String[][] l_temp = new String[list.length][];
        
        int i;
        int j = 0;
        int k;
        
        for(i = 0; i < list.length; i++){
            if(isGroup && fromMe[i].equals("false")){
                if(i > 0){
                    if(!names[i].equals(names[i - 1])){
                        if(list[i].length() > 0)
                            list[i] = names[i] + ":\n" + list[i];
                        else
                            list[i] = names[i] + ":\n" + list[i] + " ";
                    }
                }
                else{
                    if(list[i].length() > 0)
                        list[i] = names[i] + ":\n" + list[i];
                    else
                        list[i] = names[i] + ":\n" + list[i] + " ";
                }
            }
            l_temp[i] = StringD.cropString(list[i], font, getWidth() - 26, max_size);
            j += l_temp[i].length;
        }
        
        String[] m_temp = new String[j];
        indexes = new int[j];
        
        i = 0;
        for(j = 0; j < l_temp.length; j++){
            for(k = 0; k < l_temp[j].length; k++){
                m_temp[i] = l_temp[j][k];
                indexes[i] = j;
                i++;
            }
        }
        
        setList(m_temp);
    }
}
