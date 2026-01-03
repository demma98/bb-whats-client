
//package ;

class CodeC extends ListC implements Runnable{
    
    Thread getCode;
    
    boolean threadContinue = true;
    int count = 0;
    
    CodeC(Demma app) {
        super(app);
        
        setList(new String[]{""});
        
        title = "pairing code";
        
        off_x_f = 5;
        off_y_f = 40;
        
        list[0] = "waiting";
        
        getCode = new Thread(this);
        getCode.start();
    }
    
    public void run(){
        while(threadContinue && count < 20){
            try {
                String resp = TunnelD.sendResp("\"req\" : \"code\", \"num\" : \"" + AccountC.getNumber(AccountC.getAccount()) + "\"");
                
                if(StringD.getFromJSON(resp, "\"status\"").equals("success")){
                    AccountC.setPassword(AccountC.getAccount(), StringD.getFromJSON(resp, "\"password\"").getBytes());
                    threadContinue = false;
                    app.setStateLoading(app.STATE_MAIN_CHATS);
                }
                else if(StringD.getFromJSON(resp, "\"auth\"").equals("false")){
                    threadContinue = false;
                    list[0] = "no auth";
                }
                else {
                    list[0] = StringD.getFromJSON(resp, "\"cause\"");
                }
            }
            catch (Exception e){
                list[0] = e.toString();
                threadContinue = false;
            }
            
            try{
                Thread.sleep(5000);
            }
            catch(Exception e){}
            count++;
        }
    }
} 
